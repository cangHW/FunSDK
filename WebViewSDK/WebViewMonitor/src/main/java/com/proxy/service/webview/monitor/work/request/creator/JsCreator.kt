package com.proxy.service.webview.monitor.work.request.creator

import com.proxy.service.webview.monitor.work.base.BaseMonitor

/**
 * @author: cangHX
 * @date: 2026/6/5 17:41
 * @desc:
 */
object JsCreator {

    private const val TYPE_STREAM = "jpg|jpeg|png|gif|bmp|webp|svg|mp4|avi|mov|mkv|webm"
    private const val TYPE_IMAGE = "content-type: image/"
    private const val TYPE_VIDEO = "content-type: video/"

    private const val THREAD_MAIN = "main"
    private const val THREAD_WORK = "work"

    private const val MAX_LENGTH = 1000

    fun getJs(): String {
        return """
            (function() {
                window.__csWebMonitor = window.__csWebMonitor || {};
                var state = window.__csWebMonitor;

                ${createCommonUtilsJs()}
                ${createXhrHookJs()}
                ${createFetchHookJs()}
            })();
        """.trimIndent()
    }

    private fun createCommonUtilsJs(): String {
        return """
                function now() {
                    if (window.performance && typeof window.performance.now === "function") {
                        return window.performance.now();
                    }
                    return Date.now();
                }

                function limitText(value, isBinary) {
                    if (value === undefined || value === null) {
                        return "";
                    }
                    var text = "";
                    try {
                        text = typeof value === "string" ? value : String(value);
                    } catch (e) {
                        text = "";
                    }
                    if (text.length > $MAX_LENGTH) {
                        if (isBinary) {
                            return "二进制内容, 且超出${MAX_LENGTH}字符限制，不打印";
                        }
                        return text.substring(0, $MAX_LENGTH) + " 超出${MAX_LENGTH}字符";
                    }
                    return text;
                }

                function normalizeHeaders(headers) {
                    var result = {};
                    if (!headers) {
                        return result;
                    }
                    try {
                        if (typeof headers.forEach === "function") {
                            headers.forEach(function(value, key) {
                                result[key] = value;
                            });
                        } else if (Array.isArray(headers)) {
                            headers.forEach(function(item) {
                                if (item && item.length >= 2) {
                                    result[item[0]] = item[1];
                                }
                            });
                        } else {
                            result = headers;
                        }
                    } catch (e) {}
                    return result;
                }

                function toHeaderString(headers) {
                    return limitText(JSON.stringify(normalizeHeaders(headers)), false);
                }

                // 生成前端监控内部的单次请求标识，用于关联 hookTiming 与 networkTiming。
                function createRequestId() {
                    state.requestSeq = (state.requestSeq || 0) + 1;
                    return "cs_req_" + Date.now() + "_" + state.requestSeq;
                }

                function toAbsoluteUrl(url) {
                    try {
                        return new URL(url, window.location.href).href;
                    } catch (e) {
                        return String(url || "");
                    }
                }

                function isHttpRequest(url) {
                    try {
                        var absoluteUrl = toAbsoluteUrl(url);
                        var u = new URL(absoluteUrl);
                        return u.protocol === "http:" || u.protocol === "https:";
                    } catch (e) {
                        return false;
                    }
                }

                // 根据请求 URL 和 hook 起始时间匹配 ResourceTiming，同 URL 并发时取 startTime 与 afterTime 最接近的一条。
                function findResourceTiming(url, afterTime) {
                    try {
                        if (!window.performance || typeof window.performance.getEntriesByType !== "function") {
                            return null;
                        }
                        var absoluteUrl = toAbsoluteUrl(url);
                        var entries = window.performance.getEntriesByType("resource") || [];
                        var bestEntry = null;
                        var bestDiff = Infinity;
                        var tolerance = 50;
                        for (var i = 0; i < entries.length; i++) {
                            var entry = entries[i];
                            if (entry.name !== absoluteUrl && entry.name !== url) {
                                continue;
                            }
                            if (afterTime !== undefined && afterTime !== null && entry.startTime < afterTime - tolerance) {
                                continue;
                            }
                            var diff = Math.abs(entry.startTime - (afterTime || 0));
                            if (diff < bestDiff) {
                                bestDiff = diff;
                                bestEntry = entry;
                            }
                        }
                        return bestEntry;
                    } catch (e) {}
                    return null;
                }

                function duration(end, start) {
                    end = end || 0;
                    start = start || 0;
                    var value = end - start;
                    return value > 0 ? value : 0;
                }

                function getPageOrigin() {
                    try {
                        return new URL(window.location.href).origin;
                    } catch (e) {
                        return "";
                    }
                }

                function isCrossOriginRequest(url) {
                    try {
                        var pageOrigin = getPageOrigin();
                        if (!pageOrigin) {
                            return false;
                        }
                        return new URL(toAbsoluteUrl(url)).origin !== pageOrigin;
                    } catch (e) {
                        return false;
                    }
                }

                // 判断 ResourceTiming 细项是否因跨域未配置 Timing-Allow-Origin 而不可信。
                function detectTimingDetailLevel(entry, requestUrl) {
                    if (!entry) {
                        return "unavailable";
                    }
                    var crossOrigin = isCrossOriginRequest(requestUrl);
                    var transferSize = entry.transferSize || 0;
                    var encodedBodySize = entry.encodedBodySize || 0;
                    var decodedBodySize = entry.decodedBodySize || 0;
                    var opaqueSize = transferSize === 0 && encodedBodySize === 0 && decodedBodySize === 0;
                    var brokenBreakdown = (entry.responseStart || 0) === 0 && (entry.responseEnd || 0) > 0;
                    var zeroedPhases = (entry.requestStart || 0) === 0
                        && (entry.domainLookupStart || 0) === 0
                        && (entry.connectStart || 0) === 0;
                    if (crossOrigin && (opaqueSize || brokenBreakdown || zeroedPhases)) {
                        return "restricted";
                    }
                    return "full";
                }

                // 构造浏览器网络层耗时。该数据来自 PerformanceResourceTiming，可能因跨域、缓存、连接复用等缺失或为 0。
                function buildPerformanceInfo(url, afterTime) {
                    var entry = findResourceTiming(url, afterTime);
                    if (!entry) {
                        return {
                            networkTiming: { detailLevel: "unavailable" },
                            size: {
                                transferSize: 0,
                                encodedBodySize: 0,
                                decodedBodySize: 0,
                                fromCache: false
                            },
                            protocol: {
                                nextHopProtocol: "",
                                initiatorType: ""
                            }
                        };
                    }

                    var detailLevel = detectTimingDetailLevel(entry, url);
                    var fromCache = (entry.transferSize || 0) === 0 && (entry.encodedBodySize || 0) > 0;
                    if (detailLevel === "restricted"
                        && (entry.transferSize || 0) === 0
                        && (entry.encodedBodySize || 0) === 0
                        && (entry.decodedBodySize || 0) === 0) {
                        fromCache = false;
                    }

                    return {
                        networkTiming: {
                            detailLevel: detailLevel,
                            startTime: entry.startTime || 0,
                            queueing: duration(entry.requestStart, entry.startTime),
                            redirect: duration(entry.redirectEnd, entry.redirectStart),
                            dns: duration(entry.domainLookupEnd, entry.domainLookupStart),
                            tcp: duration(entry.connectEnd, entry.connectStart),
                            tls: entry.secureConnectionStart > 0 ? duration(entry.connectEnd, entry.secureConnectionStart) : 0,
                            ttfb: duration(entry.responseStart, entry.requestStart),
                            responseDownload: duration(entry.responseEnd, entry.responseStart),
                            total: duration(entry.responseEnd, entry.startTime)
                        },
                        size: {
                            transferSize: entry.transferSize || 0,
                            encodedBodySize: entry.encodedBodySize || 0,
                            decodedBodySize: entry.decodedBodySize || 0,
                            fromCache: fromCache
                        },
                        protocol: {
                            nextHopProtocol: entry.nextHopProtocol || "",
                            initiatorType: entry.initiatorType || ""
                        }
                    };
                }

                function isBinaryUrl(url) {
                    try {
                        return String(url || "").match(/\.($TYPE_STREAM)${'$'}/i) != null;
                    } catch (e) {
                        return false;
                    }
                }

                function isBinaryHeaders(headers) {
                    try {
                        var value = String(headers || "").toLowerCase();
                        return value.indexOf("$TYPE_IMAGE") >= 0 || value.indexOf("$TYPE_VIDEO") >= 0;
                    } catch (e) {
                        return false;
                    }
                }

                function safeReport(log) {
                    ${BaseMonitor.createLog("logMonitorRequest", "log")}
                }

                function safeReportStart(log) {
                    ${BaseMonitor.createLog("logMonitorRequestStart", "log")}
                }

                function buildRequestStartLog(monitor) {
                    return JSON.stringify({
                        requestId: monitor.requestId,
                        requestType: monitor.requestType,
                        pageUrl: window.location.href,
                        requestUrl: monitor.requestUrl,
                        method: monitor.method,
                        startTime: monitor.startTime,
                        phase: "start"
                    });
                }

                function getThreadType() {
                    try {
                        if (typeof window !== "undefined" && typeof window.location !== "undefined") {
                            return "$THREAD_MAIN";
                        }
                    } catch (e) {}
                    return "$THREAD_WORK";
                }
        """.trimIndent()
    }

