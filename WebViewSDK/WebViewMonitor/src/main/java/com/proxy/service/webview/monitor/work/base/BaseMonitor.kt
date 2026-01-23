package com.proxy.service.webview.monitor.work.base

import com.proxy.service.webview.base.web.IWeb
import com.proxy.service.webview.monitor.CsWebMonitor
import com.proxy.service.webview.monitor.config.MonitorConfig
import com.proxy.service.webview.monitor.constant.WebMonitorConstants

/**
 * @author: cangHX
 * @data: 2026/1/23 13:52
 * @desc:
 */
abstract class BaseMonitor {

    /**
     * 执行监控
     * */
    fun doMonitor(web: IWeb?) {
        if (!shouldRun(CsWebMonitor.getMonitorConfig())) {
            return
        }

        web?.evaluateJavascript(getJs(WebMonitorConstants.WEB_MONITOR_LOG_BRIDGE_NAME_SPACE), null)
    }

    protected abstract fun shouldRun(config: MonitorConfig): Boolean

    protected abstract fun getJs(nameSpace: String): String

}