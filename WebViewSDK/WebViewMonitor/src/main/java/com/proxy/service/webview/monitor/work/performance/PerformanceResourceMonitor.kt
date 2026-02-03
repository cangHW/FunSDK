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
                "       font: []," +
                "       video: []," +
                "       audio: []," +
                "       iframe: []," +
                "       other: []" +
                "   };" +
                "   for (var i = 0; i < entries.length; i++) {" +
                "       var entry = entries[i];" +
                "       var type = entry.initiatorType;" +
                "       var name = entry.name;" +

                checkResIsImg() +
                checkResIsScript() +
                checkResIsCss() +
                checkResIsFont() +
                checkResIsVideo() +
                checkResIsAudio() +
                checkResIsIframe() +

                "       var performanceData = {" +
                "           startTime: entry.startTime," +
                "           duration: entry.duration," +
                "           transferSize: entry.transferSize," +
                "           domainLookupStart: entry.domainLookupStart," +
                "           domainLookupEnd: entry.domainLookupEnd," +
                "           connectStart: entry.connectStart," +
                "           connectEnd: entry.connectEnd," +
                "           requestStart: entry.requestStart," +
                "           responseStart: entry.responseStart," +
                "           responseEnd: entry.responseEnd," +
                "           name: entry.name," +
                "           nextHopProtocol: entry.nextHopProtocol," +
                "           fromCache: entry.transferSize === 0 && entry.encodedBodySize > 0" +
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
                builder.append(LINE_1).append("字体资源: ").append("\n")
                data.font?.forEach {
                    builder.append(LINE_2).append(it).append("\n")
                }
                CsLogger.tag(TAG).d(builder.toString())

                builder.clear()
                builder.append(LINE_1).append("视频资源: ").append("\n")
                data.video?.forEach {
                    builder.append(LINE_2).append(it).append("\n")
                }
                CsLogger.tag(TAG).d(builder.toString())

                builder.clear()
                builder.append(LINE_1).append("音频资源: ").append("\n")
                data.audio?.forEach {
                    builder.append(LINE_2).append(it).append("\n")
                }
                CsLogger.tag(TAG).d(builder.toString())

                builder.clear()
                builder.append(LINE_1).append("HTML 资源: ").append("\n")
                data.iframe?.forEach {
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


    private fun checkResIsImg(): String {
        val builder = StringBuilder()

        builder.append("if (")
            .append("name.endsWith(\".jpg\")")
            .append("||").append("name.endsWith(\".jpeg\")")
            .append("||").append("name.endsWith(\".png\")")
            .append("||").append("name.endsWith(\".gif\")")
            .append("||").append("name.endsWith(\".webp\")")
            .append("||").append("name.endsWith(\".svg\")")
            .append("){type = \"img\"}")

        return builder.toString()
    }

    private fun checkResIsScript(): String {
        val builder = StringBuilder()

        builder.append("if (")
            .append("name.endsWith(\".js\")")
            .append("){type = \"script\"}")

        return builder.toString()
    }

    private fun checkResIsCss(): String {
        val builder = StringBuilder()

        builder.append("if (")
            .append("name.endsWith(\".css\")")
            .append("){type = \"css\"}")

        return builder.toString()
    }

    private fun checkResIsFont(): String {
        val builder = StringBuilder()

        builder.append("if (")
            .append("name.endsWith(\".woff\")")
            .append("||").append("name.endsWith(\".woff2\")")
            .append("||").append("name.endsWith(\".ttf\")")
            .append("||").append("name.endsWith(\".otf\")")
            .append("||").append("name.endsWith(\".eot\")")
            .append("){type = \"font\"}")

        return builder.toString()
    }

    private fun checkResIsVideo(): String {
        val builder = StringBuilder()

        builder.append("if (")
            .append("name.endsWith(\".mp4\")")
            .append("||").append("name.endsWith(\".webm\")")
            .append("||").append("name.endsWith(\".ogg\")")
            .append("){type = \"video\"}")

        return builder.toString()
    }

    private fun checkResIsAudio(): String {
        val builder = StringBuilder()

        builder.append("if (")
            .append("name.endsWith(\".mp3\")")
            .append("||").append("name.endsWith(\".wav\")")
            .append("||").append("name.endsWith(\".aac\")")
            .append("){type = \"audio\"}")

        return builder.toString()
    }

    private fun checkResIsIframe(): String {
        val builder = StringBuilder()

        builder.append("if (")
            .append("name.endsWith(\".html\")")
            .append("||").append("name.endsWith(\".htm\")")
            .append("){type = \"iframe\"}")

        return builder.toString()
    }
}