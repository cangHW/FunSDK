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
 * @data: 2024/12/17 17:51
 * @desc:
 */
object OkhttpFactory {

    /**
     * 获取 OkHttpClient 对象
     * */
    fun create(config: IOkhttpConfig): OkHttpClient {
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

        if (config.getInterceptor().isNotEmpty()) {
            builder.addInterceptor(ExceptionInterceptor())
            config.getInterceptor().forEach {
                builder.addInterceptor(it)
            }
        }

        if (config.getNetworkInterceptor().isNotEmpty()) {
            builder.addNetworkInterceptor(ExceptionInterceptor())
            config.getNetworkInterceptor().forEach {
                builder.addNetworkInterceptor(it)
            }
        }

        config.getEventListener()?.let {
            builder.eventListener(it)
        }

        config.getDns()?.let {
            builder.dns(it)
        }

        TrustCerManager.getSSLSocketFactory(
            config.getServerCerAssetsName(),
            config.getClientCerAssetsName(),
            config.getClientCerPassWord(),
            config.getX509TrustManager()
        )?.let {
            builder.sslSocketFactory(
                it,
                TrustCerManager.getX509TrustManager(config.getX509TrustManager())
            )
        }

        if (!CoreConfig.isDebug) {
            builder.proxy(Proxy.NO_PROXY)
        }

        config.getHostnameVerifier()?.let {
            builder.hostnameVerifier(it)
        }

        val client = builder.build()
        config.getMaxRequest()?.let {
            client.dispatcher.maxRequests = it
        }

        return client
    }

}