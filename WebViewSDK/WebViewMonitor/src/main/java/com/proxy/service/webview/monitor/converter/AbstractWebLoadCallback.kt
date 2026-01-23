package com.proxy.service.webview.monitor.converter

import android.graphics.Bitmap
import com.proxy.service.webview.base.listener.WebLoadCallback
import com.proxy.service.webview.base.web.error.SslError
import com.proxy.service.webview.base.web.error.SslErrorHandler

/**
 * @author: cangHX
 * @data: 2026/1/23 13:48
 * @desc:
 */
abstract class AbstractWebLoadCallback : WebLoadCallback {

    private var realCallback: WebLoadCallback? = null

    fun setRealWebLoadCallback(callback: WebLoadCallback) {
        this.realCallback = callback
    }

    override fun onPageStarted(url: String) {
        super.onPageStarted(url)
        realCallback?.onPageStarted(url)
    }

    override fun onProgressChanged(newProgress: Int) {
        super.onProgressChanged(newProgress)
        realCallback?.onProgressChanged(newProgress)
    }

    override fun onPageCommitVisible(url: String) {
        super.onPageCommitVisible(url)
        realCallback?.onPageCommitVisible(url)
    }

    override fun onPageFinished(url: String) {
        super.onPageFinished(url)
        realCallback?.onPageFinished(url)
    }

    override fun onPageFirstFrameRendered(url: String) {
        super.onPageFirstFrameRendered(url)
        realCallback?.onPageFirstFrameRendered(url)
    }

    override fun onReceivedTitle(title: String) {
        super.onReceivedTitle(title)
        realCallback?.onReceivedTitle(title)
    }

    override fun onReceivedIcon(icon: Bitmap?) {
        super.onReceivedIcon(icon)
        realCallback?.onReceivedIcon(icon)
    }

    override fun onPageError(url: String, isMainFrameError: Boolean, isHttpError: Boolean) {
        super.onPageError(url, isMainFrameError, isHttpError)
        realCallback?.onPageError(url, isMainFrameError, isHttpError)
    }

    override fun onPageSslError(error: SslError, handler: SslErrorHandler) {
        super.onPageSslError(error, handler)
        realCallback?.onPageSslError(error, handler)
    }
}