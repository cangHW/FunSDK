package com.proxy.service.apihttp.info.download.manager

import com.proxy.service.apihttp.base.download.config.DownloadConfig
import com.proxy.service.apihttp.info.common.okhttp.OkHttpFactory
import com.proxy.service.apihttp.info.download.manager.impl.DownloadOkHttpConfig
import okhttp3.OkHttpClient

/**
 * @author: cangHX
 * @date: 2024/11/1 19:37
 * @desc:
 */
object OkHttpManager {

    private var client: OkHttpClient? = null

    fun createOkhttpClient(config: DownloadConfig) {
        client = OkHttpFactory.create(DownloadOkHttpConfig(config))
    }

    fun getOkhttpClient(): OkHttpClient? {
        return client
    }

}