package com.proxy.service.webview.info.web.factory

import android.webkit.CookieManager
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.listener.WebInterceptCallback
import com.proxy.service.webview.base.listener.WebLifecycleCallback
import com.proxy.service.webview.base.listener.WebLoadCallback
import com.proxy.service.webview.info.view.WebViewImpl

/**
 * @author: cangHX
 * @data: 2024/10/24 14:42
 * @desc:
 */
class WebFactory {

    companion object {
        fun create(): WebFactory {
            return WebFactory()
        }
    }

    private var webLoadCallback: WebLoadCallback? = null
    private var webInterceptCallback: WebInterceptCallback? = null
    private var webLifecycleCallback: WebLifecycleCallback? = null

    fun setWebLoadCallback(webLoadCallback: WebLoadCallback?): WebFactory {
        this.webLoadCallback = webLoadCallback
        return this
    }

    fun setWebInterceptCallback(webInterceptCallback: WebInterceptCallback?): WebFactory {
        this.webInterceptCallback = webInterceptCallback
        return this
    }

    fun setWebLifecycleCallback(webLifecycleCallback: WebLifecycleCallback?): WebFactory {
        this.webLifecycleCallback = webLifecycleCallback
        return this
    }

    fun createWeb(config: WebConfig): WebViewImpl {
        val webView = WebViewImpl(CsContextManager.getApplication())
        webView.setWebLifecycleCallback(webLifecycleCallback)
        initWeb(config, webView)
        return webView
    }

    private fun initWeb(config: WebConfig, webView: WebViewImpl) {
        webView.settings.domStorageEnabled = true
        CookieManager.getInstance()
            .setAcceptThirdPartyCookies(webView, config.isAcceptThirdPartyCookies())
        webView.settings.mixedContentMode = config.getMixedContentMode().mode
        webView.settings.javaScriptEnabled = config.isJavaScriptEnabled()
        webView.settings.cacheMode = config.getCacheMode().mode
        webView.settings.useWideViewPort = config.isUseWideViewPort()
        webView.settings.loadWithOverviewMode = config.isLoadWithOverviewMode()
        if (config.isUserAgentAppend()) {
            webView.settings.userAgentString =
                "${webView.settings.userAgentString} ${config.getUserAgent()}"
        } else {
            webView.settings.userAgentString = config.getUserAgent()
        }
        webView.settings.savePassword = config.isSavePassword()
        webView.settings.allowFileAccess = config.isAllowFileAccess()
        webView.settings.allowContentAccess = config.isAllowContentAccess()
        webView.settings.allowFileAccessFromFileURLs = config.isAllowFileAccessFromFileURLs()
        webView.settings.allowUniversalAccessFromFileURLs =
            config.isAllowUniversalAccessFromFileURLs()

        webView.isVerticalScrollBarEnabled = config.isVerticalScrollBarEnabled()
        webView.isHorizontalScrollBarEnabled = config.isHorizontalScrollBarEnabled()
        webView.isHorizontalFadingEdgeEnabled = config.isHorizontalFadingEdgeEnabled()

        webView.webViewClient = CommonWebViewClientImpl(
            config.getWebViewAssetLoader(),
            webLoadCallback,
            webInterceptCallback
        )
        webView.webChromeClient = CommonWebChromeClientImpl(
            webLoadCallback,
            webInterceptCallback
        )
    }
}