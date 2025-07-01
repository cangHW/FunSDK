package com.proxy.service.webview.info.web

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.constants.WebViewConstants
import com.proxy.service.webview.base.listener.WebInterceptCallback
import com.proxy.service.webview.base.listener.WebLifecycleCallback
import com.proxy.service.webview.base.listener.WebLoadCallback
import com.proxy.service.webview.base.web.IWeb
import com.proxy.service.webview.base.web.IWebLoader
import com.proxy.service.webview.info.config.JavaScriptManager
import com.proxy.service.webview.info.utils.WebUtils
import com.proxy.service.webview.info.view.WebViewImpl
import com.proxy.service.webview.info.view.group.IFactory
import com.proxy.service.webview.info.web.factory.WebFactory
import com.proxy.service.webview.info.web.script.ScriptFactory

/**
 * @author: cangHX
 * @data: 2024/8/1 15:01
 * @desc:
 */
class WebLoaderImpl(private val config: WebConfig) : IWebLoader {

    private val tag = "${WebViewConstants.LOG_TAG_START}Loader"

    private var url: String = ""
    private var lifecycleOwner: LifecycleOwner? = null
    private var webLoadCallback: WebLoadCallback? = null
    private var webInterceptCallback: WebInterceptCallback? = null
    private var webLifecycleCallback: WebLifecycleCallback? = null
    private val cache = HashSet<String>()
    private val javascriptInterfaceMap = HashMap<String, HashMap<Class<*>, Any>?>()

    override fun loadUrl(url: String): IWebLoader {
        this.url = url
        return this
    }

    override fun addJavascriptInterface(any: Any): IWebLoader {
        addJavascriptInterface(JavaScriptManager.GLOBAL_BRIDGE_NAME, any)
        return this
    }

    override fun addJavascriptInterface(nameSpace: String, any: Any): IWebLoader {
        val cacheKey = "${nameSpace}_${any.javaClass.name.replace(".", "_")}"
        if (cache.contains(cacheKey)) {
            CsLogger.tag(tag).d("重复加载，any = $any")
            return this
        }
        cache.add(cacheKey)
        WebUtils.addJavascriptInterfaceToMap(nameSpace, any, javascriptInterfaceMap,
            error = {
                CsLogger.tag(tag).e(it, "加载 JavascriptInterface 失败，any = $any")
            },
            success = {
                CsLogger.tag(tag).d("加载 JavascriptInterface 成功，any = $any")
            }
        )
        return this
    }

    override fun setLifecycleOwner(owner: LifecycleOwner): IWebLoader {
        this.lifecycleOwner = owner
        return this
    }

    override fun setWebLoadCallback(callback: WebLoadCallback): IWebLoader {
        this.webLoadCallback = callback
        return this
    }

    override fun setWebInterceptCallback(callback: WebInterceptCallback): IWebLoader {
        this.webInterceptCallback = callback
        return this
    }

    override fun setWebLifecycleCallback(callback: WebLifecycleCallback): IWebLoader {
        this.webLifecycleCallback = callback
        return this
    }

    override fun loadTo(viewGroup: ViewGroup?): IWeb {
        if (viewGroup != null) {
            return WebImpl(createWebView(viewGroup), lifecycleOwner)
        }
        return load()
    }

    override fun load(): IWeb {
        return WebImpl(createWebView(null), lifecycleOwner)
    }

    @SuppressLint("JavascriptInterface")
    private fun createWebView(viewGroup: ViewGroup?): WebViewImpl {
        val webView = WebFactory.create()
            .setWebLoadCallback(webLoadCallback)
            .setWebInterceptCallback(webInterceptCallback)
            .setWebLifecycleCallback(webLifecycleCallback)
            .createWeb(config)

        WebUtils.merge(JavaScriptManager.getNameMethods(), javascriptInterfaceMap).forEach {
            ScriptFactory.getJavaScript(it.value)?.let { bridge ->
                try {
                    webView.addJavascriptInterface(bridge, it.key)
                } catch (throwable: Throwable) {
                    CsLogger.tag(tag).e(throwable)
                }
            }
        }

        viewGroup?.let {
            IFactory.of(it, webView)
        }

        if (url.isNotEmpty() && url.isNotBlank()) {
            webView.loadUrl(url)
        }

        return webView
    }
}