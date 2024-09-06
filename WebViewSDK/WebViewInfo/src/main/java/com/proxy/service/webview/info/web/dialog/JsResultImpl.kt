package com.proxy.service.webview.info.web.dialog

import com.proxy.service.webview.base.web.dialog.JsResult

/**
 * @author: cangHX
 * @data: 2024/8/5 11:30
 * @desc:
 */
class JsResultImpl(private val jsResult: android.webkit.JsResult?): JsResult {
    override fun cancel() {
        jsResult?.cancel()
    }

    override fun confirm() {
        jsResult?.confirm()
    }
}