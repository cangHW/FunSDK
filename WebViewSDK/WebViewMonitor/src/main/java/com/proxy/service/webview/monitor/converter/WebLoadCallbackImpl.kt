package com.proxy.service.webview.monitor.converter

import com.proxy.service.webview.base.listener.WebLoadCallback
import com.proxy.service.webview.base.web.IWeb
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

    override fun onPageFinished(url: String) {
        super.onPageFinished(url)

        CookieMonitor.doMonitor(web)
        AjaxRequestMonitor.doMonitor(web)

    }

}