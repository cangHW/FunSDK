package com.proxy.service.webview.monitor.work.request

import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.base.BaseMonitor
import java.lang.StringBuilder

/**
 * @author: cangHX
 * @data: 2026/1/23 14:07
 * @desc:
 */
object AjaxRequestMonitor : BaseMonitor() {

    private const val TAG = "${WebMonitorConstants.TAG}AjaxReq"

    override fun shouldRun(): Boolean {
        val enableLog = config.isLogAjaxRequestEnable()
        val callback = config.getLogAjaxRequestCallback()

        return enableLog || callback != null
    }

    override fun getJs(): String {
        val js = "var originalOpen = XMLHttpRequest.prototype.open;" +
                "XMLHttpRequest.prototype.open = function(method, url) {" +
                "    this._startTime = Date.now();" +
                "    this.addEventListener('readystatechange', function() {" +
                "        if (this.readyState == 4) {" +
                "            var endTime = Date.now();" +
                "            var duration = endTime - this._startTime;" +
                "            var headers = this.getAllResponseHeaders();" +
                "            var body = this.responseText;" +
                "            var requestHeaders = JSON.stringify(this._requestHeaders || {});" +
                "            var requestBody = this._requestBody || '';" +
                "            var log = {" +
                "                method: method," +
                "                duration: duration," +
                "                url: url," +
                "                requestHeaders: requestHeaders," +
                "                requestBody: requestBody," +
                "                responseHeaders: headers," +
                "                responseBody: body" +
                "            };" +
                createLog("logMonitorAjaxRequest", "JSON.stringify(log)") +
                "        }" +
                "    });" +
                "    this._requestHeaders = {};" +
                "    this._requestBody = '';" +
                "    var originalSetRequestHeader = this.setRequestHeader;" +
                "    this.setRequestHeader = function(header, value) {" +
                "        this._requestHeaders[header] = value;" +
                "        originalSetRequestHeader.call(this, header, value);" +
                "    };" +
                "    var originalSend = this.send;" +
                "    this.send = function(body) {" +
                "        this._requestBody = body;" +
                "        originalSend.call(this, body);" +
                "    };" +
                "    originalOpen.apply(this, arguments);" +
                "};"

        return js
    }

    override fun dispatchLog(url: String, log: String) {
        if (config.isLogAjaxRequestEnable()) {
            val data = CsJsonUtils.fromJson(log, AjaxRequestData::class.java)
            val value: String
            if (data == null) {
                value = log
            } else {
                val builder = StringBuilder()
                builder.append(url).append("\n")

                builder.append("    请求方式: ")
                    .append(data.method)
                    .append("\n")

                builder.append("    请求耗时: ")
                    .append(data.duration)
                    .append("ms")
                    .append("\n")

                builder.append("    请求 url: ")
                    .append(data.url)
                    .append("\n")

                builder.append("    请求 header: ")
                    .append(data.requestHeaders?.replace("\n", ""))
                    .append("\n")

                builder.append("    请求 body: ")
                    .append(data.requestBody)
                    .append("\n")

                builder.append("    返回 header: ")
                    .append(data.responseHeaders?.replace("\n", ""))
                    .append("\n")

                builder.append("    返回 body: ")
                    .append(data.responseBody)
                    .append("\n")

                value = builder.toString()
            }

            CsLogger.tag(TAG).d("Ajax Request: $value")
        }

        config.getLogAjaxRequestCallback()?.onMonitorCall(url, log)
    }
}