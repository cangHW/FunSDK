package com.proxy.service.core.framework.io.file.media.callback

import com.proxy.service.core.framework.io.file.media.config.DataInfo

/**
 * @author: cangHX
 * @data: 2025/1/2 18:03
 * @desc:
 */
interface QueryCallback {

    /**
     * 成功
     * */
    fun onSuccess(list: ArrayList<DataInfo>)

    /**
     * 失败
     * */
    fun onFailed()

}