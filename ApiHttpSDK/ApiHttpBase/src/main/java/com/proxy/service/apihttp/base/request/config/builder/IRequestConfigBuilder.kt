package com.proxy.service.apihttp.base.request.config.builder

import com.proxy.service.apihttp.base.common.config.base.IBaseConfig
import com.proxy.service.apihttp.base.request.config.RequestConfig
import com.proxy.service.core.framework.convert.CsStorageUnit
import retrofit2.CallAdapter
import retrofit2.Converter
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/5/21 20:43
 * @desc:
 */
interface IRequestConfigBuilder : IBaseConfig<IRequestConfigBuilder> {

    /**
     * 设置连接超时时间
     * */
    fun setConnectTimeOut(timeout: Long, unit: TimeUnit): IRequestConfigBuilder

    /**
     * 设置写入超时时间
     * */
    fun setWriteTimeOut(timeout: Long, unit: TimeUnit): IRequestConfigBuilder

    /**
     * 设置读取超时时间
     * */
    fun setReadTimeOut(timeout: Long, unit: TimeUnit): IRequestConfigBuilder

    /**
     * 设置缓存位置以及缓存大小
     * */
    fun setCache(
        cacheMaxSize: Long,
        cacheSizeUnit: CsStorageUnit,
        cacheDir: String
    ): IRequestConfigBuilder

    /**
     * 添加返回值转换器
     * */
    fun addConverterFactory(factory: Converter.Factory): IRequestConfigBuilder

    /**
     * 添加返回管理器
     * */
    fun addCallAdapterFactory(factory: CallAdapter.Factory): IRequestConfigBuilder

    /**
     * 设置最大并发请求数
     * */
    fun setMaxRequest(maxRequests: Int): IRequestConfigBuilder

    /**
     * 创建配置
     * */
    fun build(): RequestConfig

}