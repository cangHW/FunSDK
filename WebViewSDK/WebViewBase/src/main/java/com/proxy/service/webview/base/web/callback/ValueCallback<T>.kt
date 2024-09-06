package com.proxy.service.webview.base.web.callback

/**
 * @author: cangHX
 * @data: 2024/8/10 16:45
 * @desc:
 */
interface ValueCallback<T> {

    fun onReceiveValue(value: T)

}