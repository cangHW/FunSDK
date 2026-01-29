package com.proxy.service.webview.monitor.work.request

import android.os.Build
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.base.BaseMonitor
import java.lang.StringBuilder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


/**
 * @author: cangHX
 * @data: 2026/1/23 13:35
 * @desc:
 */
object CookieMonitor : BaseMonitor() {

    private const val TAG = "${WebMonitorConstants.TAG}Cookie"

    override fun shouldRun(): Boolean {
        val enableLog = config.isLogCookieEnable()
        val callback = config.getLogCookieCallback()

        return enableLog || callback != null
    }

    override fun getJs(): String {
        val log = createLog("logMonitorCookie", "document.cookie")
        return "javascript:$log;"
    }

    override fun dispatchLog(url: String, log: String) {
        if (config.isLogCookieEnable()) {
            val builder = StringBuilder()
            builder.append("当前页面 ").append(url).append("\n")

            try {
                val cookiePairs: List<String> = log.split("; ")
                for (pair in cookiePairs) {
                    if (!pair.contains("=")) {
                        continue
                    }
                    val keyValue = pair.split("=".toRegex(), limit = 2).toTypedArray()
                    val key = keyValue[0]
                    val value = if (keyValue.size > 1) keyValue[1] else ""

                    val decodedValue = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        URLDecoder.decode(value, StandardCharsets.UTF_8)
                    } else {
                        URLDecoder.decode(value)
                    }

                    builder.append("    $key").append(": ").append(decodedValue).append("\n")
                }
            } catch (_: Throwable) {
                builder.clear()
                builder.append(url).append("\n").append(log)
            }

            CsLogger.tag(TAG).d("Cookies from JS: $builder")
        }

        config.getLogCookieCallback()?.onMonitorCall(url, log)
    }

}