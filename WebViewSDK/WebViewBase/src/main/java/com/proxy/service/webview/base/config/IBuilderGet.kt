package com.proxy.service.webview.base.config

import androidx.webkit.WebViewAssetLoader
import com.proxy.service.webview.base.listener.WebDownloadListener
import com.proxy.service.webview.base.web.setting.ISettingGet

/**
 * @author: cangHX
 * @data: 2024/8/1 19:46
 * @desc:
 */
interface IBuilderGet : ISettingGet {

    /**
     * 获取 WebView 是否接受第三方 Cookie, 这个方法在 Android API 级别 21（Lollipop）及以上有效。
     * */
    fun isAcceptThirdPartyCookies(): Boolean

    /**
     * 是否是追加模式设置用户代理。
     * */
    fun isUserAgentAppend(): Boolean

    /**
     * 获取 web 容器本地资源加载器
     * */
    fun getWebViewAssetLoader(): WebViewAssetLoader?

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

    /**
     * 获取下载回调
     * */
    fun getWebDownloadListener(): WebDownloadListener?
}