package com.proxy.service.webview.monitor.converter

import com.proxy.service.webview.base.web.IWeb
import com.proxy.service.webview.monitor.work.base.MonitorFactory

/**
 * @author: cangHX
 * @date: 2026/1/23 13:40
 * @desc:
 */
class WebLoadCallbackImpl(
    private val factory: MonitorFactory
) : AbstractWebLoadCallback() {

    private var web: IWeb? = null

    fun setWeb(web: IWeb) {
        this.web = web
    }

    override fun onPageStarted(url: String) {
        super.onPageStarted(url)
        factory.getRequestMonitor().onPageStarted()

        factory.getCookieMonitor().runMonitor(web)
        factory.getRequestMonitor().runMonitor(web)
    }

    override fun onPageAllResourceFinished(url: String) {
        super.onPageAllResourceFinished(url)

        factory.getLoadPageMonitor().runMonitor(web)
        factory.getLoadResourceMonitor().runMonitor(web)
    }
}