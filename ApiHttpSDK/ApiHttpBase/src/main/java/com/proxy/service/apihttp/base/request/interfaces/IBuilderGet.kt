package com.proxy.service.apihttp.base.request.interfaces

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
interface IBuilderGet {

    fun getBaseUrl(): String

    fun getConnectTimeOut(): Long

    fun getWriteTimeOut(): Long

    fun getReadTimeOut(): Long

    fun getCacheMaxSize(): Long

    fun getCacheDir(): String

    fun getInterceptor(): MutableList<Interceptor>

    fun getNetworkInterceptor(): MutableList<Interceptor>

    fun getEventListener(): MutableList<EventListener>

    fun getConverterFactory(): MutableList<Converter.Factory>

    fun getCallAdapterFactory(): MutableList<CallAdapter.Factory>

    fun getDns(): Dns?

    fun getServerCerAssetsName(): String

    fun getClientCerAssetsName(): String

    fun getClientCerPassWord(): String

    fun getMaxRequest(): Int

}