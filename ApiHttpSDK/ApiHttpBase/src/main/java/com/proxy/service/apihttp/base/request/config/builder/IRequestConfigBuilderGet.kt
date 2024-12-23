package com.proxy.service.apihttp.base.request.config.builder

import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor
import retrofit2.CallAdapter
import retrofit2.Converter

/**
 * @author: cangHX
 * @data: 2024/5/21 20:43
 * @desc:
 */
interface IRequestConfigBuilderGet {

    fun getBaseUrl(): String

    /**
     * 获取连接超时时间
     * */
    fun getConnectTimeOut(): Long

    fun getWriteTimeOut(): Long

    fun getReadTimeOut(): Long

    fun getCacheMaxSize(): Long

    fun getCacheDir(): String

    /**
     * 获取应用级别拦截器
     * */
    fun getInterceptor(): MutableList<Interceptor>

    /**
     * 获取网络级别拦截器
     * */
    fun getNetworkInterceptor(): MutableList<Interceptor>

    /**
     * 获取事件节点回调
     * */
    fun getEventListener(): EventListener?

    fun getConverterFactory(): MutableList<Converter.Factory>

    fun getCallAdapterFactory(): MutableList<CallAdapter.Factory>

    /**
     * 获取 dns 拦截
     * */
    fun getDns(): Dns?

    /**
     * 获取服务端证书路径
     * */
    fun getServerCerAssetsName(): String

    /**
     * 获取客户端证书路径
     * */
    fun getClientCerAssetsName(): String

    /**
     * 获取客户端证书密钥
     * */
    fun getClientCerPassWord(): String

    /**
     * 获取最大并发量
     * */
    fun getMaxRequest(): Int

}