package com.proxy.service.webview.bridge.ds.converter

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.webview.base.listener.WebInterceptCallback
import com.proxy.service.webview.base.listener.WebLoadCallback
import com.proxy.service.webview.base.web.IWeb
import com.proxy.service.webview.base.web.IWebLoader
import com.proxy.service.webview.bridge.ds.bridge.WebBridge
import com.proxy.service.webview.bridge.ds.config.Config

/**
 * @author: cangHX
 * @data: 2024/8/10 15:28
 * @desc:
 */
class WebLoaderConverter(private val loader: IWebLoader) : IWebLoader {

    private val bridge = WebBridge()

    init {
        loader.addJavascriptInterface(Config.BRIDGE_NAME, bridge)
    }

    override fun addJavascriptInterface(any: Any): IWebLoader {
        loader.addJavascriptInterface(Config.BRIDGE_NAME, any)
        bridge.put("", any)
        return this
    }

    override fun addJavascriptInterface(nameSpace: String, any: Any): IWebLoader {
        loader.addJavascriptInterface(nameSpace, any)
        bridge.put(nameSpace, any)
        return this
    }

    override fun load(): IWeb {
        return loadTo(null)
    }

    override fun loadTo(viewGroup: ViewGroup?): IWeb {
        val web = loader.loadTo(viewGroup)
        bridge.setWeb(web)
        return web
    }

    override fun loadUrl(url: String): IWebLoader {
        loader.loadUrl(url)
        return this
    }

    override fun setInterceptCallback(callback: WebInterceptCallback): IWebLoader {
        loader.setInterceptCallback(callback)
        return this
    }

    override fun setLifecycleOwner(owner: LifecycleOwner): IWebLoader {
        loader.setLifecycleOwner(owner)
        return this
    }

    override fun setLoadCallback(callback: WebLoadCallback): IWebLoader {
        loader.setLoadCallback(callback)
        return this
    }
}