    private fun createXhrHookJs(): String {
        return """
                (function() {
                    if (state.xhrHooked) {
                        return;
                    }
                    if (!window.XMLHttpRequest || !XMLHttpRequest.prototype) {
                        return;
                    }
                    state.xhrHooked = true;
                    state.originalXHROpen = XMLHttpRequest.prototype.open;
                    state.originalXHRSend = XMLHttpRequest.prototype.send;
                    state.originalXHRSetRequestHeader = XMLHttpRequest.prototype.setRequestHeader;

                    function reportXhrError(xhr, type, message) {
                        var monitor = xhr.__csMonitor;
                        if (!monitor || monitor.reportedError) {
                            return;
                        }
                        monitor.reportedError = true;
                        var endTime = now();
                        var performanceInfo = buildPerformanceInfo(monitor.requestUrl, monitor.sendTime || monitor.openTime);
                        var log = {
                            requestId: monitor.requestId,
                            requestType: monitor.requestType,
                            phase: "end",
                            pageUrl: window.location.href,
                            requestUrl: monitor.requestUrl,
                            method: monitor.method,
                            status: xhr.status || 0,
                            success: false,
                            hookTiming: {
                                xhr: {
                                    openTime: monitor.openTime,
                                    sendTime: monitor.sendTime,
                                    headersReceivedTime: monitor.headersReceivedTime,
                                    loadingTime: monitor.loadingTime,
                                    doneTime: endTime
                                }
                            },
                            networkTiming: performanceInfo.networkTiming,
                            size: performanceInfo.size,
                            protocol: performanceInfo.protocol,
                            content: {
                                threadType: monitor.threadType,
                                requestHeaders: limitText(JSON.stringify(monitor.requestHeaders || {}), false),
                                requestBody: limitText(monitor.requestBody, monitor.isBinaryContent),
                                responseHeaders: "",
                                responseBody: ""
                            },
                            errorInfo: {
                                type: type,
                                message: message
                            }
                        };
                        safeReport(JSON.stringify(log));
                    }

                    XMLHttpRequest.prototype.open = function(method, url) {
                        var absoluteUrl = toAbsoluteUrl(url);
                        if (!isHttpRequest(absoluteUrl)) {
                            return state.originalXHROpen.apply(this, arguments);
                        }
                        this.__csMonitor = {
                            requestId: createRequestId(),
                            requestType: "xhr",
                            method: method,
                            requestUrl: absoluteUrl,
                            threadType: getThreadType(),
                            openTime: now(),
                            sendTime: null,
                            headersReceivedTime: null,
                            loadingTime: null,
                            doneTime: null,
                            requestHeaders: {},
                            requestBody: "",
                            isBinaryContent: isBinaryUrl(url)
                        };
                        if (!this.__csMonitorReadystatechangeHooked) {
                            this.__csMonitorReadystatechangeHooked = true;
                            this.addEventListener("readystatechange", function() {
                                var monitor = this.__csMonitor;
                                if (!monitor) {
                                    return;
                                }
                                if (this.readyState === 2) {
                                    monitor.headersReceivedTime = now();
                                } else if (this.readyState === 3) {
                                    monitor.loadingTime = now();
                                } else if (this.readyState === 4) {
                                    monitor.doneTime = now();
                                    var headers = "";
                                    try {
                                        headers = this.getAllResponseHeaders() || "";
                                    } catch (e) {}
                                    monitor.isBinaryContent = monitor.isBinaryContent || isBinaryHeaders(headers);

                                    var body = "";
                                    try {
                                        body = this.responseText || "";
                                    } catch (e) {
                                        body = "";
                                    }

                                    var performanceInfo = buildPerformanceInfo(monitor.requestUrl, monitor.sendTime || monitor.openTime);
                                    var errorInfo = null;
                                    if (this.status >= 400) {
                                        errorInfo = {
                                            type: "httpError",
                                            message: "HTTP 状态码异常: " + this.status
                                        };
                                    }

                                    var log = {
                                        requestId: monitor.requestId,
                                        requestType: monitor.requestType,
                                        phase: "end",
                                        pageUrl: window.location.href,
                                        requestUrl: monitor.requestUrl,
                                        method: monitor.method,
                                        status: this.status,
                                        success: this.status >= 200 && this.status < 400,
                                        hookTiming: {
                                            xhr: {
                                                openTime: monitor.openTime,
                                                sendTime: monitor.sendTime,
                                                headersReceivedTime: monitor.headersReceivedTime,
                                                loadingTime: monitor.loadingTime,
                                                doneTime: monitor.doneTime
                                            }
                                        },
                                        networkTiming: performanceInfo.networkTiming,
                                        size: performanceInfo.size,
                                        protocol: performanceInfo.protocol,
                                        content: {
                                            threadType: monitor.threadType,
                                            requestHeaders: limitText(JSON.stringify(monitor.requestHeaders || {}), false),
                                            requestBody: limitText(monitor.requestBody, monitor.isBinaryContent),
                                            responseHeaders: limitText(headers, false),
                                            responseBody: limitText(body, monitor.isBinaryContent)
                                        },
                                        errorInfo: errorInfo
                                    };
                                    safeReport(JSON.stringify(log));
                                }
                            });
                            this.addEventListener("error", function() {
                                reportXhrError(this, "xhrError", "XHR 请求失败");
                            });
                            this.addEventListener("timeout", function() {
                                reportXhrError(this, "xhrTimeout", "XHR 请求超时");
                            });
                            this.addEventListener("abort", function() {
                                reportXhrError(this, "xhrAbort", "XHR 请求取消");
                            });
                        }
                        return state.originalXHROpen.apply(this, arguments);
                    };

                    XMLHttpRequest.prototype.setRequestHeader = function(header, value) {
                        if (this.__csMonitor) {
                            this.__csMonitor.requestHeaders[header] = value;
                        }
                        return state.originalXHRSetRequestHeader.apply(this, arguments);
                    };

                    XMLHttpRequest.prototype.send = function(body) {
                        if (this.__csMonitor) {
                            this.__csMonitor.sendTime = now();
                            this.__csMonitor.requestBody = body;
                            this.__csMonitor.startTime = this.__csMonitor.sendTime;
                            safeReportStart(buildRequestStartLog(this.__csMonitor));
                        }
                        return state.originalXHRSend.apply(this, arguments);
                    };
                })();
        """.trimIndent()
    }

