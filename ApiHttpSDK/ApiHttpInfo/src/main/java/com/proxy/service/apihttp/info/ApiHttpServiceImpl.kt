package com.proxy.service.apihttp.info

import com.proxy.service.annotations.CloudApiService
import com.proxy.service.apihttp.base.ApiHttpService
import com.proxy.service.apihttp.base.init.ApiConfig
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.retrofit.RetrofitManager
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/5/21 17:36
 * @desc:
 */
@CloudApiService(serviceTag = "service/api_http")
class ApiHttpServiceImpl: ApiHttpService {

    private val isInit = AtomicBoolean(false)

    override fun init(config: ApiConfig) {
        if (isInit.compareAndSet(false, true)) {
            Config.setApiConfig(config)
            RetrofitManager.clear()
        }
    }

    override fun <T> getService(service: Class<T>): T {
       return RetrofitManager.getRetrofit().create(service)
    }
}