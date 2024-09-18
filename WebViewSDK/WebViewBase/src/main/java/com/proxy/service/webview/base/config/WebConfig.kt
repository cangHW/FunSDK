package com.proxy.service.webview.base.config

import androidx.webkit.WebViewAssetLoader
import com.proxy.service.webview.base.enums.CacheMode
import com.proxy.service.webview.base.enums.MixedContentMode

/**
 * @author: cangHX
 * @data: 2024/7/31 18:28
 * @desc:
 */
class WebConfig private constructor(private val builder: IBuilderGet) : IBuilderGet {

    override fun isAcceptThirdPartyCookies(): Boolean {
        return builder.isAcceptThirdPartyCookies()
    }

    override fun getMixedContentMode(): MixedContentMode {
        return builder.getMixedContentMode()
    }

    override fun isJavaScriptEnabled(): Boolean {
        return builder.isJavaScriptEnabled()
    }

    override fun getCacheMode(): CacheMode {
        return builder.getCacheMode()
    }

    override fun isUseWideViewPort(): Boolean {
        return builder.isUseWideViewPort()
    }

    override fun isLoadWithOverviewMode(): Boolean {
        return builder.isLoadWithOverviewMode()
    }

    override fun getUserAgent(): String {
        return builder.getUserAgent()
    }

    override fun isUserAgentAppend(): Boolean {
        return builder.isUserAgentAppend()
    }

    override fun isSavePassword(): Boolean {
        return builder.isSavePassword()
    }

    override fun isAllowFileAccess(): Boolean {
        return builder.isAllowFileAccess()
    }

    override fun isAllowContentAccess(): Boolean {
        return builder.isAllowContentAccess()
    }

    override fun isAllowFileAccessFromFileURLs(): Boolean {
        return builder.isAllowFileAccessFromFileURLs()
    }

    override fun isAllowUniversalAccessFromFileURLs(): Boolean {
        return builder.isAllowUniversalAccessFromFileURLs()
    }

    override fun getWebViewAssetLoader(): WebViewAssetLoader? {
        return builder.getWebViewAssetLoader()
    }

    override fun isVerticalScrollBarEnabled(): Boolean {
        return builder.isVerticalScrollBarEnabled()
    }

    override fun isHorizontalScrollBarEnabled(): Boolean {
        return builder.isHorizontalScrollBarEnabled()
    }

    override fun isHorizontalFadingEdgeEnabled(): Boolean {
        return builder.isHorizontalFadingEdgeEnabled()
    }

    companion object {
        fun builder(): IBuilder {
            return Builder()
        }
    }

    class Builder : IBuilder, IBuilderGet {

        private var isAcceptThirdPartyCookies: Boolean = true
        private var mixedContentMode: MixedContentMode = MixedContentMode.MIXED_CONTENT_NEVER_ALLOW
        private var javaScriptEnabled: Boolean = true
        private var cacheMode: CacheMode = CacheMode.LOAD_DEFAULT
        private var isUseWideViewPort: Boolean = false
        private var isLoadWithOverviewMode: Boolean = true
        private var userAgent: String = ""
        private var isUserAgentAppend: Boolean = true
        private var isSavePassword: Boolean = false
        private var isAllowFileAccess: Boolean = false
        private var isAllowContentAccess: Boolean = false
        private var isAllowFileAccessFromFileURLs: Boolean = false
        private var isAllowUniversalAccessFromFileURLs: Boolean = false
        private var webResourceAssetLoader: WebViewAssetLoader?=null
        private var verticalScrollBarEnabled:Boolean = true
        private var horizontalScrollBarEnabled:Boolean = true
        private var horizontalFadingEdgeEnabled:Boolean = false

        override fun setAcceptThirdPartyCookies(accept: Boolean): IBuilder {
            this.isAcceptThirdPartyCookies = accept
            return this
        }

        override fun setMixedContentMode(mode: MixedContentMode): IBuilder {
            this.mixedContentMode = mode
            return this
        }

