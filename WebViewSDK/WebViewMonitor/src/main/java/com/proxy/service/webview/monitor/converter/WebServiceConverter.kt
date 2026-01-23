package com.proxy.service.webview.monitor.converter

import com.proxy.service.webview.base.WebService
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.web.IWebLoader

/**
 * @author: cangHX
 * @data: 2026/1/23 13:29
 * @desc:
 */
class WebServiceConverter(private val service: WebService): WebService {

    override fun createWebLoader(config: WebConfig): IWebLoader {
        return WebLoaderConverter(service.createWebLoader(config))
    }

    override fun addGlobalJavascriptInterface(any: Any) {
        service.addGlobalJavascriptInterface(any)
    }

    override fun addGlobalJavascriptInterface(nameSpace: String, any: Any) {
        service.addGlobalJavascriptInterface(nameSpace, any)
    }

    override fun setGlobalCookie(url: String, value: String) {
        service.setGlobalCookie(url, value)
    }

    override fun flushGlobalCookie() {
        service.flushGlobalCookie()
    }
}