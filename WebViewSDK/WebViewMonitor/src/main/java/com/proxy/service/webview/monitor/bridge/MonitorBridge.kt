package com.proxy.service.webview.monitor.bridge

import android.webkit.JavascriptInterface
import com.proxy.service.webview.monitor.work.base.MonitorFactory

/**
 * @author: cangHX
 * @date: 2026/1/23 14:03
 * @desc:
 */
class MonitorBridge(
    private val factory: MonitorFactory
) {

    @JavascriptInterface
    fun logMonitorCookie(url: String, log: String) {
        factory.getCookieMonitor().dispatchLog(url, log)
    }


    @JavascriptInterface
    fun logMonitorRequestStart(url: String, log: String) {
        factory.getRequestMonitor().dispatchRequestStart(url, log)
    }

    @JavascriptInterface
    fun logMonitorRequest(url: String, log: String) {
        factory.getRequestMonitor().dispatchLog(url, log)
    }


    @JavascriptInterface
    fun logMonitorLoadPage(url: String, log: String) {
        factory.getLoadPageMonitor().dispatchLog(url, log)
    }

    @JavascriptInterface
    fun logMonitorLoadResource(url: String, log: String) {
        factory.getLoadResourceMonitor().dispatchLog(url, log)
    }

}