package com.proxy.service.webview.monitor.work.request

import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.base.BaseMonitor
import com.proxy.service.webview.monitor.work.request.bean.RequestData
import java.lang.StringBuilder
import java.text.DecimalFormat

/**
 * @author: cangHX
 * @date: 2026/1/23 14:07
 * @desc:
 */
object RequestMonitor : BaseMonitor() {

    private const val TAG = "${WebMonitorConstants.TAG}Req"

    private const val TYPE_STREAM = "jpg|jpeg|png|gif|bmp|webp|svg|mp4|avi|mov|mkv|webm"
    private const val TYPE_IMAGE = "content-type: image/"
    private const val TYPE_VIDEO = "content-type: video/"

    private const val THREAD_MAIN = "main"
    private const val THREAD_WORK = "work"

    private const val MAX_LENGTH = 1000

    override fun shouldRun(): Boolean {
        return getConfig().isLogRequestEnable()
    }

    override fun getJs(): String {
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

                // 根据请求 URL 和 hook 起始时间匹配 ResourceTiming，同 URL 多次请求时优先取当前请求之后的最后一条。
                function findResourceTiming(url, afterTime) {
                    try {
                        if (!window.performance || typeof window.performance.getEntriesByType !== "function") {
                            return null;
                        }
                        var absoluteUrl = toAbsoluteUrl(url);
                        var entries = window.performance.getEntriesByType("resource") || [];
                        var target = null;
                        var tolerance = 50;
                        for (var i = 0; i < entries.length; i++) {
                            var entry = entries[i];
                            if (entry.name !== absoluteUrl && entry.name !== url) {
                                continue;
                            }
                            if (afterTime !== undefined && afterTime !== null && entry.startTime < afterTime - tolerance) {
                                continue;
                            }
                            target = entry;
                        }
                        return target;
                    } catch (e) {}
                    return null;
                }

                function duration(end, start) {
                    end = end || 0;
                    start = start || 0;
                    var value = end - start;
                    return value > 0 ? value : 0;
                }

                // 构造浏览器网络层耗时。该数据来自 PerformanceResourceTiming，可能因跨域、缓存、连接复用等缺失或为 0。
                function buildPerformanceInfo(url, afterTime) {
                    var entry = findResourceTiming(url, afterTime);
                    if (!entry) {
                        return {
                            networkTiming: { available: false },
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

                    return {
                        networkTiming: {
                            available: true,
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
                            fromCache: (entry.transferSize || 0) === 0 && (entry.encodedBodySize || 0) > 0
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
                    try {
                        if (
                            window.${WebMonitorConstants.WEB_MONITOR_LOG_BRIDGE_NAME_SPACE} &&
                            typeof window.${WebMonitorConstants.WEB_MONITOR_LOG_BRIDGE_NAME_SPACE}.logMonitorRequest === "function"
                        ) {
                            window.${WebMonitorConstants.WEB_MONITOR_LOG_BRIDGE_NAME_SPACE}.logMonitorRequest(window.location.href, log);
                        }
                    } catch (e) {}
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
                        this.__csMonitor = {
                            requestId: createRequestId(),
                            requestType: "xhr",
                            method: method,
                            requestUrl: toAbsoluteUrl(url),
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
                        var requestId = createRequestId();
                        var startTime = now();
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


    override fun dispatchLog(url: String, log: String) {
        val data = CsJsonUtils.fromJson(log, RequestData::class.java)
        val value: String
        if (data == null) {
            value = log
        } else {
            val builder = StringBuilder()
            val hookTotal = getHookTotalDuration(data)
            val networkTotal = data.networkTiming?.takeIf { it.available }?.total
            val statusText = getStatusText(data)

            builder.append("请求监控 [")
                .append(data.requestType)
                .append("] ")
                .append(data.method)
                .append(" ")
                .append(statusText)
                .append(" 线程=")
                .append(data.content?.threadType ?: "")
                .append(" 调用层=")
                .append(formatMs(hookTotal))
                .append(" 网络层=")
                .append(formatMs(networkTotal))
                .append(" ")
                .append(data.requestUrl)
                .append("\n")

            appendHookTiming(builder, data)
            appendNetworkTiming(builder, data)
            appendMetaInfo(builder, data)
            appendContentInfo(builder, data)
            appendErrorInfo(builder, data)
            value = builder.toString()
        }

        CsLogger.tag(TAG).d("Request: $value")

        getConfig().getLogRequestCallback()?.onMonitorCall(url, log)
    }

    private fun appendHookTiming(builder: StringBuilder, data: RequestData) {
        val hookTiming = data.hookTiming ?: return
        val xhr = hookTiming.xhr
        if (xhr != null) {
            // open等待：从 xhr.open(...) 到 xhr.send(...) 的耗时，通常包含设置 header、准备 body 等同步业务准备时间。
            val openWait = duration(xhr.sendTime, xhr.openTime)
            // send到完成：从 xhr.send(...) 到 readyState=4 的耗时，包含请求发出、响应接收和 XHR 完成状态触发。
            // 注意：它不是纯网络层耗时，纯网络层耗时以 networkTiming 为准。
            val sendToDone = duration(xhr.doneTime, xhr.sendTime)
            builder.append("  调用层: open等待=")
                .append(formatMs(openWait))
                .append(", send到完成=")
                .append(formatMs(sendToDone))
                .append("\n")
            return
        }

        val fetch = hookTiming.fetch
        if (fetch != null) {
            // fetch响应：从业务调用 fetch(...) 到 fetch Promise resolve，表示拿到 Response 对象的耗时。
            // 注意：它不代表完整响应体读取完成，也不包含业务 response.json() 解析耗时。
            val fetchResolve = duration(fetch.promiseResolvedTime, fetch.fetchCallTime)
            // 响应体读取：Monitor 读取 response.clone().text() 的耗时。
            // 注意：这是监控侧读取克隆响应体，不代表业务侧解析耗时。
            val bodyRead = duration(fetch.cloneBodyReadDoneTime, fetch.cloneBodyReadStartTime)
            builder.append("  调用层: fetch响应=")
                .append(formatMs(fetchResolve))
                .append(", 响应体读取=")
                .append(formatMs(bodyRead))
                .append("\n")
        }
    }

    private fun appendNetworkTiming(builder: StringBuilder, data: RequestData) {
        val networkTiming = data.networkTiming ?: return
        if (!networkTiming.available) {
            return
        }
        builder.append("  网络层: 总=")
            .append(formatMs(networkTiming.total))
            .append(", 排队/准备=")
            .append(formatMs(networkTiming.queueing))
        // TTFB：responseStart - requestStart，代表从请求发送到首字节返回。
        builder.append(", TTFB=")
            .append(formatMs(networkTiming.ttfb))
            .append(", 下载=")
            .append(formatMs(networkTiming.responseDownload))
            .append(", DNS=")
            .append(formatMs(networkTiming.dns))
            .append(", TCP=")
            .append(formatMs(networkTiming.tcp))
            .append(", TLS=")
            .append(formatMs(networkTiming.tls))
            .append("\n")
    }

    private fun appendMetaInfo(builder: StringBuilder, data: RequestData) {
        val size = data.size
        val protocol = data.protocol
        if (size == null && protocol == null) {
            return
        }
        builder.append("  信息: 传输=")
            .append(formatBytes(size?.transferSize))
            .append(", 缓存=")
            .append(size?.fromCache ?: false)
            .append(", 协议=")
            .append(protocol?.nextHopProtocol.orEmpty())
            .append("\n")
    }

    private fun appendContentInfo(builder: StringBuilder, data: RequestData) {
        val content = data.content ?: return
        appendIfNotBlank(builder, "  请求Header: ", content.requestHeaders)
        appendIfNotBlank(builder, "  请求Body: ", content.requestBody)
        appendIfNotBlank(builder, "  响应Header: ", content.responseHeaders)
        appendIfNotBlank(builder, "  响应Body: ", content.responseBody)
    }

    private fun appendErrorInfo(builder: StringBuilder, data: RequestData) {
        val errorInfo = data.errorInfo ?: return
        if (errorInfo.type.isNullOrBlank() && errorInfo.message.isNullOrBlank()) {
            return
        }
        builder.append("  异常: ")
            .append(errorInfo.type)
            .append(", ")
            .append(errorInfo.message)
            .append("\n")
    }

    private fun appendIfNotBlank(builder: StringBuilder, prefix: String, value: String?) {
        if (value.isNullOrBlank()) {
            return
        }
        builder.append(prefix)
            .append(value.replace("\n", ""))
            .append("\n")
    }

    private fun getHookTotalDuration(data: RequestData): Float? {
        val hookTiming = data.hookTiming ?: return null
        hookTiming.xhr?.let {
            return duration(it.doneTime, it.sendTime)
        }
        hookTiming.fetch?.let {
            return duration(it.promiseResolvedTime ?: it.promiseRejectedTime, it.fetchCallTime)
        }
        return null
    }

    private fun getStatusText(data: RequestData): String {
        val status = data.status
        if (status != null && status > 0) {
            return status.toString()
        }
        return if (data.success) {
            "成功"
        } else {
            "失败"
        }
    }

    private fun duration(end: Float?, start: Float?): Float? {
        if (end == null || start == null) {
            return null
        }
        val value = end - start
        return if (value >= 0) {
            value
        } else {
            null
        }
    }

    private fun formatMs(value: Float?): String {
        if (value == null) {
            return "N/A"
        }
        try {
            return "${DecimalFormat("#.##").format(value)}ms"
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return "error"
    }

    private fun formatBytes(value: Float?): String {
        val size = value ?: 0f
        if (size < 1024f) {
            return "${DecimalFormat("#.##").format(size)}B"
        }
        return "${DecimalFormat("#.##").format(size / 1024f)}KB"
    }
}