    private fun createFetchHookJs(): String {
        return """
                function resolveFetchInput(input, init) {
                    var info = {
                        url: "",
                        method: "GET",
                        headers: {},
                        body: ""
                    };
                    try {
                        if (typeof input === "string") {
                            info.url = input;
                        } else if (input) {
                            info.url = input.url || "";
                            info.method = input.method || info.method;
                            info.headers = normalizeHeaders(input.headers);
                        }
                        if (init) {
                            info.method = init.method || info.method;
                            info.headers = normalizeHeaders(init.headers || info.headers);
                            info.body = init.body || "";
                        }
                        info.url = toAbsoluteUrl(info.url);
                    } catch (e) {}
                    return info;
                }

                (function() {
                    if (state.fetchHooked) {
                        return;
                    }
                    if (typeof window.fetch !== "function") {
                        return;
                    }
                    state.fetchHooked = true;
                    state.originalFetch = window.fetch;

                    window.fetch = function(input, init) {
                        var requestInfo = resolveFetchInput(input, init);
                        if (!isHttpRequest(requestInfo.url)) {
                            return state.originalFetch.apply(this, arguments);
                        }
                        var requestId = createRequestId();
                        var startTime = now();
                        var startMonitor = {
                            requestId: requestId,
                            requestType: "fetch",
                            requestUrl: requestInfo.url,
                            method: requestInfo.method,
                            startTime: startTime
                        };
                        safeReportStart(buildRequestStartLog(startMonitor));
                        return state.originalFetch.apply(this, arguments).then(function(response) {
                            var responseTime = now();
                            var responseHeaders = {};
                            try {
                                responseHeaders = normalizeHeaders(response.headers);
                            } catch (e) {}
                            var clonedResponse = response.clone();
                            var bodyReadStartTime = now();
                            clonedResponse.text().then(function(body) {
                                var bodyReadDoneTime = now();
                                var requestUrl = requestInfo.url || response.url;
                                var performanceInfo = buildPerformanceInfo(requestUrl, startTime);
                                var errorInfo = null;
                                if (response.status >= 400) {
                                    errorInfo = {
                                        type: "httpError",
                                        message: "HTTP 状态码异常: " + response.status
                                    };
                                }
                                var log = {
                                    requestId: requestId,
                                    requestType: "fetch",
                                    phase: "end",
                                    pageUrl: window.location.href,
                                    requestUrl: requestUrl,
                                    method: requestInfo.method,
                                    status: response.status,
                                    success: response.status >= 200 && response.status < 400,
                                    hookTiming: {
                                        fetch: {
                                            fetchCallTime: startTime,
                                            promiseResolvedTime: responseTime,
                                            cloneBodyReadStartTime: bodyReadStartTime,
                                            cloneBodyReadDoneTime: bodyReadDoneTime
                                        }
                                    },
                                    networkTiming: performanceInfo.networkTiming,
                                    size: performanceInfo.size,
                                    protocol: performanceInfo.protocol,
                                    content: {
                                        threadType: getThreadType(),
                                        requestHeaders: toHeaderString(requestInfo.headers),
                                        requestBody: limitText(requestInfo.body, isBinaryUrl(requestUrl)),
                                        responseHeaders: toHeaderString(responseHeaders),
                                        responseBody: limitText(body, isBinaryHeaders(JSON.stringify(responseHeaders)))
                                    },
                                    errorInfo: errorInfo
                                };
                                safeReport(JSON.stringify(log));
                            }).catch(function(error) {
                                var bodyReadDoneTime = now();
                                var requestUrl = requestInfo.url || response.url;
                                var performanceInfo = buildPerformanceInfo(requestUrl, startTime);
                                var log = {
                                    requestId: requestId,
                                    requestType: "fetch",
                                    phase: "end",
                                    pageUrl: window.location.href,
                                    requestUrl: requestUrl,
                                    method: requestInfo.method,
                                    status: response.status,
                                    success: false,
                                    hookTiming: {
                                        fetch: {
                                            fetchCallTime: startTime,
                                            promiseResolvedTime: responseTime,
                                            cloneBodyReadStartTime: bodyReadStartTime,
                                            cloneBodyReadDoneTime: bodyReadDoneTime
                                        }
                                    },
                                    networkTiming: performanceInfo.networkTiming,
                                    size: performanceInfo.size,
                                    protocol: performanceInfo.protocol,
                                    content: {
                                        threadType: getThreadType(),
                                        requestHeaders: toHeaderString(requestInfo.headers),
                                        requestBody: limitText(requestInfo.body, isBinaryUrl(requestUrl)),
                                        responseHeaders: toHeaderString(responseHeaders),
                                        responseBody: ""
                                    },
                                    errorInfo: {
                                        type: "cloneBodyReadFail",
                                        message: String(error)
                                    }
                                };
                                safeReport(JSON.stringify(log));
                            });
                            return response;
                        }).catch(function(error) {
                            var responseTime = now();
                            var performanceInfo = buildPerformanceInfo(requestInfo.url, startTime);
                            var log = {
                                requestId: requestId,
                                requestType: "fetch",
                                phase: "end",
                                pageUrl: window.location.href,
                                requestUrl: requestInfo.url,
                                method: requestInfo.method,
                                status: 0,
                                success: false,
                                hookTiming: {
                                    fetch: {
                                        fetchCallTime: startTime,
                                        promiseRejectedTime: responseTime
                                    }
                                },
                                networkTiming: performanceInfo.networkTiming,
                                size: performanceInfo.size,
                                protocol: performanceInfo.protocol,
                                content: {
                                    threadType: getThreadType(),
                                    requestHeaders: toHeaderString(requestInfo.headers),
                                    requestBody: limitText(requestInfo.body, isBinaryUrl(requestInfo.url)),
                                    responseHeaders: "",
                                    responseBody: ""
                                },
                                errorInfo: {
                                    type: "fetchReject",
                                    message: String(error)
                                }
                            };
                            safeReport(JSON.stringify(log));
                            throw error;
                        });
                    };
                })();
        """.trimIndent()
    }
}