        override fun setJavaScriptEnabled(enabled: Boolean): IBuilder {
            this.javaScriptEnabled = enabled
            return this
        }

        override fun setCacheMode(mode: CacheMode): IBuilder {
            this.cacheMode = mode
            return this
        }

        override fun setUseWideViewPort(use: Boolean): IBuilder {
            this.isUseWideViewPort = use
            return this
        }

        override fun setLoadWithOverviewMode(overview: Boolean): IBuilder {
            this.isLoadWithOverviewMode = overview
            return this
        }

        override fun setUserAgent(ua: String, append: Boolean): IBuilder {
            this.userAgent = ua
            this.isUserAgentAppend = append
            return this
        }

        override fun setSavePassword(save: Boolean): IBuilder {
            this.isSavePassword = save
            return this
        }

        override fun setAllowFileAccess(allow: Boolean): IBuilder {
            this.isAllowFileAccess = allow
            return this
        }

        override fun setAllowContentAccess(allow: Boolean): IBuilder {
            this.isAllowContentAccess = allow
            return this
        }

        override fun setAllowFileAccessFromFileURLs(allow: Boolean): IBuilder {
            this.isAllowFileAccessFromFileURLs = allow
            return this
        }

        override fun setAllowUniversalAccessFromFileURLs(allow: Boolean): IBuilder {
            this.isAllowUniversalAccessFromFileURLs = allow
            return this
        }

        override fun setWebViewAssetLoader(loader: WebViewAssetLoader): IBuilder {
            this.webResourceAssetLoader = loader
            return this
        }

        override fun setVerticalScrollBarEnabled(verticalScrollBarEnabled: Boolean): IBuilder {
            this.verticalScrollBarEnabled = verticalScrollBarEnabled
            return this
        }

        override fun setHorizontalScrollBarEnabled(horizontalScrollBarEnabled: Boolean): IBuilder {
            this.horizontalScrollBarEnabled = horizontalScrollBarEnabled
            return this
        }

        override fun setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled: Boolean): IBuilder {
            this.horizontalFadingEdgeEnabled = horizontalFadingEdgeEnabled
            return this
        }

        override fun build(): WebConfig {
            return WebConfig(this)
        }

        override fun isAcceptThirdPartyCookies(): Boolean {
            return isAcceptThirdPartyCookies
        }

        override fun getMixedContentMode(): MixedContentMode {
            return mixedContentMode
        }

        override fun isJavaScriptEnabled(): Boolean {
            return javaScriptEnabled
        }

        override fun getCacheMode(): CacheMode {
            return cacheMode
        }

        override fun isUseWideViewPort(): Boolean {
            return isUseWideViewPort
        }

        override fun isLoadWithOverviewMode(): Boolean {
            return isLoadWithOverviewMode
        }

        override fun getUserAgent(): String {
            return userAgent
        }

        override fun isUserAgentAppend(): Boolean {
            return isUserAgentAppend
        }

        override fun isSavePassword(): Boolean {
            return isSavePassword
        }

        override fun isAllowFileAccess(): Boolean {
            return isAllowFileAccess
        }

        override fun isAllowContentAccess(): Boolean {
            return isAllowContentAccess
        }

        override fun isAllowFileAccessFromFileURLs(): Boolean {
            return isAllowFileAccessFromFileURLs
        }

        override fun isAllowUniversalAccessFromFileURLs(): Boolean {
            return isAllowUniversalAccessFromFileURLs
        }

        override fun getWebViewAssetLoader(): WebViewAssetLoader? {
            return webResourceAssetLoader
        }

        override fun isVerticalScrollBarEnabled(): Boolean {
            return verticalScrollBarEnabled
        }

        override fun isHorizontalScrollBarEnabled(): Boolean {
            return horizontalScrollBarEnabled
        }

        override fun isHorizontalFadingEdgeEnabled(): Boolean {
            return horizontalFadingEdgeEnabled
        }
    }

}