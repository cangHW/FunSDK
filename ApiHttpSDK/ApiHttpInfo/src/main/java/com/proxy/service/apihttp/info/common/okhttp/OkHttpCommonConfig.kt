package com.proxy.service.apihttp.info.common.okhttp

import com.proxy.service.apihttp.base.common.config.base.IBaseConfigGet
import com.proxy.service.apihttp.info.request.okhttp.interceptor.PerformanceInterceptor
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.X509TrustManager

/**
 * @author: cangHX
 * @date: 2026/5/12 16:53
 * @desc:
 */
abstract class OkHttpCommonConfig(
    private val config: IBaseConfigGet
) : IOkHttpConfig {

    override fun getInterceptors(): MutableList<Interceptor> {
        return config.getInterceptors()
    }

    override fun getNetworkInterceptors(): MutableList<Interceptor> {
        return config.getNetworkInterceptors()
    }

    override fun getEventListener(): EventListener? {
        return PerformanceInterceptor(config.getEventListener())
    }

    override fun getDns(): Dns? {
        return config.getDns()
    }

    override fun getFollowRedirects(): Boolean {
        return config.getFollowRedirects()
    }

    override fun getFollowSslRedirects(): Boolean {
        return config.getFollowSslRedirects()
    }

    override fun getServerCerAssetsName(): String? {
        return config.getServerCerAssetsName()
    }

    override fun getClientCerAssetsName(): String? {
        return config.getClientCerAssetsName()
    }

    override fun getClientCerPassWord(): String? {
        return config.getClientCerPassWord()
    }

    override fun getX509TrustManager(): X509TrustManager? {
        return config.getX509TrustManager()
    }

    override fun getHostnameVerifier(): HostnameVerifier? {
        return config.getHostnameVerifier()
    }

}