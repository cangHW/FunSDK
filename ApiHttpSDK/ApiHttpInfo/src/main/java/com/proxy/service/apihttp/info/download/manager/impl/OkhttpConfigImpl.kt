package com.proxy.service.apihttp.info.download.manager.impl

import com.proxy.service.apihttp.base.download.config.DownloadConfig
import com.proxy.service.apihttp.info.common.okhttp.IOkhttpConfig
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor

/**
 * @author: cangHX
 * @data: 2024/12/17 18:39
 * @desc:
 */
class OkhttpConfigImpl : IOkhttpConfig {

    companion object {
        val instance by lazy {
            OkhttpConfigImpl()
        }
    }

    private var config: DownloadConfig? = null

    fun setDownloadConfig(config: DownloadConfig) {
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
        return config?.getInterceptor() ?: mutableListOf()
    }

    override fun getNetworkInterceptor(): MutableList<Interceptor> {
        return config?.getNetworkInterceptor() ?: mutableListOf()
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

    override fun getMaxRequest(): Int? {
        return null
    }
}