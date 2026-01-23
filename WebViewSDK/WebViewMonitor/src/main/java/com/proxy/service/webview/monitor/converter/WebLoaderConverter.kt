package com.proxy.service.webview.monitor.converter

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.webview.base.listener.WebInterceptCallback
import com.proxy.service.webview.base.listener.WebLifecycleCallback
import com.proxy.service.webview.base.listener.WebLoadCallback
import com.proxy.service.webview.base.web.IWeb
import com.proxy.service.webview.base.web.IWebLoader
import com.proxy.service.webview.monitor.bridge.MonitorBridge
import com.proxy.service.webview.monitor.constant.WebMonitorConstants

/**
 * @author: cangHX
 * @data: 2026/1/23 13:30
 * @desc:
 */
class WebLoaderConverter(private val loader: IWebLoader) : IWebLoader {

    private val webLoadCallbackImpl = WebLoadCallbackImpl()

    init {
        loader.setWebLoadCallback(webLoadCallbackImpl)
        loader.addJavascriptInterface(
            WebMonitorConstants.WEB_MONITOR_LOG_BRIDGE_NAME_SPACE,
            MonitorBridge()
        )
    }

    override fun loadUrl(url: String): IWebLoader {
        loader.loadUrl(url)
        return this
    }

    override fun addJavascriptInterface(any: Any): IWebLoader {
        loader.addJavascriptInterface(any)
        return this
    }

    override fun addJavascriptInterface(nameSpace: String, any: Any): IWebLoader {
        loader.addJavascriptInterface(nameSpace, any)
        return this
    }

    override fun setLifecycleOwner(owner: LifecycleOwner): IWebLoader {
        loader.setLifecycleOwner(owner)
        return this
    }

    override fun setWebLoadCallback(callback: WebLoadCallback): IWebLoader {
        webLoadCallbackImpl.setRealWebLoadCallback(callback)
        return this
    }

    override fun setWebInterceptCallback(callback: WebInterceptCallback): IWebLoader {
        loader.setWebInterceptCallback(callback)
        return this
    }

    override fun setWebLifecycleCallback(callback: WebLifecycleCallback): IWebLoader {
        loader.setWebLifecycleCallback(callback)
        return this
    }

    override fun createTo(viewGroup: ViewGroup?): IWeb {
        val web = loader.createTo(viewGroup)
        webLoadCallbackImpl.setWeb(web)
        return web
    }

    override fun create(): IWeb {
        val web = loader.create()
        webLoadCallbackImpl.setWeb(web)
        return web
    }
}