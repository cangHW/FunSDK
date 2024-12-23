package com.proxy.service.apihttp.info.request.okhttp

import com.proxy.service.apihttp.base.request.config.RequestConfig
import com.proxy.service.apihttp.info.common.okhttp.IOkhttpConfig
import com.proxy.service.apihttp.info.request.okhttp.interceptor.BaseUrlInterceptor
import com.proxy.service.apihttp.info.request.okhttp.interceptor.ExactTimeOutInterceptor
import com.proxy.service.apihttp.info.request.okhttp.interceptor.HttpLoggingInterceptor
import com.proxy.service.apihttp.info.request.okhttp.interceptor.NetworkInterceptor
import com.proxy.service.apihttp.info.request.okhttp.interceptor.RetryWithDelayInterceptor
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor

/**
 * @author: cangHX
 * @data: 2024/12/17 18:11
 * @desc:
 */
class OkhttpConfigImpl(
    private val config: RequestConfig
): IOkhttpConfig {
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

    override fun getInterceptor(): MutableList<Interceptor> {
        val list = ArrayList<Interceptor>()
        list.add(BaseUrlInterceptor(config.getBaseUrl()))
        list.addAll(config.getInterceptor())
        list.add(RetryWithDelayInterceptor())
        list.add(ExactTimeOutInterceptor())
        list.add(HttpLoggingInterceptor())
        list.add(NetworkInterceptor())
        return list
    }

    override fun getNetworkInterceptor(): MutableList<Interceptor> {
        return config.getNetworkInterceptor()
    }

    override fun getEventListener(): EventListener? {
        return config.getEventListener()
    }

    override fun getDns(): Dns? {
        return config.getDns()
    }

    override fun getServerCerAssetsName(): String {
        return config.getServerCerAssetsName()
    }

    override fun getClientCerAssetsName(): String {
        return config.getClientCerAssetsName()
    }

    override fun getClientCerPassWord(): String {
        return config.getClientCerPassWord()
    }

    override fun getMaxRequest(): Int {
        return config.getMaxRequest()
    }
}