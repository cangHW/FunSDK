package com.proxy.service.webview.info.web.factory

import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.URLUtil
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.constants.WebViewConstants
import com.proxy.service.webview.base.listener.WebDownloadListener
import com.proxy.service.webview.base.listener.WebInterceptCallback
import com.proxy.service.webview.base.listener.WebLifecycleCallback
import com.proxy.service.webview.base.listener.WebLoadCallback
import com.proxy.service.webview.base.web.IWebLoader
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

    private val tag = "${WebViewConstants.LOG_TAG_START}WebFactory"


    private var webLoadCallback: WebLoadCallback? = null
    private var webInterceptCallback: WebInterceptCallback? = null
    private var webDownloadListener: WebDownloadListener? = null
    private var webLifecycleCallback: WebLifecycleCallback? = null

    fun setWebLoadCallback(webLoadCallback: WebLoadCallback?): WebFactory {
        this.webLoadCallback = webLoadCallback
        return this
    }

    fun setWebDownloadCallback(webDownloadListener: WebDownloadListener?): WebFactory {
        this.webDownloadListener = webDownloadListener
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
        val webViewSetting = webView.settings

        webViewSetting.setSupportMultipleWindows(config.isSupportMultipleWindows())

        webViewSetting.textZoom = config.getTextZoom()

        webViewSetting.setSupportZoom(config.isSupportZoom())
        webViewSetting.builtInZoomControls = config.isBuiltInZoomControls()
        webViewSetting.displayZoomControls = config.isDisplayZoomControls()

        webViewSetting.domStorageEnabled = config.isDomStorageEnabled()
        webViewSetting.mixedContentMode = config.getMixedContentMode().mode
        webViewSetting.javaScriptEnabled = config.isJavaScriptEnabled()
        webViewSetting.cacheMode = config.getCacheMode().mode
        webViewSetting.useWideViewPort = config.isUseWideViewPort()
        webViewSetting.loadWithOverviewMode = config.isLoadWithOverviewMode()
        if (config.isUserAgentAppend()) {
            webViewSetting.userAgentString =
                "${webViewSetting.userAgentString} ${config.getUserAgent()}"
        } else {
            webViewSetting.userAgentString = config.getUserAgent()
        }
        webViewSetting.savePassword = config.isSavePassword()
        webViewSetting.allowFileAccess = config.isAllowFileAccess()
        webViewSetting.allowContentAccess = config.isAllowContentAccess()
        webViewSetting.allowFileAccessFromFileURLs = config.isAllowFileAccessFromFileURLs()
        webViewSetting.allowUniversalAccessFromFileURLs =
            config.isAllowUniversalAccessFromFileURLs()
        webViewSetting.mediaPlaybackRequiresUserGesture =
            config.isMediaPlaybackRequiresUserGesture()

        CookieManager.getInstance()
            .setAcceptThirdPartyCookies(webView, config.isAcceptThirdPartyCookies())

        webView.isVerticalScrollBarEnabled = config.isVerticalScrollBarEnabled()
        webView.isHorizontalScrollBarEnabled = config.isHorizontalScrollBarEnabled()
        webView.isHorizontalFadingEdgeEnabled = config.isHorizontalFadingEdgeEnabled()

        webDownloadListener?.let {
            webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
                var fileName: String? = null
                try {
                    fileName = URLUtil.guessFileName(url, contentDisposition, mimetype)
                } catch (throwable: Throwable) {
                    CsLogger.tag(tag).e(throwable)
                }
                it.onDownloadStart(
                    url ?: "",
                    fileName,
                    userAgent ?: "",
                    contentDisposition ?: "",
                    mimetype ?: "",
                    contentLength
                )
            }
        }

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