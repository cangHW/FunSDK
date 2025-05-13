package com.proxy.service.apihttp.info.download.manager

import com.proxy.service.apihttp.info.common.okhttp.OkhttpFactory
import com.proxy.service.apihttp.info.download.manager.impl.OkhttpConfigImpl
import okhttp3.OkHttpClient

/**
 * @author: cangHX
 * @data: 2024/11/1 19:37
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