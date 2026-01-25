package com.proxy.service.webview.monitor.bridge

import android.webkit.JavascriptInterface
import com.proxy.service.webview.monitor.work.base.BaseMonitor

/**
 * @author: cangHX
 * @data: 2026/1/23 14:03
 * @desc:
 */
class MonitorBridge {

    @JavascriptInterface
    fun logMonitorData(tag: String, log: String){
        BaseMonitor.dispatchLog(tag, log)
    }

}