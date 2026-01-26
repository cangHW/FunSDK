package com.proxy.service.webview.monitor.work.base

import com.proxy.service.webview.base.web.IWeb
import com.proxy.service.webview.monitor.CsWebMonitor
import com.proxy.service.webview.monitor.config.MonitorConfig
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.performance.PerformanceMonitor
import com.proxy.service.webview.monitor.work.request.AjaxRequestMonitor
import com.proxy.service.webview.monitor.work.request.CookieMonitor

/**
 * @author: cangHX
 * @data: 2026/1/23 13:52
 * @desc:
 */
abstract class BaseMonitor {

    protected val config: MonitorConfig = CsWebMonitor.getMonitorConfig()

    /**
     * 执行监控
     * */
    fun runMonitor(web: IWeb?) {
        if (!shouldRun()) {
            return
        }

        web?.evaluateJavascript(getJs(), null)
    }

    /**
     * 是否应该执行
     * */
    protected abstract fun shouldRun(): Boolean

    /**
     * 获取执行脚本
     * */
    protected abstract fun getJs(): String

    /**
     * 处理日志
     * */
    abstract fun dispatchLog(url: String, log: String)


    protected fun createLog(method: String, content: String): String {
        return "window.${WebMonitorConstants.WEB_MONITOR_LOG_BRIDGE_NAME_SPACE}.${method}(window.location.href, $content);"
    }

}