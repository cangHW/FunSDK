package com.proxy.service.apihttp.info.common.okhttp

import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor

/**
 * @author: cangHX
 * @data: 2024/12/17 17:53
 * @desc:
 */
interface IOkhttpConfig {

    /**
     * 获取连接超时时间
     * */
    fun getConnectTimeOut(): Long?

    /**
     * 获取写入超时时间
     * */
    fun getWriteTimeOut(): Long?

    /**
     * 获取读取超时时间
     * */
    fun getReadTimeOut(): Long?

    /**
     * 获取缓存大小
     * */
    fun getCacheMaxSize(): Long?

    /**
     * 获取缓存路径
     * */
    fun getCacheDir(): String?

    /**
     * 获取应用拦截器
     * */
    fun getInterceptor(): MutableList<Interceptor>

    /**
     * 获取网络拦截器
     * */
    fun getNetworkInterceptor(): MutableList<Interceptor>

    /**
     * 获取事件节点回调
     * */
    fun getEventListener(): EventListener?

    /**
     * 获取 dns
     * */
    fun getDns(): Dns?

    /**
     * 获取服务端证书路径
     * */
    fun getServerCerAssetsName(): String?

    /**
     * 获取客户端证书路径
     * */
    fun getClientCerAssetsName(): String?

    /**
     * 获取客户端证书密钥
     * */
    fun getClientCerPassWord(): String?

    /**
     * 获取最大并发数
     * */
    fun getMaxRequest(): Int?

}