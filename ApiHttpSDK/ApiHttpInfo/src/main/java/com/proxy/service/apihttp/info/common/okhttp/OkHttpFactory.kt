package com.proxy.service.apihttp.info.common.okhttp

import com.proxy.service.apihttp.info.common.ssl.TrustCerManager
import com.proxy.service.apihttp.info.request.okhttp.interceptor.ExceptionInterceptor
import com.proxy.service.core.constants.CoreConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @date: 2024/12/17 17:51
 * @desc:
 */
object OkHttpFactory {

    /**
     * 获取 OkHttpClient 对象
     * */
    fun create(config: IOkHttpConfig): OkHttpClient {
        val builder = OkHttpClient.Builder()

        config.getConnectTimeOut()?.let {
            builder.connectTimeout(it, TimeUnit.MILLISECONDS)
        }
        config.getWriteTimeOut()?.let {
            builder.writeTimeout(it, TimeUnit.MILLISECONDS)
        }
        config.getReadTimeOut()?.let {
            builder.readTimeout(it, TimeUnit.MILLISECONDS)
        }

        config.getCacheDir()?.let { dir ->
            config.getCacheMaxSize()?.let { size ->
                if (dir.trim().isNotEmpty()) {
                    builder.cache(Cache(File(dir), size))
                }
            }
        }

        builder.addInterceptor(ExceptionInterceptor())
        if (config.getInterceptors().isNotEmpty()) {
            config.getInterceptors().forEach {
                builder.addInterceptor(it)
            }
        }

        builder.addNetworkInterceptor(ExceptionInterceptor())
        if (config.getNetworkInterceptors().isNotEmpty()) {
            config.getNetworkInterceptors().forEach {
                builder.addNetworkInterceptor(it)
            }
        }

        config.getEventListener()?.let {
            builder.eventListener(it)
        }

        config.getDns()?.let {
            builder.dns(it)
        }

        builder.followRedirects(config.getFollowRedirects())
        builder.followSslRedirects(config.getFollowSslRedirects())

        TrustCerManager.parse(builder, config)
        config.getHostnameVerifier()?.let {
            builder.hostnameVerifier(it)
        } ?: let {
            if (CoreConfig.isDebug) {
                builder.hostnameVerifier { _, _ -> true }
            }
        }
        if (!CoreConfig.isDebug) {
            builder.proxy(Proxy.NO_PROXY)
        }

        val client = builder.build()
        config.getMaxRequest()?.let {
            client.dispatcher.maxRequests = it
        }

        return client
    }

}