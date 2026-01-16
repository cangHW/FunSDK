package com.proxy.service.webview.base.web.setting

import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.enums.CacheMode
import com.proxy.service.webview.base.enums.MixedContentMode

/**
 * @author: cangHX
 * @data: 2026/1/16 11:06
 * @desc:
 */
interface ISettingSet<T> {

    /**
     * 是否支持多窗口模式。
     * 默认为 false
     * */
    fun setSupportMultipleWindows(support: Boolean): T

    /**
     * 设置文本的缩放比例（百分比）。
     *
     * 默认值为 100，表示正常大小（即 100%）。
     * 设置为 150 表示文字放大到 150%。
     * 设置为 50 表示文字缩小到 50%。
     * */
    fun setTextZoom(textZoom: Int): T

    /**
     * 设置是否支持缩放。
     * 默认为 false
     * */
    fun setSupportZoom(support: Boolean): T

    /**
     * 是否使用系统默认的缩放功能
     * 默认为 false
     * */
    fun setBuiltInZoomControls(enabled: Boolean): T

    /**
     * 是否显示系统默认的缩放控件（放大/缩小按钮）。依赖于 [setBuiltInZoomControls] 设置为 true，否则不会显示缩放控件。
     * 默认为 true
     * */
    fun setDisplayZoomControls(enabled: Boolean): T

    /**
     * 启用或禁用 DOM Storage（文档对象模型存储）。
     * 默认为 true
     * */
    fun setDomStorageEnabled(flag: Boolean): T

    /**
     * 设置 WebView 的混合内容模式, 混合内容指的是在 HTTPS 页面中加载 HTTP 内容，可能会导致安全问题。
     * 默认为 [MixedContentMode.MIXED_CONTENT_NEVER_ALLOW]
     * */
    fun setMixedContentMode(mode: MixedContentMode): T

    /**
     * 设置是否应该启用 JavaScript 执行。
     * 默认为 true
     * */
    fun setJavaScriptEnabled(enabled: Boolean): T

    /**
     * 设置缓存模式，不同的缓存模式可能会受到浏览器实现和版本的影响。
     * 默认为 [CacheMode.LOAD_DEFAULT]
     * */
    fun setCacheMode(mode: CacheMode): T

    /**
     * 设置 WebView 是否支持 HTML 的 viewport 标签或者使用一个广泛的视口。
     * 默认为 false
     * */
    fun setUseWideViewPort(use: Boolean): T

    /**
     * 设置 WebView 是否在加载页面时将内容缩放以适应屏幕的宽度。
     * 默认为 true
     * */
    fun setLoadWithOverviewMode(overview: Boolean): T

    /**
     * 设置用户代理。
     * @param append    是否是追加，默认为 true
     * */
    fun setUserAgent(ua: String, append: Boolean = true): T

    /**
     * 设置是否密码自动填充。
     * 默认为 false
     * */
    fun setSavePassword(save: Boolean): T

    /**
     * 是否允许使用文件系统。
     * 默认为 false
     * */
    fun setAllowFileAccess(allow: Boolean): T

    /**
     * 是否允许 WebView 通过内容提供者(ContentProvider)访问内容URI。
     * 默认为 false
     * */
    fun setAllowContentAccess(allow: Boolean): T

    /**
     * 是否允许在 file URL 上下文中运行 JavaScript 代码时访问其他文件 URL。
     * 默认为 false。
     *
     * 建议使用 [WebConfig.Builder.setWebViewAssetLoader]
     * */
    fun setAllowFileAccessFromFileURLs(allow: Boolean): T

    /**
     * 是否允许在 file URL 上下文中运行 JavaScript 代码时访问所有来源（包括HTTP和HTTPS）的 URL。
     * 默认为 false
     *
     * 建议使用 [WebConfig.Builder.setWebViewAssetLoader]
     * */
    fun setAllowUniversalAccessFromFileURLs(allow: Boolean): T

    /**
     * 设置媒体内容（如音频和视频）是否需要用户手势才能播放
     * 默认为 true
     * */
    fun setMediaPlaybackRequiresUserGesture(require: Boolean): T
}