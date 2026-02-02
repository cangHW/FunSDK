package com.proxy.service.webview.monitor.work.performance

import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.base.BaseMonitor
import java.lang.StringBuilder

/**
 * @author: cangHX
 * @data: 2026/1/30 17:04
 * @desc:
 */
object PerformanceResourceMonitor : BaseMonitor() {

    private const val TAG = "${WebMonitorConstants.TAG}PerformRes"

    private const val LINE_1 = "    "
    private const val LINE_2 = "        "

    override fun shouldRun(): Boolean {
        val enableLog = config.isLogLoadPageResourceTimeEnable()
        val callback = config.getLogLoadPageResourceTimeCallback()

        return enableLog || callback != null
    }

    override fun getJs(): String {
        return "javascript:(function() {" +
                "   var entries = performance.getEntries();" +
                "   var groupedData = {" +
                "       img: []," +
                "       script: []," +
                "       css: []," +
                "       xmlhttprequest: []," +
                "       other: []" +
                "   };" +
                "   for (var i = 0; i < entries.length; i++) {" +
                "       var type = entries[i].initiatorType;" +

                "       var performanceData = {" +
                "           startTime: entries[i].startTime," +
                "           duration: entries[i].duration," +
                "           transferSize: entries[i].transferSize," +
                "           domainLookupStart: entries[i].domainLookupStart," +
                "           domainLookupEnd: entries[i].domainLookupEnd," +
                "           connectStart: entries[i].connectStart," +
                "           connectEnd: entries[i].connectEnd," +
                "           requestStart: entries[i].requestStart," +
                "           responseStart: entries[i].responseStart," +
                "           responseEnd: entries[i].responseEnd," +
                "           name: entries[i].name," +
                "           nextHopProtocol: entries[i].nextHopProtocol," +
                "           fromCache: entries[i].transferSize === 0 && entries[i].encodedBodySize > 0" +
                "       };" +

                "       if (groupedData[type]) {" +
                "           groupedData[type].push(performanceData);" +
                "       } else {" +
                "           groupedData.other.push(performanceData);" +
                "       }" +
                "   }" +
                createLog("logMonitorPerformPageRes", "JSON.stringify(groupedData)") +
                "})()"
    }

    override fun dispatchLog(url: String, log: String) {
        if (config.isLogLoadPageResourceTimeEnable()) {
            val data = CsJsonUtils.fromJson(log, PerformanceResourceData::class.java)
            if (data == null) {
                CsLogger.tag(TAG).d("Performance Data: $log")
            } else {
                val builder = StringBuilder()
                builder.append("当前页面 ").append(url).append("\n")

                builder.append(LINE_1).append("图片资源: ").append("\n")
                data.img?.forEach {
                    builder.append(LINE_2).append(it).append("\n")
                }
                CsLogger.tag(TAG).d("Performance Data: $builder")

                builder.clear()
                builder.append(LINE_1).append("脚本资源: ").append("\n")
                data.script?.forEach {
                    builder.append(LINE_2).append(it).append("\n")
                }
                CsLogger.tag(TAG).d(builder.toString())

                builder.clear()
                builder.append(LINE_1).append("样式资源: ").append("\n")
                data.css?.forEach {
                    builder.append(LINE_2).append(it).append("\n")
                }
                CsLogger.tag(TAG).d(builder.toString())

                builder.clear()
                builder.append(LINE_1).append("请求: ").append("\n")
                data.xmlhttprequest?.forEach {
                    builder.append(LINE_2).append(it).append("\n")
                }
                CsLogger.tag(TAG).d(builder.toString())

                builder.clear()
                builder.append(LINE_1).append("其他资源: ").append("\n")
                data.other?.forEach {
                    builder.append(LINE_2).append(it).append("\n")
                }
                CsLogger.tag(TAG).d(builder.toString())
            }
        }

        config.getLogLoadPageResourceTimeCallback()?.onMonitorCall(url, log)
    }
}