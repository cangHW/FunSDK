package com.proxy.service.webview.monitor.work.request

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.base.BaseMonitor

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
        val log = createLog(TAG, "document.cookie")
        return "javascript:$log;"
    }

    override fun dispatchLog(tag: String, log: String) {
        if (tag != TAG){
            return
        }

        if (config.isLogCookieEnable()) {
            CsLogger.tag(tag).d("Cookies from JS: $log")
        }

        config.getLogCookieCallback()?.onReceiveValue(log)
    }

}