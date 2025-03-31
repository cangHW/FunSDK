package com.proxy.service.apihttp.base.common.config

import com.proxy.service.apihttp.base.common.config.base.IBase
import com.proxy.service.apihttp.base.common.config.base.IBaseConfig
import com.proxy.service.apihttp.base.common.config.base.IBaseConfigGet
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.X509TrustManager

/**
 * @author: cangHX
 * @data: 2025/3/27 20:57
 * @desc:
 */
abstract class BaseConfig<T> : IBaseConfig<T>, IBase<T>, IBaseConfigGet {

    private var interceptors: MutableList<Interceptor> = ArrayList()
    private var networkInterceptors: MutableList<Interceptor> = ArrayList()
    private var eventListener: EventListener? = null
    private var dns: Dns? = null

    override fun addInterceptor(interceptor: Interceptor): T {
        interceptors.add(interceptor)
        return getInstance()
    }

    override fun addNetworkInterceptor(interceptor: Interceptor): T {
        networkInterceptors.add(interceptor)
        return getInstance()
    }

    override fun setEventListener(eventListener: EventListener): T {
        this.eventListener = eventListener
        return getInstance()
    }

    override fun setDns(dns: Dns): T {
        this.dns = dns
        return getInstance()
    }

    override fun getInterceptor(): MutableList<Interceptor> {
        return interceptors
    }

    override fun getNetworkInterceptor(): MutableList<Interceptor> {
        return networkInterceptors
    }

    override fun getEventListener(): EventListener? {
        return eventListener
    }

    override fun getDns(): Dns? {
        return dns
    }


    private var serverCerAssetsName: String? = null
    private var clientCerAssetsName: String? = null
    private var clientCerPassWord: String? = null
    private var x509TrustManager: X509TrustManager? = null
    private var hostnameVerifier: HostnameVerifier?=null

    override fun setSslSocket(serverCerAssetsName: String): T {
        this.serverCerAssetsName = serverCerAssetsName
        this.clientCerAssetsName = null
        this.clientCerPassWord = null
        return getInstance()
    }

    override fun setSslSocket(
        serverCerAssetsName: String,
        clientCerAssetsName: String,
        clientCerPassWord: String
    ): T {
        this.serverCerAssetsName = serverCerAssetsName
        this.clientCerAssetsName = clientCerAssetsName
        this.clientCerPassWord = clientCerPassWord
        return getInstance()
    }

    override fun setX509TrustManager(x509TrustManager: X509TrustManager): T {
        this.x509TrustManager = x509TrustManager
        return getInstance()
    }

    override fun setHostnameVerifier(hostnameVerifier: HostnameVerifier): T {
        this.hostnameVerifier = hostnameVerifier
        return getInstance()
    }

    override fun getServerCerAssetsName(): String? {
        return serverCerAssetsName
    }

    override fun getClientCerAssetsName(): String? {
        return clientCerAssetsName
    }

    override fun getClientCerPassWord(): String? {
        return clientCerPassWord
    }

    override fun getX509TrustManager(): X509TrustManager? {
        return x509TrustManager
    }

    override fun getHostnameVerifier(): HostnameVerifier? {
        return hostnameVerifier
    }
}