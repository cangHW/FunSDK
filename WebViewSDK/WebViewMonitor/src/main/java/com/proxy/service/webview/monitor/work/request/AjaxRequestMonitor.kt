package com.proxy.service.webview.monitor.work.request

import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.base.BaseMonitor
import java.lang.StringBuilder
import java.text.DecimalFormat

/**
 * @author: cangHX
 * @data: 2026/1/23 14:07
 * @desc:
 */
object AjaxRequestMonitor : BaseMonitor() {

    private const val TAG = "${WebMonitorConstants.TAG}AjaxReq"

    private const val TYPE_STREAM = "jpg|jpeg|png|gif|bmp|webp|svg|mp4|avi|mov|mkv|webm"
    private const val TYPE_IMAGE = "content-type: image/"
    private const val TYPE_VIDEO = "content-type: video/"

    private const val MAX_LENGTH = 1000

    override fun shouldRun(): Boolean {
        val enableLog = config.isLogAjaxRequestEnable()
        val callback = config.getLogAjaxRequestCallback()

        return enableLog || callback != null
    }

    override fun getJs(): String {
        val js = "var originalOpen = XMLHttpRequest.prototype.open;" +
                "XMLHttpRequest.prototype.open = function(method, url) {" +
                "    this._url = url;" +
                "    this._startTime = performance.now();" +
                "    this._isBinaryContent = false;" +
                "    if (this._url.match(/\\.($TYPE_STREAM)\$/i)) {" +
                "        this._isBinaryContent = true;" +
                "    };" +
                "    this.addEventListener('readystatechange', function() {" +
                "        if (this.readyState == 4) {" +
                "            var endTime = performance.now();" +
                "            var duration = endTime - this._startTime;" +

                "            const entries = performance.getEntriesByName(this._url);" +
                "            var pDuration = {};" +
                "            if (entries.length > 0) {" +
                "                const entry = entries[0];" +
                "                pDuration[startTime] = entry.startTime;" +
                "                pDuration[domainLookupStart] = entry.domainLookupStart;" +
                "                pDuration[domainLookupEnd] = entry.domainLookupEnd;" +
                "                pDuration[connectStart] = entry.connectStart;" +
                "                pDuration[connectEnd] = entry.connectEnd;" +
                "                pDuration[requestStart] = entry.requestStart;" +
                "                pDuration[responseStart] = entry.responseStart;" +
                "                pDuration[responseEnd] = entry.responseEnd;" +
                "            };" +

                "            var requestHeaders = JSON.stringify(this._requestHeaders || {});" +
                "            if (requestHeaders.length > $MAX_LENGTH) {" +
                "                requestHeaders = requestHeaders.substring(0, $MAX_LENGTH) + \" 超出${MAX_LENGTH}字符\";" +
                "            };" +

                "            var headers = this.getAllResponseHeaders();" +
                "            if (headers.toLowerCase().includes(\"$TYPE_IMAGE\") || headers.toLowerCase().includes(\"$TYPE_VIDEO\")) {" +
                "               this._isBinaryContent = true;" +
                "            };" +
                "            if (headers.length > $MAX_LENGTH) {" +
                "                headers = headers.substring(0, $MAX_LENGTH) + \" 超出${MAX_LENGTH}字符\";" +
                "            };" +

                "            var requestBody = this._requestBody || '';" +
                "            if (requestBody.length > $MAX_LENGTH) {" +
                "                if (this._isBinaryContent) {" +
                "                    requestBody = \"请求二进制文件参数, 超出${MAX_LENGTH}字符限制，不打印\";" +
                "                } else {;" +
                "                    requestBody = requestBody.substring(0, $MAX_LENGTH) + \" 超出${MAX_LENGTH}字符\";" +
                "                };" +
                "            };" +

                "            var body = this.responseText;" +
                "            if (body.length > $MAX_LENGTH) {" +
                "                if (this._isBinaryContent) {" +
                "                    body = \"二进制内容, 且超出${MAX_LENGTH}字符限制，不打印\";" +
                "                } else {;" +
                "                    body = body.substring(0, $MAX_LENGTH) + \" 超出${MAX_LENGTH}字符\";" +
                "                };" +
                "            };" +

                "            var log = {" +
                "                method: method," +
                "                duration: duration," +
                "                pDuration: pDuration," +
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
                val format = DecimalFormat("#.##")

                val builder = StringBuilder()
                builder.append("当前页面 ").append(url).append("\n")

                builder.append("    请求方式: ")
                    .append(data.method)
                    .append("\n")

                builder.append("    请求耗时: ")
                    .append(format.format(data.duration))
                    .append("ms")
                    .append("\n")

                val pDuration = data.pDuration
                if (pDuration != null) {
                    builder.append("    网络层耗时: ")
                        .append(format.format(pDuration.responseEnd - pDuration.startTime))
                        .append("ms.")
                        .append(" (PS: ")
                        .append("1、DNS解析=")
                        .append(format.format(pDuration.domainLookupEnd - pDuration.domainLookupStart))
                        .append("ms. ")
                        .append("2、TCP连接=")
                        .append(format.format(pDuration.connectEnd - pDuration.connectStart))
                        .append("ms. ")
                        .append("3、请求发送=")
                        .append(format.format(pDuration.requestStart - pDuration.connectEnd))
                        .append("ms. ")
                        .append("4、响应接收=")
                        .append(format.format(pDuration.responseEnd - pDuration.responseStart))
                        .append("ms")
                        .append(")")
                }

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