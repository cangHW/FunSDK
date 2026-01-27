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
        if (realCallback != null) {
            realCallback?.onPageStarted(url)
        } else {
            super.onPageStarted(url)
        }
    }

    override fun onProgressChanged(newProgress: Int) {
        if (realCallback != null) {
            realCallback?.onProgressChanged(newProgress)
        } else {
            super.onProgressChanged(newProgress)
        }
    }

    override fun onPageCommitVisible(url: String) {
        if (realCallback != null) {
            realCallback?.onPageCommitVisible(url)
        } else {
            super.onPageCommitVisible(url)
        }
    }

    override fun onPageFinished(url: String) {
        if (realCallback != null) {
            realCallback?.onPageFinished(url)
        } else {
            super.onPageFinished(url)
        }
    }

    override fun onPageFirstFrameRendered(url: String) {
        if (realCallback != null) {
            realCallback?.onPageFirstFrameRendered(url)
        } else {
            super.onPageFirstFrameRendered(url)
        }
    }

    override fun onReceivedTitle(title: String) {
        if (realCallback != null) {
            realCallback?.onReceivedTitle(title)
        } else {
            super.onReceivedTitle(title)
        }
    }

    override fun onReceivedIcon(icon: Bitmap?) {
        if (realCallback != null) {
            realCallback?.onReceivedIcon(icon)
        } else {
            super.onReceivedIcon(icon)
        }
    }

    override fun onPageError(url: String, isMainFrameError: Boolean, isHttpError: Boolean) {
        if (realCallback != null) {
            realCallback?.onPageError(url, isMainFrameError, isHttpError)
        } else {
            super.onPageError(url, isMainFrameError, isHttpError)
        }
    }

    override fun onPageSslError(error: SslError, handler: SslErrorHandler) {
        if (realCallback != null) {
            realCallback?.onPageSslError(error, handler)
        } else {
            super.onPageSslError(error, handler)
        }
    }
}