package com.proxy.service.apihttp.base.upload.config.builder

import com.proxy.service.apihttp.base.request.config.builder.IRequestConfigBuilder
import com.proxy.service.apihttp.base.upload.config.UploadConfig
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/12/17 15:20
 * @desc:
 */
interface IUploadConfigBuilder {

    /**
     * 设置连接超时时间
     * */
    fun setConnectTimeOut(timeout: Long, unit: TimeUnit): IUploadConfigBuilder

    /**
     * 添加应用级别拦截器
     * */
    fun addInterceptor(interceptor: Interceptor): IUploadConfigBuilder

    /**
     * 添加网络级别拦截器
     * */
    fun addNetworkInterceptor(interceptor: Interceptor): IUploadConfigBuilder

    /**
     * 添加事件节点回调
     * */
    fun addEventListener(eventListener: EventListener): IUploadConfigBuilder

    /**
     * 设置 dns 拦截
     * */
    fun setDns(dns: Dns): IUploadConfigBuilder

    /**
     * 设置 ssl 证书校验为服务端单向校验
     * */
    fun setSslSocket(serverCerAssetsName: String): IUploadConfigBuilder

    /**
     * 设置 ssl 证书校验为客户端、服务端双向校验
     * */
    fun setSslSocket(
        serverCerAssetsName: String,
        clientCerAssetsName: String,
        clientCerPassWord: String
    ): IUploadConfigBuilder

    /**
     * 设置最大并发请求数
     * */
    fun setMaxTask(maxTasks: Int): IUploadConfigBuilder

    /**
     * 创建配置
     * */
    fun build(): UploadConfig

}