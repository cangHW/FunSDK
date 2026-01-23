package com.proxy.service.webview.monitor.bridge

import android.webkit.JavascriptInterface
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.monitor.CsWebMonitor

/**
 * @author: cangHX
 * @data: 2026/1/23 14:03
 * @desc:
 */
class MonitorBridge {

    @JavascriptInterface
    fun logCookies(tag: String, cookies: String) {
        val config = CsWebMonitor.getMonitorConfig()
        if (config.isLogCookieEnable()) {
            CsLogger.tag(tag).i("Cookies from JS: $cookies")
        }

        config.getLogCookieCallback()?.onReceiveValue(cookies)
    }

    @JavascriptInterface
    fun logAjaxRequest(tag: String, log: String) {
        val config = CsWebMonitor.getMonitorConfig()
        if (config.isLogAjaxRequestEnable()) {
            CsLogger.tag(tag).i("Ajax Request: $log")
        }

        config.getLogAjaxRequestCallback()?.onReceiveValue(log)
    }

}