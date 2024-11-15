package com.proxy.service.apihttp.base.request.config

import com.proxy.service.apihttp.base.request.interfaces.IBuilder
import com.proxy.service.apihttp.base.request.interfaces.IBuilderGet
import com.proxy.service.core.framework.convert.CsStorageUnit
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/5/21 16:59
 * @desc:
 */
class RequestConfig private constructor(private val builder: IBuilderGet) : IBuilderGet {
    override fun getBaseUrl(): String {
        return builder.getBaseUrl()
    }

    override fun getConnectTimeOut(): Long {
        return builder.getConnectTimeOut()
    }

    override fun getWriteTimeOut(): Long {
        return builder.getWriteTimeOut()
    }

    override fun getReadTimeOut(): Long {
        return builder.getReadTimeOut()
    }

    override fun getCacheMaxSize(): Long {
        return builder.getCacheMaxSize()
    }

    override fun getCacheDir(): String {
        return builder.getCacheDir()
    }

    override fun getInterceptor(): MutableList<Interceptor> {
        return builder.getInterceptor()
    }

    override fun getNetworkInterceptor(): MutableList<Interceptor> {
        return builder.getNetworkInterceptor()
    }

    override fun getEventListener(): MutableList<EventListener> {
        return builder.getEventListener()
    }

    override fun getConverterFactory(): MutableList<Converter.Factory> {
        return builder.getConverterFactory()
    }

    override fun getCallAdapterFactory(): MutableList<CallAdapter.Factory> {
        return builder.getCallAdapterFactory()
    }

    override fun getDns(): Dns? {
        return builder.getDns()
    }

    override fun getServerCerAssetsName(): String {
        return builder.getServerCerAssetsName()
    }

    override fun getClientCerAssetsName(): String {
        return builder.getClientCerAssetsName()
    }

    override fun getClientCerPassWord(): String {
        return builder.getClientCerPassWord()
    }

    override fun getMaxRequest(): Int {
        return builder.getMaxRequest()
    }

    companion object {
        fun builder(baseUrl: String): IBuilder {
            return Builder(baseUrl)
        }
    }

    private class Builder(baseUrl: String) : IBuilder, IBuilderGet {

        companion object {
            private const val TIMEOUT_MIN: Long = 5 * 1000
        }

        private var baseUrl: String = ""

        private var connectTimeOut: Long = 30 * 1000
        private var writeTimeOut: Long = 30 * 1000
        private var readTimeOut: Long = 30 * 1000

        private var cacheDir: String = ""
        private var cacheMaxSize: Long = 1024 * 1024

        private var interceptors: MutableList<Interceptor> = ArrayList()
        private var networkInterceptors: MutableList<Interceptor> = ArrayList()
        private var eventListeners: MutableList<EventListener> = ArrayList()
        private var converterFactory: MutableList<Converter.Factory> = ArrayList()
        private var callAdapterFactory: MutableList<CallAdapter.Factory> = ArrayList()

        private var dns: Dns?=null

        private var serverCerAssetsName: String = ""
        private var clientCerAssetsName: String = ""
        private var clientCerPassWord: String = ""

        private var maxRequests = 4

        init {
            if (baseUrl.isNotBlank()) {
                this.baseUrl = baseUrl
            }
        }

        override fun setConnectTimeOut(timeout: Long, unit: TimeUnit): Builder {
            unit.toMillis(timeout).let {
                connectTimeOut = if (it > TIMEOUT_MIN) {
                    it
                } else {
                    TIMEOUT_MIN
                }
            }
            return this
        }

        override fun setWriteTimeOut(timeout: Long, unit: TimeUnit): Builder {
            unit.toMillis(timeout).let {
                writeTimeOut = if (it > TIMEOUT_MIN) {
                    it
                } else {
                    TIMEOUT_MIN
                }
            }
            return this
        }

        override fun setReadTimeOut(timeout: Long, unit: TimeUnit): Builder {
            unit.toMillis(timeout).let {
                readTimeOut = if (it > TIMEOUT_MIN) {
                    it
                } else {
                    TIMEOUT_MIN
                }
            }
            return this
        }

        override fun setCache(cacheMaxSize: Long, cacheSizeUnit: CsStorageUnit, cacheDir: String): Builder {
            cacheSizeUnit.toBLong(cacheMaxSize).let {
                this.cacheMaxSize = if (it < 0) {
                    0
                } else {
                    it
                }
            }
            this.cacheDir = cacheDir
            return this
        }

        override fun addInterceptor(interceptor: Interceptor): Builder {
            interceptors.add(interceptor)
            return this
        }

        override fun addNetworkInterceptor(interceptor: Interceptor): Builder {
            networkInterceptors.add(interceptor)
            return this
        }

        override fun addEventListener(eventListener: EventListener): IBuilder {
            eventListeners.add(eventListener)
            return this
        }

        override fun addConverterFactory(factory: Converter.Factory): IBuilder {
            converterFactory.add(factory)
            return this
        }

        override fun addCallAdapterFactory(factory: CallAdapter.Factory): IBuilder {
            callAdapterFactory.add(factory)
            return this
        }

        override fun setDns(dns: Dns): IBuilder {
            this.dns = dns
            return this
        }

        override fun setSslSocket(serverCerAssetsName: String): Builder {
            this.serverCerAssetsName = serverCerAssetsName
            this.clientCerAssetsName = ""
            this.clientCerPassWord = ""
            return this
        }

        override fun setSslSocket(
            serverCerAssetsName: String,
            clientCerAssetsName: String,
            clientCerPassWord: String
        ): Builder {
            this.serverCerAssetsName = serverCerAssetsName
            this.clientCerAssetsName = clientCerAssetsName
            this.clientCerPassWord = clientCerPassWord
            return this
        }

        override fun setMaxRequest(maxRequests: Int): Builder {
            if (maxRequests > 4) {
                this.maxRequests = maxRequests
            }
            return this
        }

        override fun build(): RequestConfig {
            return RequestConfig(this)
        }

        override fun getBaseUrl(): String {
            return baseUrl
        }

        override fun getConnectTimeOut(): Long {
            return connectTimeOut
        }

        override fun getWriteTimeOut(): Long {
            return writeTimeOut
        }

        override fun getReadTimeOut(): Long {
            return readTimeOut
        }

        override fun getCacheMaxSize(): Long {
            return cacheMaxSize
        }

        override fun getCacheDir(): String {
            return cacheDir
        }

        override fun getInterceptor(): MutableList<Interceptor> {
            return interceptors
        }

        override fun getNetworkInterceptor(): MutableList<Interceptor> {
            return networkInterceptors
        }

        override fun getEventListener(): MutableList<EventListener> {
            return eventListeners
        }

        override fun getConverterFactory(): MutableList<Converter.Factory> {
            return converterFactory
        }

        override fun getCallAdapterFactory(): MutableList<CallAdapter.Factory> {
            return callAdapterFactory
        }

        override fun getDns(): Dns? {
            return this.dns
        }

        override fun getServerCerAssetsName(): String {
            return serverCerAssetsName
        }

        override fun getClientCerAssetsName(): String {
            return clientCerAssetsName
        }

        override fun getClientCerPassWord(): String {
            return clientCerPassWord
        }

        override fun getMaxRequest(): Int {
            return maxRequests
        }

    }

}