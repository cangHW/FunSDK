package com.proxy.service.apihttp.info.request.okhttp

import com.proxy.service.apihttp.base.request.config.RequestConfig
import com.proxy.service.apihttp.info.common.okhttp.OkHttpCommonConfig
import com.proxy.service.apihttp.info.request.okhttp.interceptor.BaseUrlInterceptor
import com.proxy.service.apihttp.info.request.okhttp.interceptor.ExactTimeOutInterceptor
import com.proxy.service.apihttp.info.request.okhttp.interceptor.HttpLoggingInterceptor
import com.proxy.service.apihttp.info.request.okhttp.interceptor.NetworkInterceptor
import com.proxy.service.apihttp.info.request.okhttp.interceptor.RetryWithDelayInterceptor
import okhttp3.Interceptor

/**
 * @author: cangHX
 * @date: 2024/12/17 18:11
 * @desc:
 */
class RequestOkHttpConfig(
    private val config: RequestConfig
): OkHttpCommonConfig(config) {

    override fun getConnectTimeOut(): Long {
        return config.getConnectTimeOut()
    }

    override fun getWriteTimeOut(): Long {
        return config.getWriteTimeOut()
    }

    override fun getReadTimeOut(): Long {
        return config.getReadTimeOut()
    }

    override fun getCacheMaxSize(): Long {
        return config.getCacheMaxSize()
    }

    override fun getCacheDir(): String {
        return config.getCacheDir()
    }

    override fun getInterceptors(): MutableList<Interceptor> {
        val list = ArrayList<Interceptor>()
        list.add(BaseUrlInterceptor(config.getBaseUrl()))
        list.addAll(config.getInterceptors())
        list.add(RetryWithDelayInterceptor())
        list.add(HttpLoggingInterceptor())
        list.add(NetworkInterceptor())
        list.add(ExactTimeOutInterceptor())
        return list
    }

    override fun getMaxRequest(): Int {
        return config.getMaxRequest()
    }
}