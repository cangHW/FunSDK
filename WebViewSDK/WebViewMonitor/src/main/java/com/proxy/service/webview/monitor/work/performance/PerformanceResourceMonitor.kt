package com.proxy.service.webview.monitor.work.performance

import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.base.BaseMonitor
import com.proxy.service.webview.monitor.work.performance.bean.PerformanceResourceData
import java.lang.StringBuilder

/**
 * @author: cangHX
 * @date: 2026/1/30 17:04
 * @desc:
 */
object PerformanceResourceMonitor : BaseMonitor() {

    private const val TAG = "${WebMonitorConstants.TAG}PerformRes"

    override fun shouldRun(): Boolean {
        return getConfig().isLogLoadPageResourceTimeEnable()
    }

    override fun getJs(): String {
        return """
            javascript:(function() {
                if (!window.performance || typeof window.performance.getEntriesByType !== "function") {
                    return;
                }

                ${createCommonUtilsJs()}
                ${createResourceTypeJs()}
                ${createResourceInfoJs()}
                ${createResourceCollectJs()}
            })()
        """.trimIndent()
    }

    private fun createCommonUtilsJs(): String {
        return """
                function duration(end, start) {
                    end = end || 0;
                    start = start || 0;
                    var value = end - start;
                    return value > 0 ? value : 0;
                }

                function normalizeName(name) {
                    try {
                        return String(name || "").split("#")[0].split("?")[0].toLowerCase();
                    } catch (e) {
                        return "";
                    }
                }

                function hasSuffix(name, suffixes) {
                    for (var i = 0; i < suffixes.length; i++) {
                        if (name.endsWith(suffixes[i])) {
                            return true;
                        }
                    }
                    return false;
                }
        """.trimIndent()
    }

    private fun createResourceTypeJs(): String {
        return """
                function getResourceType(entry) {
                    var initiatorType = entry.initiatorType || "";
                    var name = normalizeName(entry.name);
                    if (initiatorType === "img" || initiatorType === "image" || hasSuffix(name, [".jpg", ".jpeg", ".png", ".gif", ".webp", ".svg", ".bmp"])) {
                        return "img";
                    }
                    if (initiatorType === "script" || hasSuffix(name, [".js"])) {
                        return "script";
                    }
                    if (initiatorType === "css" || initiatorType === "link" || hasSuffix(name, [".css"])) {
                        return "css";
                    }
                    if (initiatorType === "font" || hasSuffix(name, [".woff", ".woff2", ".ttf", ".otf", ".eot"])) {
                        return "font";
                    }
                    if (initiatorType === "video" || hasSuffix(name, [".mp4", ".webm", ".ogg", ".mov", ".mkv"])) {
                        return "video";
                    }
                    if (initiatorType === "audio" || hasSuffix(name, [".mp3", ".wav", ".aac", ".m4a"])) {
                        return "audio";
                    }
                    if (initiatorType === "iframe" || hasSuffix(name, [".html", ".htm"])) {
                        return "iframe";
                    }
                    return "other";
                }
        """.trimIndent()
    }

    private fun createResourceInfoJs(): String {
        return """
                function createResourceInfo(entry, type) {
                    return {
                        type: type,
                        name: entry.name || "",
                        startTime: entry.startTime || 0,
                        total: duration(entry.responseEnd, entry.startTime),
                        queueing: duration(entry.requestStart, entry.startTime),
                        dns: duration(entry.domainLookupEnd, entry.domainLookupStart),
                        tcp: duration(entry.connectEnd, entry.connectStart),
                        tls: entry.secureConnectionStart > 0 ? duration(entry.connectEnd, entry.secureConnectionStart) : 0,
                        ttfb: duration(entry.responseStart, entry.requestStart),
                        download: duration(entry.responseEnd, entry.responseStart),
                        transferSize: entry.transferSize || 0,
                        encodedBodySize: entry.encodedBodySize || 0,
                        decodedBodySize: entry.decodedBodySize || 0,
                        fromCache: (entry.transferSize || 0) === 0 && (entry.encodedBodySize || 0) > 0,
                        nextHopProtocol: entry.nextHopProtocol || ""
                    };
                }
        """.trimIndent()
    }

