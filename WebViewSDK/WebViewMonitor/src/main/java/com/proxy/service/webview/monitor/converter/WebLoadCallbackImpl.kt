package com.proxy.service.webview.monitor.converter

import com.proxy.service.webview.base.web.IWeb
import com.proxy.service.webview.monitor.work.performance.PerformanceMonitor
import com.proxy.service.webview.monitor.work.performance.PerformanceResourceMonitor
import com.proxy.service.webview.monitor.work.request.AjaxRequestMonitor
import com.proxy.service.webview.monitor.work.request.CookieMonitor

/**
 * @author: cangHX
 * @data: 2026/1/23 13:40
 * @desc:
 */
class WebLoadCallbackImpl : AbstractWebLoadCallback() {

    private var web: IWeb? = null

    fun setWeb(web: IWeb) {
        this.web = web
    }

    override fun onPageStarted(url: String) {
        super.onPageStarted(url)

        CookieMonitor.runMonitor(web)
        AjaxRequestMonitor.runMonitor(web)
    }

    override fun onPageAllResourceFinished(url: String) {
        super.onPageAllResourceFinished(url)

        PerformanceMonitor.runMonitor(web)
        PerformanceResourceMonitor.runMonitor(web)
    }
}