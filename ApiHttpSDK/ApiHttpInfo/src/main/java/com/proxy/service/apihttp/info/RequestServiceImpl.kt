package com.proxy.service.apihttp.info

import com.proxy.service.annotations.CloudApiNewInstance
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.apihttp.base.request.RequestService
import com.proxy.service.apihttp.base.request.config.RequestConfig
import com.proxy.service.apihttp.info.request.retrofit.RetrofitManager
import retrofit2.Retrofit

/**
 * @author: cangHX
 * @data: 2024/5/21 17:36
 * @desc:
 */
@CloudApiNewInstance
@CloudApiService(serviceTag = "service/http_request")
class RequestServiceImpl : RequestService {

    private val lock = Any()

    @Volatile
    private var requestConfig: RequestConfig? = null

    @Volatile
    private var retrofit: Retrofit? = null

    override fun init(config: RequestConfig) {
        if (requestConfig == null) {
            synchronized(lock) {
                if (requestConfig == null) {
                    this.requestConfig = config
                }
            }
        }
    }

    override fun <T> getService(service: Class<T>): T {
        if (requestConfig == null) {
            throw NullPointerException("Are you init ApiHttp first?")
        }

        if (retrofit == null) {
            synchronized(lock) {
                if (retrofit == null) {
                    retrofit = RetrofitManager.getRetrofit(requestConfig!!)
                }
            }
        }
        return retrofit!!.create(service)
    }
}