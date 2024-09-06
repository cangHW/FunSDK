package com.proxy.service.webview.base.config

import com.proxy.service.webview.base.enums.CacheMode
import com.proxy.service.webview.base.enums.MixedContentMode

/**
 * @author: cangHX
 * @data: 2024/8/1 19:46
 * @desc:
 */
interface IBuilderGet {

    /**
     * 获取 WebView 是否接受第三方 Cookie, 这个方法在 Android API 级别 21（Lollipop）及以上有效。
     * */
    fun isAcceptThirdPartyCookies(): Boolean

    /**
     * 设置 WebView 的混合内容模式, 混合内容指的是在 HTTPS 页面中加载 HTTP 内容，可能会导致安全问题。
     * */
    fun getMixedContentMode(): MixedContentMode

    /**
     * 是否启用 WebView 的调试功能。
     * */
    fun isWebContentsDebuggingEnabled(): Boolean

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
     * 是否是追加模式设置用户代理。
     * */
    fun isUserAgentAppend(): Boolean

    /**
     * 是否允许使用文件系统。
     * */
    fun isAllowFileAccess(): Boolean

    /**
     * 是否允许显示垂直滚动条
     * */
    fun isVerticalScrollBarEnabled(): Boolean

    /**
     * 是否允许显示水平滚动条
     * */
    fun isHorizontalScrollBarEnabled(): Boolean

    /**
     * 是否显示水平渐隐效果，用于提示可以上下滑动
     * */
    fun isHorizontalFadingEdgeEnabled(): Boolean

}