package com.proxy.service.core.framework.io.file.media.callback

/**
 * @author: cangHX
 * @data: 2024/11/8 17:30
 * @desc:
 */
interface InsertCallback {

    /**
     * 成功
     * */
    fun onSuccess(path: String)

    /**
     * 失败
     * */
    fun onFailed()

}