    private fun createResourceCollectJs(): String {
        return """
                var entries = performance.getEntriesByType("resource") || [];
                var groupedData = {
                    summary: {
                        totalCount: 0,
                        cacheCount: 0,
                        transferSize: 0,
                        maxDuration: 0,
                        slowResources: []
                    },
                    img: [],
                    script: [],
                    css: [],
                    font: [],
                    video: [],
                    audio: [],
                    iframe: [],
                    other: []
                };
                var allResources = [];

                for (var i = 0; i < entries.length; i++) {
                    var entry = entries[i];
                    if (entry.initiatorType === "xmlhttprequest" || entry.initiatorType === "fetch") {
                        continue;
                    }

                    var type = getResourceType(entry);
                    var resourceInfo = createResourceInfo(entry, type);
                    groupedData[type].push(resourceInfo);
                    allResources.push(resourceInfo);

                    groupedData.summary.totalCount += 1;
                    groupedData.summary.transferSize += resourceInfo.transferSize;
                    if (resourceInfo.fromCache) {
                        groupedData.summary.cacheCount += 1;
                    }
                    if (resourceInfo.total > groupedData.summary.maxDuration) {
                        groupedData.summary.maxDuration = resourceInfo.total;
                    }
                }

                allResources.sort(function(a, b) {
                    return b.total - a.total;
                });
                groupedData.summary.slowResources = allResources.slice(0, 5);

                ${createLog("logMonitorPerformPageRes", "JSON.stringify(groupedData)")}
        """.trimIndent()
    }

    override fun dispatchLog(url: String, log: String) {
        val data = CsJsonUtils.fromJson(log, PerformanceResourceData::class.java)
        if (data == null) {
            CsLogger.tag(TAG).d("Performance Data: $log")
        } else {
            val builder = StringBuilder()
            val summary = data.summary
            builder.append("资源监控 当前页面=")
                .append(url)
                .append("\n")
            builder.append("  总览: 资源=")
                .append(summary?.totalCount ?: 0)
                .append(", 缓存=")
                .append(summary?.cacheCount ?: 0)
                .append(", 传输=")
                .append(formatBytes(summary?.transferSize))
                .append(", 最慢=")
                .append(formatMs(summary?.maxDuration))
                .append("\n")
            builder.append("  分组: 图片=")
                .append(data.img?.size ?: 0)
                .append(", 脚本=")
                .append(data.script?.size ?: 0)
                .append(", 样式=")
                .append(data.css?.size ?: 0)
                .append(", 字体=")
                .append(data.font?.size ?: 0)
                .append(", 视频=")
                .append(data.video?.size ?: 0)
                .append(", 音频=")
                .append(data.audio?.size ?: 0)
                .append(", HTML=")
                .append(data.iframe?.size ?: 0)
                .append(", 其他=")
                .append(data.other?.size ?: 0)
                .append("\n")
            appendSlowResources(builder, summary?.slowResources)
            CsLogger.tag(TAG).d("Performance Data: $builder")
        }

        getConfig().getLogLoadPageResourceTimeCallback()?.onMonitorCall(url, log)
    }

    private fun appendSlowResources(
        builder: StringBuilder,
        slowResources: ArrayList<com.proxy.service.webview.monitor.work.performance.bean.PerformanceResourceDataInfo>?
    ) {
        if (slowResources.isNullOrEmpty()) {
            return
        }
        builder.append("  慢资源Top5:").append("\n")
        slowResources.forEachIndexed { index, item ->
            builder.append("    ")
                .append(index + 1)
                .append(". [")
                .append(item.type)
                .append("] ")
                .append(formatMs(item.total))
                .append(", ")
                .append(formatBytes(item.transferSize))
                .append(", 缓存=")
                .append(item.fromCache)
                .append(", 协议=")
                .append(item.nextHopProtocol)
                .append(", ")
                .append(item.name)
                .append("\n")
        }
    }

    private fun formatMs(value: Float?): String {
        val cost = value ?: 0f
        return "${java.text.DecimalFormat("#.##").format(cost)}ms"
    }

    private fun formatBytes(value: Long?): String {
        val size = value ?: 0L
        if (size < 1024L) {
            return "${size}B"
        }
        return "${java.text.DecimalFormat("#.##").format(size / 1024f)}KB"
    }
}