package com.proxy.service.apihttp.base.upload.config.builder

import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor

/**
 * @author: cangHX
 * @data: 2024/12/17 15:20
 * @desc:
 */
interface IUploadConfigBuilderGet {

    /**
     * 获取连接超时时间
     * */
    fun getConnectTimeOut(): Long

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
     * 获取最大并发请求数
     * */
    fun getMaxTask(): Int

}