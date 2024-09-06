package com.proxy.service.apihttp.base.interfaces

import com.proxy.service.apihttp.base.init.ApiConfig
import com.proxy.service.core.framework.convert.CsStorageUnit
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/5/21 20:43
 * @desc:
 */
interface IBuilder {

    /**
     * 设置连接超时时间
     * */
    fun setConnectTimeOut(timeout: Long, unit: TimeUnit): IBuilder

    /**
     * 设置写入超时时间
     * */
    fun setWriteTimeOut(timeout: Long, unit: TimeUnit): IBuilder

    /**
     * 设置读取超时时间
     * */
    fun setReadTimeOut(timeout: Long, unit: TimeUnit): IBuilder

    /**
     * 设置缓存位置以及缓存大小
     * */
    fun setCache(cacheMaxSize: Long, cacheSizeUnit: CsStorageUnit, cacheDir: String): IBuilder

    /**
     * 添加应用级别拦截器
     * */
    fun addInterceptor(interceptor: Interceptor): IBuilder

    /**
     * 添加网络级别拦截器
     * */
    fun addNetworkInterceptor(interceptor: Interceptor): IBuilder

    /**
     * 添加事件节点回调
     * */
    fun addEventListener(eventListener: EventListener): IBuilder

    /**
     * 添加返回值转换器
     * */
    fun addConverterFactory(factory: Converter.Factory): IBuilder

    /**
     * 添加返回管理器
     * */
    fun addCallAdapterFactory(factory: CallAdapter.Factory): IBuilder

    /**
     * 设置 dns 拦截
     * */
    fun setDns(dns: Dns): IBuilder

    /**
     * 设置 ssl 证书校验为服务端单向校验
     * */
    fun setSslSocket(serverCerAssetsName: String): IBuilder

    /**
     * 设置 ssl 证书校验为客户端、服务端双向校验
     * */
    fun setSslSocket(
        serverCerAssetsName: String,
        clientCerAssetsName: String,
        clientCerPassWord: String
    ): IBuilder

    /**
     * 设置最大并发请求数
     * */
    fun setMaxRequest(maxRequests: Int): IBuilder

    /**
     * 创建配置
     * */
    fun build(): ApiConfig

}