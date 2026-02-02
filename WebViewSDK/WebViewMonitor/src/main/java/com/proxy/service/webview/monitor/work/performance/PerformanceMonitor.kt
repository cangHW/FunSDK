package com.proxy.service.webview.monitor.work.performance

import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.base.BaseMonitor
import java.lang.StringBuilder
import java.text.DecimalFormat

/**
 * @author: cangHX
 * @data: 2026/1/25 19:50
 * @desc:
 */
object PerformanceMonitor : BaseMonitor() {

    private const val TAG = "${WebMonitorConstants.TAG}Perform"

    override fun shouldRun(): Boolean {
        val enableLog = config.isLogLoadPageTimeEnable()
        val callback = config.getLogLoadPageTimeCallback()

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
                createLog("logMonitorPerformPage", "JSON.stringify(performanceData)") +
                "})()"
    }

    override fun dispatchLog(url: String, log: String) {
        if (config.isLogLoadPageTimeEnable()) {
            val data = CsJsonUtils.fromJson(log, PerformanceData::class.java)
            val value: String
            if (data == null) {
                value = log
            } else {
                val format = DecimalFormat("#.##")
                val builder = StringBuilder()
                builder.append("当前页面 ").append(url).append("\n")

                builder.append("    DNS 查询耗时: ")
                    .append(format.format(data.domainLookupEnd - data.domainLookupStart))
                    .append("ms")
                    .append("\n")
                builder.append("    TCP 连接耗时: ")
                    .append(format.format(data.connectEnd - data.connectStart))
                    .append("ms")
                    .append("\n")

                builder.append("    服务器响应耗时: ")
                    .append(format.format(data.responseStart - data.requestStart))
                    .append("ms")
                    .append("\n")
                builder.append("    数据传输耗时: ")
                    .append(format.format(data.responseEnd - data.responseStart))
                    .append("ms")
                    .append("\n")

                builder.append("    DOM 解析耗时: ")
                    .append(format.format(data.domInteractive - data.domLoading))
                    .append("ms")
                    .append("\n")
//                builder.append("    DOMContentLoaded 事件的 JavaScript 的 时间: ")
//                    .append(data.domContentLoadedEventStart - data.domContentLoadedEventEnd)
//                    .append("ms")
//                    .append("\n")
                builder.append("    DOMContentLoaded 耗时: ")
                    .append(format.format(data.domContentLoadedEventEnd - data.navigationStart))
                    .append("ms")
                    .append("\n")

                builder.append("    页面加载总耗时: ")
                if (data.loadEventEnd > data.navigationStart) {
                    builder.append(format.format(data.loadEventEnd - data.navigationStart))
                        .append("ms")
                } else {
                    builder.append("时间统计异常")
                }

                value = builder.toString()
            }
            CsLogger.tag(TAG).d("Performance Data: $value")
        }

        config.getLogLoadPageTimeCallback()?.onMonitorCall(url, log)
    }

}