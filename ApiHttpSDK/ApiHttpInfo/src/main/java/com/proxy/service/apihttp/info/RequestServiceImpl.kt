package com.proxy.service.apihttp.info

import com.proxy.service.annotations.CloudApiService
import com.proxy.service.apihttp.base.request.RequestService
import com.proxy.service.apihttp.base.request.config.RequestConfig
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.request.retrofit.RetrofitManager

/**
 * @author: cangHX
 * @data: 2024/5/21 17:36
 * @desc:
 */
@CloudApiService(serviceTag = "service/http_request")
class RequestServiceImpl : RequestService {

    private val lock = Any()
    @Volatile
    private var isInit = false

    override fun init(config: RequestConfig) {
        if (!isInit) {
            synchronized(lock) {
                if (!isInit) {
                    Config.setRequestConfig(config)
                    RetrofitManager.clear()
                    isInit = true
                }
            }
        }
    }

    override fun <T> getService(service: Class<T>): T {
        return RetrofitManager.getRetrofit().create(service)
    }
}