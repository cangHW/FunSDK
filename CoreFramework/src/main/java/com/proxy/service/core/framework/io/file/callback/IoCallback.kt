package com.proxy.service.core.framework.io.file.callback

/**
 * @author: cangHX
 * @data: 2024/11/8 17:30
 * @desc:
 */
interface IoCallback {

    /**
     * 成功
     * */
    fun onSuccess()

    /**
     * 失败
     * */
    fun onFailed()

}