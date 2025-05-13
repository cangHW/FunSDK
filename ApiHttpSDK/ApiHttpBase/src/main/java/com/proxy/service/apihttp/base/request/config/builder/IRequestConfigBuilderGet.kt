package com.proxy.service.apihttp.base.request.config.builder

import com.proxy.service.apihttp.base.common.config.base.IBaseConfigGet
import retrofit2.CallAdapter
import retrofit2.Converter

/**
 * @author: cangHX
 * @data: 2024/5/21 20:43
 * @desc:
 */
interface IRequestConfigBuilderGet : IBaseConfigGet {

    fun getBaseUrl(): String

    /**
     * 获取连接超时时间
     * */
    fun getConnectTimeOut(): Long

    fun getWriteTimeOut(): Long

    fun getReadTimeOut(): Long

    fun getCacheMaxSize(): Long

    fun getCacheDir(): String

    fun getConverterFactory(): MutableList<Converter.Factory>

    fun getCallAdapterFactory(): MutableList<CallAdapter.Factory>

    /**
     * 获取最大并发量
     * */
    fun getMaxRequest(): Int

}