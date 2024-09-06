package com.proxy.service.webview.info

import android.webkit.CookieManager
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.webview.base.WebService
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.web.IWebLoader
import com.proxy.service.webview.info.config.JavaScriptManager
import com.proxy.service.webview.info.web.WebLoaderImpl
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/8/1 10:23
 * @desc:
 */
@CloudApiService(serviceTag = "service/web_view")
class WebServiceImpl : WebService {

    private val isAcceptCookie = AtomicBoolean(false)

    override fun createWebLoader(config: WebConfig): IWebLoader {
        return WebLoaderImpl(config)
    }

    override fun addGlobalJavascriptInterface(any: Any) {
        JavaScriptManager.addGlobalJavascriptInterface(any)
    }

    override fun addGlobalJavascriptInterface(nameSpace: String, any: Any) {
        JavaScriptManager.addGlobalJavascriptInterface(nameSpace, any)
    }

    override fun setGlobalCookie(url: String, value: String) {
        if (isAcceptCookie.compareAndSet(false, true)) {
            CookieManager.getInstance().setAcceptCookie(true)
        }
        CookieManager.getInstance().setCookie(url, value)
    }

    override fun flushGlobalCookie() {
        CookieManager.getInstance().flush()
    }

}