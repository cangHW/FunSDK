package com.proxy.service.webview.monitor.converter

import com.proxy.service.webview.base.web.IWeb
import com.proxy.service.webview.monitor.work.base.BaseMonitor

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

    private var isPageStarted = true

    override fun onPageStarted(url: String) {
        super.onPageStarted(url)
        isPageStarted = true
    }

    override fun onPageFinished(url: String) {
        super.onPageFinished(url)

        if (isPageStarted) {
            isPageStarted = false

            BaseMonitor.runMonitor(web)
        }
    }

}