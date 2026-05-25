package com.proxy.service.webview.base.config

import androidx.webkit.WebViewAssetLoader
import com.proxy.service.webview.base.enums.CacheMode
import com.proxy.service.webview.base.enums.MixedContentMode
import com.proxy.service.webview.base.listener.WebDownloadListener

/**
 * @author: cangHX
 * @date: 2024/7/31 18:28
 * @desc:
 */
class WebConfig private constructor(
    private val builder: IBuilderGet
) : IBuilderGet by builder{

    companion object {
        fun builder(): IBuilder {
            return Builder()
        }
    }

    class Builder : IBuilder, IBuilderGet {

        private var isSupportMultipleWindows = false
        private var textZoom = 100
        private var isSupportZoom = false
        private var isBuiltInZoomControls = false
        private var isDisplayZoomControls = true
        private var domStorageEnabled = true
        private var isAcceptThirdPartyCookies: Boolean = true
        private var mixedContentMode: MixedContentMode = MixedContentMode.MIXED_CONTENT_NEVER_ALLOW
        private var javaScriptEnabled: Boolean = true
        private var mediaPlaybackRequiresUserGesture: Boolean = true
        private var offscreenPreRaster: Boolean = false
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
        private var webResourceAssetLoader: WebViewAssetLoader? = null
        private var verticalScrollBarEnabled: Boolean = true
        private var horizontalScrollBarEnabled: Boolean = true
        private var horizontalFadingEdgeEnabled: Boolean = false

        private var webDownloadListener: WebDownloadListener? = null

        override fun setSupportMultipleWindows(support: Boolean): IBuilder {
            this.isSupportMultipleWindows = support
            return this
        }

        override fun setTextZoom(textZoom: Int): IBuilder {
            this.textZoom = textZoom
            return this
        }

        override fun setSupportZoom(support: Boolean): IBuilder {
            this.isSupportZoom = support
            return this
        }

        override fun setBuiltInZoomControls(enabled: Boolean): IBuilder {
            this.isBuiltInZoomControls = enabled
            return this
        }

        override fun setDisplayZoomControls(enabled: Boolean): IBuilder {
            this.isDisplayZoomControls = enabled
            return this
        }

        override fun setDomStorageEnabled(flag: Boolean): IBuilder {
            this.domStorageEnabled = flag
            return this
        }

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

        override fun setMediaPlaybackRequiresUserGesture(require: Boolean): IBuilder {
            this.mediaPlaybackRequiresUserGesture = require
            return this
        }

        override fun setOffscreenPreRaster(enabled: Boolean): IBuilder {
            this.offscreenPreRaster = enabled
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

        override fun setWebDownloadCallback(callback: WebDownloadListener?): IBuilder {
            this.webDownloadListener = callback
            return this
        }

        override fun build(): WebConfig {
            return WebConfig(this)
        }

        override fun isSupportMultipleWindows(): Boolean {
            return isSupportMultipleWindows
        }

        override fun getTextZoom(): Int {
            return textZoom
        }

        override fun isSupportZoom(): Boolean {
            return isSupportZoom
        }

        override fun isBuiltInZoomControls(): Boolean {
            return isBuiltInZoomControls
        }

        override fun isDisplayZoomControls(): Boolean {
            return isDisplayZoomControls
        }

        override fun isDomStorageEnabled(): Boolean {
            return domStorageEnabled
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

        override fun isMediaPlaybackRequiresUserGesture(): Boolean {
            return mediaPlaybackRequiresUserGesture
        }

        override fun isOffscreenPreRaster(): Boolean {
            return offscreenPreRaster
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

        override fun getWebDownloadListener(): WebDownloadListener? {
            return webDownloadListener
        }
    }

}