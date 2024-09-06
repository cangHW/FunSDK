package com.proxy.service.webview.info.web.error

import com.proxy.service.webview.base.web.error.SslErrorHandler

/**
 * @author: cangHX
 * @data: 2024/8/3 11:02
 * @desc:
 */
class SslErrorHandlerImpl(private val handler: android.webkit.SslErrorHandler) :
    SslErrorHandler {
    override fun proceed() {
        handler.proceed()
    }

    override fun cancel() {
        handler.cancel()
    }
}