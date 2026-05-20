package com.proxy.service.webview.monitor.work.performance

import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.base.BaseMonitor
import com.proxy.service.webview.monitor.work.performance.bean.PerformancePageData
import com.proxy.service.webview.monitor.work.performance.bean.PerformancePageDomAndLoadTiming
import com.proxy.service.webview.monitor.work.performance.bean.PerformancePageNavigationTiming
import java.lang.StringBuilder
import java.text.DecimalFormat

/**
 * @author: cangHX
 * @date: 2026/1/25 19:50
 * @desc: 页面导航性能监控
 */
object PerformancePageMonitor : BaseMonitor() {

    private const val TAG = "${WebMonitorConstants.TAG}PerPage"

    override fun shouldRun(): Boolean {
        return getConfig().isLogLoadPageTimeEnable()
    }

    override fun getJs(): String {
        return """
            javascript:(function() {
                if (!window.performance || typeof window.performance.getEntriesByType !== "function") {
                    return;
                }

                ${createCommonUtilsJs()}
                ${createPageTimingJs()}
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
        """.trimIndent()
    }

    private fun createPageTimingJs(): String {
        return """
                var navigation = window.performance.getEntriesByType("navigation")[0];
                if (!navigation) {
                    var unavailableLog = {
                        pageUrl: window.location.href,
                        navigationTiming: { available: false },
                        domAndLoadTiming: null
                    };
                    ${createLog("logMonitorPerformPage", "JSON.stringify(unavailableLog)")}
                    return;
                }

                var navigationTiming = {
                    available: true,
                    startTime: navigation.startTime || 0,
                    queueing: duration(navigation.requestStart, navigation.startTime),
                    redirect: duration(navigation.redirectEnd, navigation.redirectStart),
                    dns: duration(navigation.domainLookupEnd, navigation.domainLookupStart),
                    tcp: duration(navigation.connectEnd, navigation.connectStart),
                    tls: navigation.secureConnectionStart > 0
                        ? duration(navigation.connectEnd, navigation.secureConnectionStart) : 0,
                    ttfb: duration(navigation.responseStart, navigation.requestStart),
                    responseDownload: duration(navigation.responseEnd, navigation.responseStart),
                    total: duration(navigation.responseEnd, navigation.startTime),
                    nextHopProtocol: navigation.nextHopProtocol || ""
                };

                var domAndLoadTiming = {
                    domParse: duration(navigation.domInteractive, navigation.responseEnd),
                    domContentLoadedEvent: duration(navigation.domContentLoadedEventEnd, navigation.domContentLoadedEventStart),
                    loadEvent: duration(navigation.loadEventEnd, navigation.loadEventStart),
                    domInteractiveTotal: duration(navigation.domInteractive, navigation.startTime),
                    loadEventTotal: duration(navigation.loadEventEnd, navigation.startTime)
                };

                var log = {
                    pageUrl: window.location.href,
                    navigationTiming: navigationTiming,
                    domAndLoadTiming: domAndLoadTiming
                };
                ${createLog("logMonitorPerformPage", "JSON.stringify(log)")}
        """.trimIndent()
    }

    override fun dispatchLog(url: String, log: String) {
        val data = CsJsonUtils.fromJson(log, PerformancePageData::class.java)
        val value: String
        if (data == null) {
            value = log
        } else {
            val builder = StringBuilder()
            builder.append("页面加载性能 当前页面: ")
                .append(data.pageUrl.orEmpty().ifBlank { url })
                .append("\n")

            val domLoadTiming = data.domAndLoadTiming
            builder.append("  总览: 总加载=")
                .append(formatMs(domLoadTiming?.loadEventTotal))
                .append(", 可交互=")
                .append(formatMs(domLoadTiming?.domInteractiveTotal))
                .append("\n")

            appendNavigationTiming(builder, data.navigationTiming)
            appendDomAndLoadTiming(builder, domLoadTiming)

            value = builder.toString()
        }

        CsLogger.tag(TAG).d(value)
        getConfig().getLogLoadPageTimeCallback()?.onMonitorCall(url, log)
    }

    private fun appendNavigationTiming(
        builder: StringBuilder,
        navigationTiming: PerformancePageNavigationTiming?
    ) {
        if (navigationTiming == null || !navigationTiming.available) {
            builder.append("  网络层: 不可用\n")
            return
        }
        builder.append("  网络层: 总=")
            .append(formatMs(navigationTiming.total))
            .append(", 排队/准备=")
            .append(formatMs(navigationTiming.queueing))
            .append(", TTFB=")
            .append(formatMs(navigationTiming.ttfb))
            .append(", 下载=")
            .append(formatMs(navigationTiming.responseDownload))
            .append(", DNS=")
            .append(formatMs(navigationTiming.dns))
            .append(", TCP=")
            .append(formatMs(navigationTiming.tcp))
            .append(", TLS=")
            .append(formatMs(navigationTiming.tls))
            .append(", 协议=")
            .append(navigationTiming.nextHopProtocol.orEmpty())
            .append("\n")
    }

    private fun appendDomAndLoadTiming(
        builder: StringBuilder,
        domLoadTiming: PerformancePageDomAndLoadTiming?
    ) {
        if (domLoadTiming == null) {
            builder.append("  DOM/事件: 不可用\n")
            return
        }
        builder.append("  DOM/事件: 解析=")
            .append(formatMs(domLoadTiming.domParse))
            .append(", DCL事件=")
            .append(formatMs(domLoadTiming.domContentLoadedEvent))
            .append(", Load事件=")
            .append(formatMs(domLoadTiming.loadEvent))
            .append("\n")
    }

    private fun formatMs(value: Float?): String {
        val cost = value ?: 0f
        return "${DecimalFormat("#.##").format(cost)}ms"
    }
}
