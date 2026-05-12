package com.proxy.service.webview.base.web.error

/**
 * @author: cangHX
 * @date: 2024/8/3 10:45
 * @desc:
 */
interface SslErrorHandler {

    /**
     * 继续加载
     * */
    fun proceed()

    /**
     * 取消加载
     * */
    fun cancel()

}