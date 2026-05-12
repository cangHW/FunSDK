package com.proxy.service.apihttp.info.upload.manager.impl

import com.proxy.service.apihttp.base.upload.config.UploadConfig
import com.proxy.service.apihttp.info.common.okhttp.OkHttpCommonConfig

/**
 * @author: cangHX
 * @date: 2024/12/19 20:13
 * @desc:
 */
class UploadOkHttpConfig(
    private val config: UploadConfig
) : OkHttpCommonConfig(config) {

    override fun getConnectTimeOut(): Long {
        return config.getConnectTimeOut()
    }

    override fun getWriteTimeOut(): Long? {
        return null
    }

    override fun getReadTimeOut(): Long? {
        return null
    }

    override fun getCacheMaxSize(): Long? {
        return null
    }

    override fun getCacheDir(): String? {
        return null
    }

    override fun getMaxRequest(): Int? {
        return null
    }
}