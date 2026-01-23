package com.proxy.service.webview.monitor.work.request

import com.proxy.service.webview.monitor.config.MonitorConfig
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.base.BaseMonitor

/**
 * @author: cangHX
 * @data: 2026/1/23 14:07
 * @desc:
 */
object AjaxRequestMonitor: BaseMonitor() {

    private const val TAG = "${WebMonitorConstants.TAG}AjaxRequest"

    override fun shouldRun(config: MonitorConfig): Boolean {
        val enableLog = config.isLogAjaxRequestEnable()
        val callback = config.getLogAjaxRequestCallback()

        return enableLog || callback != null
    }

    override fun getJs(nameSpace: String): String {
        val js = "var originalOpen = XMLHttpRequest.prototype.open;" +
                "XMLHttpRequest.prototype.open = function(method, url) {" +
                "    this.addEventListener('readystatechange', function() {" +
                "        if (this.readyState == 4) {" +
                "            var headers = this.getAllResponseHeaders();" +
                "            var body = this.responseText;" +
                "            var requestHeaders = JSON.stringify(this._requestHeaders || {});" +
                "            var requestBody = this._requestBody || '';" +
                "            var log = {" +
                "                method: method," +
                "                url: url," +
                "                requestHeaders: requestHeaders," +
                "                requestBody: requestBody," +
                "                responseHeaders: headers," +
                "                responseBody: body" +
                "            };" +
                "            $nameSpace.logAjaxRequest($TAG, JSON.stringify(log));" +
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
}