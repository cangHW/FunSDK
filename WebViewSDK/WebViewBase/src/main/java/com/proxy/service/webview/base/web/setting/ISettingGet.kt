package com.proxy.service.webview.base.web.setting

import com.proxy.service.webview.base.enums.CacheMode
import com.proxy.service.webview.base.enums.MixedContentMode

/**
 * @author: cangHX
 * @data: 2026/1/16 11:06
 * @desc:
 */
interface ISettingGet {

    /**
     * 是否支持多窗口模式。
     * */
    fun isSupportMultipleWindows(): Boolean

    /**
     * 获取文本的缩放比例
     * */
    fun getTextZoom(): Int

    /**
     * 是否支持缩放
     * */
    fun isSupportZoom(): Boolean

    /**
     * 是否使用系统默认的缩放功能
     * */
    fun isBuiltInZoomControls(): Boolean

    /**
     * 是否显示系统默认的缩放控件（放大/缩小按钮）
     * */
    fun isDisplayZoomControls(): Boolean

    /**
     * 是否启用 DOM Storage
     * */
    fun isDomStorageEnabled(): Boolean

    /**
     * 设置 WebView 的混合内容模式, 混合内容指的是在 HTTPS 页面中加载 HTTP 内容，可能会导致安全问题。
     * */
    fun getMixedContentMode(): MixedContentMode

    /**
     * 是否应该启用 JavaScript 执行。
     * */
    fun isJavaScriptEnabled(): Boolean

    /**
     * 缓存模式。
     * */
    fun getCacheMode(): CacheMode

    /**
     * WebView 是否支持 HTML 的 viewport 标签或者使用一个广泛的视口。
     * */
    fun isUseWideViewPort(): Boolean

    /**
     * WebView 是否在加载页面时将内容缩放以适应屏幕的宽度。
     * */
    fun isLoadWithOverviewMode(): Boolean

    /**
     * 用户代理。
     * */
    fun getUserAgent(): String

    /**
     * 是否密码自动填充。
     * */
    fun isSavePassword(): Boolean

    /**
     * 是否允许使用文件系统。
     * */
    fun isAllowFileAccess(): Boolean

    /**
     * 是否允许 WebView 通过内容提供者(ContentProvider)访问内容URI。
     * */
    fun isAllowContentAccess(): Boolean

    /**
     * 是否允许在 file URL 上下文中运行 JavaScript 代码时访问其他文件 URL。
     * */
    fun isAllowFileAccessFromFileURLs(): Boolean

    /**
     * 是否允许在 file URL 上下文中运行 JavaScript 代码时访问所有来源（包括HTTP和HTTPS）的 URL。
     * */
    fun isAllowUniversalAccessFromFileURLs(): Boolean

    /**
     * 媒体内容（如音频和视频）是否需要用户手势才能播放
     * */
    fun isMediaPlaybackRequiresUserGesture(): Boolean
}