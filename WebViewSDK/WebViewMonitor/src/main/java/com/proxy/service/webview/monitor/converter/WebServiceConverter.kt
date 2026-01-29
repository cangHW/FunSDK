package com.proxy.service.webview.monitor.converter

import com.proxy.service.webview.base.WebService
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.web.IWebLoader
import com.proxy.service.webview.base.web.callback.ValueCallback

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

    override fun setAcceptGlobalCookie(accept: Boolean) {
        service.setAcceptGlobalCookie(accept)
    }

    override fun getGlobalCookie(url: String): String {
        return service.getGlobalCookie(url)
    }

    override fun setGlobalCookie(url: String, value: String, callback: ValueCallback<Boolean>?) {
        service.setGlobalCookie(url, value, callback)
    }

    override fun removeGlobalCookie(url: String, callback: ValueCallback<Boolean>?) {
        service.removeGlobalCookie(url, callback)
    }

    override fun removeAllGlobalCookies(callback: ValueCallback<Boolean>?) {
        service.removeAllGlobalCookies(callback)
    }

    override fun flushGlobalCookie() {
        service.flushGlobalCookie()
    }
}