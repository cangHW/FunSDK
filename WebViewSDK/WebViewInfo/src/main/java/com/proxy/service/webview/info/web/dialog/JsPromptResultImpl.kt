package com.proxy.service.webview.info.web.dialog

import com.proxy.service.webview.base.web.dialog.JsPromptResult

/**
 * @author: cangHX
 * @data: 2024/8/5 11:32
 * @desc:
 */
class JsPromptResultImpl(private val jsPromptResult: android.webkit.JsPromptResult?) :
    JsPromptResult {
    override fun cancel() {
        jsPromptResult?.cancel()
    }

    override fun confirm(result: String) {
        jsPromptResult?.confirm(result)
    }

    override fun confirm() {
        jsPromptResult?.confirm()
    }
}