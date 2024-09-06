package com.proxy.service.webview.base.web.dialog

/**
 * @author: cangHX
 * @data: 2024/8/5 10:34
 * @desc:
 */
interface JsPromptResult : JsResult {

    /**
     * 确认
     * */
    fun confirm(result: String)
}