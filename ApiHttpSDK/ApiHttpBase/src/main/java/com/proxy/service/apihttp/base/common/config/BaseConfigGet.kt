package com.proxy.service.apihttp.base.common.config

import com.proxy.service.apihttp.base.common.config.base.IBaseConfigGet
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.X509TrustManager

/**
 * @author: cangHX
 * @date: 2025/3/27 20:57
 * @desc:
 */
abstract class BaseConfigGet(
    private val builder: IBaseConfigGet
) : IBaseConfigGet {

    override fun getInterceptors(): MutableList<Interceptor> {
        return builder.getInterceptors()
    }

    override fun getNetworkInterceptors(): MutableList<Interceptor> {
        return builder.getNetworkInterceptors()
    }

    override fun getEventListener(): EventListener? {
        return builder.getEventListener()
    }

    override fun getDns(): Dns? {
        return builder.getDns()
    }

    override fun getServerCerAssetsName(): String? {
        return builder.getServerCerAssetsName()
    }

    override fun getClientCerAssetsName(): String? {
        return builder.getClientCerAssetsName()
    }

    override fun getClientCerPassWord(): String? {
        return builder.getClientCerPassWord()
    }

    override fun getX509TrustManager(): X509TrustManager? {
        return builder.getX509TrustManager()
    }

    override fun getHostnameVerifier(): HostnameVerifier? {
        return builder.getHostnameVerifier()
    }

}