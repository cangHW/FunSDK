package com.proxy.service.imageloader.info.net

/**
 * @author: cangHX
 * @data: 2025/10/14 14:48
 * @desc:
 */
interface RequestCallback {

    fun onSuccess(path: String)

    fun onFailed()

}