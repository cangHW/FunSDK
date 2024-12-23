package com.proxy.service.apihttp.base.upload.config

import com.proxy.service.apihttp.base.upload.config.builder.IUploadConfigBuilder
import com.proxy.service.apihttp.base.upload.config.builder.IUploadConfigBuilderGet
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/12/17 15:20
 * @desc:
 */
class UploadConfig private constructor(
    private val builder: IUploadConfigBuilderGet
) : IUploadConfigBuilderGet {

    override fun getConnectTimeOut(): Long {
        return builder.getConnectTimeOut()
    }

    override fun getInterceptor(): MutableList<Interceptor> {
        return builder.getInterceptor()
    }

    override fun getNetworkInterceptor(): MutableList<Interceptor> {
        return builder.getNetworkInterceptor()
    }

    override fun getEventListener(): EventListener? {
        return builder.getEventListener()
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

    override fun getMaxTask(): Int {
        return builder.getMaxTask()
    }

    companion object {

        fun builder(): IUploadConfigBuilder {
            return Builder()
        }

    }

    private class Builder : IUploadConfigBuilder, IUploadConfigBuilderGet {

        private var connectTimeOut: Long = 10 * 1000

        private var interceptors: MutableList<Interceptor> = ArrayList()
        private var networkInterceptors: MutableList<Interceptor> = ArrayList()
        private var eventListener: EventListener? = null

        private var dns: Dns? = null

        private var serverCerAssetsName: String = ""
        private var clientCerAssetsName: String = ""
        private var clientCerPassWord: String = ""

        private var maxTasks = 3

        override fun setConnectTimeOut(timeout: Long, unit: TimeUnit): IUploadConfigBuilder {
            this.connectTimeOut = unit.toMillis(timeout)
            return this
        }

        override fun addInterceptor(interceptor: Interceptor): IUploadConfigBuilder {
            this.interceptors.add(interceptor)
            return this
        }

        override fun addNetworkInterceptor(interceptor: Interceptor): IUploadConfigBuilder {
            this.networkInterceptors.add(interceptor)
            return this
        }

        override fun addEventListener(eventListener: EventListener): IUploadConfigBuilder {
            this.eventListener = eventListener
            return this
        }

        override fun setDns(dns: Dns): IUploadConfigBuilder {
            this.dns = dns
            return this
        }

        override fun setSslSocket(serverCerAssetsName: String): IUploadConfigBuilder {
            this.serverCerAssetsName = serverCerAssetsName
            return this
        }

        override fun setSslSocket(
            serverCerAssetsName: String,
            clientCerAssetsName: String,
            clientCerPassWord: String
        ): IUploadConfigBuilder {
            this.serverCerAssetsName = serverCerAssetsName
            this.clientCerAssetsName = clientCerAssetsName
            this.clientCerPassWord = clientCerPassWord
            return this
        }

        override fun setMaxTask(maxTasks: Int): IUploadConfigBuilder {
            this.maxTasks = maxTasks
            return this
        }

        override fun build(): UploadConfig {
            return UploadConfig(this)
        }

        override fun getConnectTimeOut(): Long {
            return connectTimeOut
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

        override fun getServerCerAssetsName(): String {
            return serverCerAssetsName
        }

        override fun getClientCerAssetsName(): String {
            return clientCerAssetsName
        }

        override fun getClientCerPassWord(): String {
            return clientCerPassWord
        }

        override fun getMaxTask(): Int {
            return maxTasks
        }
    }
}