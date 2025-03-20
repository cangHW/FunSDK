package com.proxy.service.webview.info.web.intercept

import android.net.Uri
import android.os.Build
import com.proxy.service.webview.base.web.request.WebResourceRequest

/**
 * @author: cangHX
 * @data: 2024/8/3 16:19
 * @desc:
 */
class WebResourceRequestImpl(
    private val request: android.webkit.WebResourceRequest
) : WebResourceRequest {

    override fun getUrl(): Uri {
        return request.url
    }

    override fun isForMainFrame(): Boolean {
        return request.isForMainFrame
    }

    override fun isRedirect(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request.isRedirect
        } else {
            false
        }
    }

    override fun hasGesture(): Boolean {
        return request.hasGesture()
    }

    override fun getMethod(): String {
        return request.method
    }

    override fun getRequestHeaders(): Map<String, String> {
        return request.requestHeaders ?: HashMap()
    }
}