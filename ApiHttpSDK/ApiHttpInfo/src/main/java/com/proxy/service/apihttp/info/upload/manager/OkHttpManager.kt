package com.proxy.service.apihttp.info.upload.manager

import com.proxy.service.apihttp.base.upload.config.UploadConfig
import com.proxy.service.apihttp.info.common.okhttp.OkHttpFactory
import com.proxy.service.apihttp.info.upload.manager.impl.UploadOkHttpConfig
import okhttp3.OkHttpClient

/**
 * @author: cangHX
 * @date: 2024/12/19 20:12
 * @desc:
 */
object OkHttpManager {

    private var client: OkHttpClient? = null

    fun createOkhttpClient(config: UploadConfig) {
        client = OkHttpFactory.create(UploadOkHttpConfig(config))
    }

    fun getOkhttpClient(): OkHttpClient? {
        return client
    }

}