package com.proxy.service.core.framework.io.file.callback

/**
 * @author: cangHX
 * @date: 2024/11/8 17:30
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