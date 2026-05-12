package com.proxy.service.apihttp.info.download.manager.impl

import com.proxy.service.apihttp.base.download.config.DownloadConfig
import com.proxy.service.apihttp.info.common.okhttp.OkHttpCommonConfig

/**
 * @author: cangHX
 * @date: 2024/12/17 18:39
 * @desc:
 */
class DownloadOkHttpConfig(
    private val config: DownloadConfig
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