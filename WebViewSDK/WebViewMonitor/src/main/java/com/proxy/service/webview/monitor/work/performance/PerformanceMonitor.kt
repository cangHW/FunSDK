package com.proxy.service.webview.monitor.work.performance

import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.base.BaseMonitor
import java.lang.StringBuilder

/**
 * @author: cangHX
 * @data: 2026/1/25 19:50
 * @desc:
 */
object PerformanceMonitor : BaseMonitor() {

    private const val TAG = "${WebMonitorConstants.TAG}Perform"

    override fun shouldRun(): Boolean {
        val enableLog = config.isLogLoadTimeEnable()
        val callback = config.getLogLoadTimeCallback()

        return enableLog || callback != null
    }

    override fun getJs(): String {
        return "javascript:(function() {" +
                "   var timing = performance.timing;" +
                "   var performanceData = {" +
                "       navigationStart: timing.navigationStart," +
                "       domainLookupStart: timing.domainLookupStart," +
                "       domainLookupEnd: timing.domainLookupEnd," +
                "       connectStart: timing.connectStart," +
                "       connectEnd: timing.connectEnd," +
                "       requestStart: timing.requestStart," +
                "       responseStart: timing.responseStart," +
                "       responseEnd: timing.responseEnd," +
                "       domLoading: timing.domLoading," +
                "       domInteractive: timing.domInteractive," +
                "       domContentLoadedEventStart: timing.domContentLoadedEventStart," +
                "       domContentLoadedEventEnd: timing.domContentLoadedEventEnd," +
                "       loadEventStart: timing.loadEventStart," +
                "       loadEventEnd: timing.loadEventEnd" +
                "   };" +
                createLog("logMonitorPerformance", "JSON.stringify(performanceData)") +
                "})()"
    }

    override fun dispatchLog(url: String, log: String) {
        if (config.isLogLoadTimeEnable()) {
            val data = CsJsonUtils.fromJson(log, PerformanceData::class.java)
            val value: String
            if (data == null) {
                value = log
            } else {
                val builder = StringBuilder()
                builder.append(url).append("\n")

                builder.append("    DNS 查询时间: ")
                    .append(data.domainLookupEnd - data.domainLookupStart)
                    .append("ms")
                    .append("\n")
                builder.append("    TCP 连接时间: ")
                    .append(data.connectEnd - data.connectStart)
                    .append("ms")
                    .append("\n")

                builder.append("    服务器响应时间: ")
                    .append(data.responseStart - data.requestStart)
                    .append("ms")
                    .append("\n")
                builder.append("    数据传输时间: ")
                    .append(data.responseEnd - data.responseStart)
                    .append("ms")
                    .append("\n")

                builder.append("    DOM 解析时间: ")
                    .append(data.domInteractive - data.domLoading)
                    .append("ms")
                    .append("\n")
//                builder.append("    DOMContentLoaded 事件的 JavaScript 的 时间: ")
//                    .append(data.domContentLoadedEventStart - data.domContentLoadedEventEnd)
//                    .append("ms")
//                    .append("\n")
                builder.append("    DOMContentLoaded 时间: ")
                    .append(data.domContentLoadedEventEnd - data.navigationStart)
                    .append("ms")
                    .append("\n")

                builder.append("    页面加载总时间: ")
                    .append(data.loadEventEnd - data.navigationStart)
                    .append("ms")

                value = builder.toString()
            }
            CsLogger.tag(TAG).d("Performance Data: $value")
        }

        config.getLogLoadTimeCallback()?.onMonitorCall(url, log)
    }

}