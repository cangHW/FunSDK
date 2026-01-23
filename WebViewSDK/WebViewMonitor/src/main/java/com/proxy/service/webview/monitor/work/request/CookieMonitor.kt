package com.proxy.service.webview.monitor.work.request

import com.proxy.service.webview.monitor.config.MonitorConfig
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.base.BaseMonitor

/**
 * @author: cangHX
 * @data: 2026/1/23 13:35
 * @desc:
 */
object CookieMonitor : BaseMonitor() {

    private const val TAG = "${WebMonitorConstants.TAG}Cookie"

    override fun shouldRun(config: MonitorConfig): Boolean {
        val enableLog = config.isLogCookieEnable()
        val callback = config.getLogCookieCallback()

        return enableLog || callback != null
    }

    override fun getJs(nameSpace: String): String {
        return "javascript:window.${nameSpace}.logCookies($TAG, document.cookie);"
    }

}