package com.proxy.service.apihttp.info.upload.manager.impl

import com.proxy.service.apihttp.base.upload.config.UploadConfig
import com.proxy.service.apihttp.info.common.okhttp.IOkhttpConfig
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.X509TrustManager

/**
 * @author: cangHX
 * @data: 2024/12/19 20:13
 * @desc:
 */
class OkhttpConfigImpl : IOkhttpConfig {

    companion object {
        val instance by lazy {
            OkhttpConfigImpl()
        }
    }

    private var config: UploadConfig? = null

    fun setUploadConfig(config: UploadConfig) {
        this.config = config
    }

    override fun getConnectTimeOut(): Long? {
        return config?.getConnectTimeOut()
    }

    override fun getWriteTimeOut(): Long? {
        return null
    }

    override fun getReadTimeOut(): Long? {
        return null
    }

    override fun getCacheMaxSize(): Long? {
        return null
    }

    override fun getCacheDir(): String? {
        return null
    }

    override fun getInterceptor(): MutableList<Interceptor> {
        return config?.getInterceptor() ?: arrayListOf()
    }

    override fun getNetworkInterceptor(): MutableList<Interceptor> {
        return config?.getNetworkInterceptor() ?: arrayListOf()
    }

    override fun getEventListener(): EventListener? {
        return config?.getEventListener()
    }

    override fun getDns(): Dns? {
        return config?.getDns()
    }

    override fun getServerCerAssetsName(): String? {
        return config?.getServerCerAssetsName()
    }

    override fun getClientCerAssetsName(): String? {
        return config?.getClientCerAssetsName()
    }

    override fun getClientCerPassWord(): String? {
        return config?.getClientCerPassWord()
    }

    override fun getX509TrustManager(): X509TrustManager? {
        return config?.getX509TrustManager()
    }

    override fun getHostnameVerifier(): HostnameVerifier? {
        return config?.getHostnameVerifier()
    }

    override fun getMaxRequest(): Int? {
        return null
    }
}