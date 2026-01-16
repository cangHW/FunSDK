package com.proxy.service.webview.info.web.setting

import android.webkit.WebSettings
import com.proxy.service.webview.base.enums.CacheMode
import com.proxy.service.webview.base.enums.MixedContentMode
import com.proxy.service.webview.base.web.setting.ISetting

/**
 * @author: cangHX
 * @data: 2026/1/16 14:01
 * @desc:
 */
class SettingImpl constructor(
    private val settings: WebSettings
) : ISetting {

    override fun setSupportMultipleWindows(support: Boolean) {
        settings.setSupportMultipleWindows(support)
    }

    override fun setTextZoom(textZoom: Int) {
        settings.textZoom = textZoom
    }

    override fun setSupportZoom(support: Boolean) {
        settings.setSupportZoom(support)
    }

    override fun setBuiltInZoomControls(enabled: Boolean) {
        settings.builtInZoomControls = enabled
    }

    override fun setDisplayZoomControls(enabled: Boolean) {
        settings.displayZoomControls = enabled
    }

    override fun setDomStorageEnabled(flag: Boolean) {
        settings.domStorageEnabled = flag
    }

    override fun setMixedContentMode(mode: MixedContentMode) {
        settings.mixedContentMode = mode.mode
    }

    override fun setJavaScriptEnabled(enabled: Boolean) {
        settings.javaScriptEnabled = enabled
    }

    override fun setCacheMode(mode: CacheMode) {
        settings.cacheMode = mode.mode
    }

    override fun setUseWideViewPort(use: Boolean) {
        settings.useWideViewPort = use
    }

    override fun setLoadWithOverviewMode(overview: Boolean) {
        settings.loadWithOverviewMode = overview
    }

    override fun setUserAgent(ua: String, append: Boolean) {
        if (append) {
            settings.userAgentString = "${settings.userAgentString} $ua}"
        } else {
            settings.userAgentString = ua
        }
    }

    override fun setSavePassword(save: Boolean) {
        settings.savePassword = save
    }

    override fun setAllowFileAccess(allow: Boolean) {
        settings.allowFileAccess = allow
    }

    override fun setAllowContentAccess(allow: Boolean) {
        settings.allowContentAccess = allow
    }

    override fun setAllowFileAccessFromFileURLs(allow: Boolean) {
        settings.allowFileAccessFromFileURLs = allow
    }

    override fun setAllowUniversalAccessFromFileURLs(allow: Boolean) {
        settings.allowUniversalAccessFromFileURLs = allow
    }

    override fun setMediaPlaybackRequiresUserGesture(require: Boolean) {
        settings.mediaPlaybackRequiresUserGesture = require
    }

    override fun isSupportMultipleWindows(): Boolean {
        return settings.supportMultipleWindows()
    }

    override fun getTextZoom(): Int {
        return settings.textZoom
    }

    override fun isSupportZoom(): Boolean {
        return settings.supportZoom()
    }

    override fun isBuiltInZoomControls(): Boolean {
        return settings.builtInZoomControls
    }

    override fun isDisplayZoomControls(): Boolean {
        return settings.displayZoomControls
    }

    override fun isDomStorageEnabled(): Boolean {
        return settings.domStorageEnabled
    }

    override fun getMixedContentMode(): MixedContentMode {
        return MixedContentMode.value(settings.mixedContentMode)
    }

    override fun isJavaScriptEnabled(): Boolean {
        return settings.javaScriptEnabled
    }

    override fun getCacheMode(): CacheMode {
        return CacheMode.value(settings.cacheMode)
    }

    override fun isUseWideViewPort(): Boolean {
        return settings.useWideViewPort
    }

    override fun isLoadWithOverviewMode(): Boolean {
        return settings.loadWithOverviewMode
    }

    override fun getUserAgent(): String {
        return settings.userAgentString
    }

    override fun isSavePassword(): Boolean {
        return settings.savePassword
    }

    override fun isAllowFileAccess(): Boolean {
        return settings.allowFileAccess
    }

    override fun isAllowContentAccess(): Boolean {
        return settings.allowContentAccess
    }

    override fun isAllowFileAccessFromFileURLs(): Boolean {
        return settings.allowFileAccessFromFileURLs
    }

    override fun isAllowUniversalAccessFromFileURLs(): Boolean {
        return settings.allowUniversalAccessFromFileURLs
    }

    override fun isMediaPlaybackRequiresUserGesture(): Boolean {
        return settings.mediaPlaybackRequiresUserGesture
    }
}