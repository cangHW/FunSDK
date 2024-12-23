package com.proxy.service.apihttp.info.upload.manager

import com.proxy.service.apihttp.info.common.okhttp.OkhttpFactory
import com.proxy.service.apihttp.info.upload.manager.impl.OkhttpConfigImpl
import okhttp3.OkHttpClient

/**
 * @author: cangHX
 * @data: 2024/12/19 20:12
 * @desc:
 */
object OkhttpManager {

    private val client: OkHttpClient by lazy {
        OkhttpFactory.create(OkhttpConfigImpl.instance)
    }

    fun getOkhttpClient(): OkHttpClient {
        return client
    }

}