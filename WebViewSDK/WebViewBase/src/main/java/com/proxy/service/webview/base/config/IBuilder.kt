package com.proxy.service.webview.base.config

import androidx.webkit.WebViewAssetLoader
import com.proxy.service.webview.base.listener.WebDownloadListener
import com.proxy.service.webview.base.web.IWebLoader
import com.proxy.service.webview.base.web.setting.ISettingSet

/**
 * @author: cangHX
 * @data: 2024/8/1 19:46
 * @desc:
 */
interface IBuilder: ISettingSet<IBuilder> {

    /**
     * 设置 WebView 是否接受第三方 Cookie, 这个方法在 Android API 级别 21（Lollipop）及以上有效。
     * 默认为 true 接受
     * */
    fun setAcceptThirdPartyCookies(accept: Boolean): IBuilder

    /**
     * 设置 web 容器本地资源加载器
     * */
    fun setWebViewAssetLoader(loader: WebViewAssetLoader): IBuilder

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
     * 设置下载回调，用于处理下载事件
     * */
    fun setWebDownloadCallback(callback: WebDownloadListener?): IBuilder

    /**
     * 创建 WebConfig 对象
     * */
    fun build(): WebConfig

}