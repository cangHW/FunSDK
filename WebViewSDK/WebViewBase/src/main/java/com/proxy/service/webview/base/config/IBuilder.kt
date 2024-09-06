package com.proxy.service.webview.base.config

import com.proxy.service.webview.base.enums.CacheMode
import com.proxy.service.webview.base.enums.MixedContentMode

/**
 * @author: cangHX
 * @data: 2024/8/1 19:46
 * @desc:
 */
interface IBuilder {

    /**
     * 设置 WebView 是否接受第三方 Cookie, 这个方法在 Android API 级别 21（Lollipop）及以上有效。
     * 默认为 true 接受
     * */
    fun setAcceptThirdPartyCookies(accept: Boolean): IBuilder

    /**
     * 设置 WebView 的混合内容模式, 混合内容指的是在 HTTPS 页面中加载 HTTP 内容，可能会导致安全问题。
     * 默认为 [MixedContentMode.MIXED_CONTENT_NEVER_ALLOW]
     * */
    fun setMixedContentMode(mode: MixedContentMode): IBuilder

    /**
     * 设置是否启用 WebView 的调试功能。
     * 默认为 false
     * */
    fun setWebContentsDebuggingEnabled(enabled: Boolean): IBuilder

    /**
     * 设置是否应该启用 JavaScript 执行。
     * 默认为 true
     * */
    fun setJavaScriptEnabled(enabled: Boolean): IBuilder

    /**
     * 设置缓存模式，不同的缓存模式可能会受到浏览器实现和版本的影响。
     * 默认为 [CacheMode.LOAD_DEFAULT]
     * */
    fun setCacheMode(mode: CacheMode): IBuilder

    /**
     * 设置 WebView 是否支持 HTML 的 viewport 标签或者使用一个广泛的视口。
     * 默认为 false
     * */
    fun setUseWideViewPort(use: Boolean): IBuilder

    /**
     * 设置 WebView 是否在加载页面时将内容缩放以适应屏幕的宽度。
     * 默认为 true
     * */
    fun setLoadWithOverviewMode(overview: Boolean): IBuilder

    /**
     * 设置用户代理。
     * @param append    是否是追加，默认为 true
     * */
    fun setUserAgent(ua: String, append: Boolean = true): IBuilder

    /**
     * 是否允许使用文件系统。
     * 默认为 false
     * */
    fun setAllowFileAccess(allow: Boolean): IBuilder

    /**
     * 设置是否允许显示垂直滚动条
     * 默认为 true
     * */
    fun setVerticalScrollBarEnabled(verticalScrollBarEnabled: Boolean): IBuilder

    /**
     * 设置是否允许显示水平滚动条
     * 默认为 true
     * */
    fun setHorizontalScrollBarEnabled(horizontalScrollBarEnabled: Boolean): IBuilder

    /**
     * 设置是否显示水平渐隐效果，用于提示可以上下滑动
     * 默认为 false
     * */
    fun setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled: Boolean): IBuilder

    /**
     * 创建 WebConfig 对象
     * */
    fun build(): WebConfig

}