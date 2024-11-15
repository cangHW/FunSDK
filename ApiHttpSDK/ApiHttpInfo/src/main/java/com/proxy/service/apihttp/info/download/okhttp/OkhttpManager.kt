package com.proxy.service.apihttp.info.download.okhttp

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/11/1 19:37
 * @desc:
 */
object OkhttpManager {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)
            .build()
    }

    fun getOkhttpClient(): OkHttpClient {
        return client
    }

}