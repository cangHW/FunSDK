package com.proxy.service.webview.bridge.ds.callback

/**
 * @author: cangHX
 * @date: 2024/8/10 17:41
 * @desc:
 */
interface CompletionHandler {

    /**
     * 进行中
     * */
    fun setProgressData(value: String)

    /**
     * 完成
     * */
    fun complete(value: String)

    /**
     * 完成
     * */
    fun complete()

}