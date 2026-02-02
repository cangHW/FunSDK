package com.proxy.service.webview.base.listener

import android.graphics.Bitmap
import com.proxy.service.webview.base.web.error.SslError
import com.proxy.service.webview.base.web.error.SslErrorHandler

/**
 * @author: cangHX
 * @data: 2024/7/31 18:28
 * @desc:
 */
interface WebLoadCallback {

    /**
     * 页面开始加载
     * */
    fun onPageStarted(url: String) {}

    /**
     * 页面加载进度变化
     * */
    fun onProgressChanged(newProgress: Int) {}

    /**
     * 页面内容部分可见，加载没有完全结束，可用于隐藏加载 loading
     * */
    fun onPageCommitVisible(url: String) {}

    /**
     * 页面加载结束, 当页面的主要资源（HTML 文档）加载完成时触发。
     * 但此时页面的视觉状态可能尚未完全渲染完成（例如 CSS 或 JavaScript 的渲染可能还在进行中）,
     * 如果需要监听页面渲染完成事件可以使用 [onPageFirstFrameRendered]
     * */
    fun onPageFinished(url: String) {}

    /**
     * 页面全部资源加载结束, 在 HTML 文档和所有子资源（style.css、image.jpg、script.js）加载完成后触发。
     * */
    fun onPageAllResourceFinished(url: String) {}

    /**
     * 页面首帧渲染完成, 需要页面可见。
     * */
    fun onPageFirstFrameRendered(url: String) {}

    /**
     * 接收到页面标题
     * */
    fun onReceivedTitle(title: String) {}

    /**
     * 接收到页面 icon
     * */
    fun onReceivedIcon(icon: Bitmap?) {}

    /**
     * 页面加载出错
     * @param isMainFrameError  是否是整体页面加载出错
     * @param isHttpError       是否是网络错误
     * */
    fun onPageError(url: String, isMainFrameError: Boolean, isHttpError: Boolean) {}

    /**
     * 页面证书错误，如：证书过期、证书不受信任等。
     * 默认情况下，会取消加载页面，但可以通过 [SslErrorHandler] 通知是否需要重新加载
     * */
    fun onPageSslError(error: SslError, handler: SslErrorHandler) {
        handler.cancel()
    }
}