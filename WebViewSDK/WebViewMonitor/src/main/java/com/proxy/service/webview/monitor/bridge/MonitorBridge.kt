package com.proxy.service.webview.monitor.bridge

import android.webkit.JavascriptInterface
import com.proxy.service.webview.monitor.work.performance.PerformanceMonitor
import com.proxy.service.webview.monitor.work.performance.PerformanceResourceMonitor
import com.proxy.service.webview.monitor.work.request.AjaxRequestMonitor
import com.proxy.service.webview.monitor.work.request.CookieMonitor

/**
 * @author: cangHX
 * @data: 2026/1/23 14:03
 * @desc:
 */
class MonitorBridge {

    @JavascriptInterface
    fun logMonitorCookie(url: String, log: String) {
        CookieMonitor.dispatchLog(url, log)
    }

    @JavascriptInterface
    fun logMonitorAjaxRequest(url: String, log: String) {
        AjaxRequestMonitor.dispatchLog(url, log)
    }

    @JavascriptInterface
    fun logMonitorPerformPage(url: String, log: String) {
        PerformanceMonitor.dispatchLog(url, log)
    }

    @JavascriptInterface
    fun logMonitorPerformPageRes(url: String, log: String) {
        PerformanceResourceMonitor.dispatchLog(url, log)
    }

}