package com.proxy.service.webview.bridge.ds.converter

import com.proxy.service.webview.base.WebService
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.web.IWebLoader
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

    override fun createWebLoader(config: WebConfig): IWebLoader {
        return WebLoaderConverter(service.createWebLoader(config))
    }

    override fun flushGlobalCookie() {
        service.flushGlobalCookie()
    }

    override fun setGlobalCookie(url: String, value: String) {
        service.setGlobalCookie(url, value)
    }
}