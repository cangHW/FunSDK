package com.proxy.service.webview.bridge.ds.converter

import com.proxy.service.webview.base.WebService
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.web.IWebLoader
import com.proxy.service.webview.base.web.callback.ValueCallback
import com.proxy.service.webview.bridge.ds.bridge.GlobalBridgeManager

/**
 * @author: cangHX
 * @data: 2024/8/10 15:26
 * @desc:
 */
class WebServiceConverter(private val service: WebService) : WebService {

    override fun addGlobalJavascriptInterface(any: Any) {
        service.addGlobalJavascriptInterface(any)
        GlobalBridgeManager.put("", any)
    }

    override fun addGlobalJavascriptInterface(nameSpace: String, any: Any) {
        service.addGlobalJavascriptInterface(nameSpace, any)
        GlobalBridgeManager.put(nameSpace, any)
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

    override fun createWebLoader(config: WebConfig): IWebLoader {
        return WebLoaderConverter(service.createWebLoader(config))
    }

    override fun flushGlobalCookie() {
        service.flushGlobalCookie()
    }
}