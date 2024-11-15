package com.proxy.service.apihttp.info.request.okhttp

import android.text.TextUtils
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.request.okhttp.event.EventListenerImpl
import com.proxy.service.apihttp.info.request.okhttp.interceptor.BaseUrlInterceptor
import com.proxy.service.apihttp.info.request.okhttp.interceptor.ExactTimeOutInterceptor
import com.proxy.service.apihttp.info.request.okhttp.interceptor.HttpLoggingInterceptor
import com.proxy.service.apihttp.info.request.okhttp.interceptor.NetworkInterceptor
import com.proxy.service.apihttp.info.request.okhttp.interceptor.RetryWithDelayInterceptor
import com.proxy.service.apihttp.info.request.okhttp.ssl.TrustCerManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/5/21 17:57
 * @desc:
 */
object OkhttpManager {

    fun create(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val config = Config.getRequestConfig()

        builder.connectTimeout(config.getConnectTimeOut(), TimeUnit.MILLISECONDS)
        builder.writeTimeout(config.getWriteTimeOut(), TimeUnit.MILLISECONDS)
        builder.readTimeout(config.getReadTimeOut(), TimeUnit.MILLISECONDS)

        if (!TextUtils.isEmpty(config.getCacheDir())) {
            builder.cache(Cache(File(config.getCacheDir()), config.getCacheMaxSize()))
        }

        builder.addInterceptor(BaseUrlInterceptor())
        config.getInterceptor().forEach {
            builder.addInterceptor(it)
        }
        builder.addInterceptor(RetryWithDelayInterceptor())
        builder.addInterceptor(ExactTimeOutInterceptor())
        builder.addInterceptor(HttpLoggingInterceptor())
        builder.addInterceptor(NetworkInterceptor())

        config.getNetworkInterceptor().forEach {
            builder.addNetworkInterceptor(it)
        }

        builder.eventListener(EventListenerImpl.getInstance())
        EventListenerImpl.getInstance().setEventListeners(config.getEventListener())

        config.getDns()?.let {
            builder.dns(it)
        }

        TrustCerManager.getSSLSocketFactory(
            config.getServerCerAssetsName(),
            config.getClientCerAssetsName(),
            config.getClientCerPassWord()
        )?.let {
            builder.sslSocketFactory(it, TrustCerManager())
        }

        if (!Config.isDebug()) {
            builder.proxy(Proxy.NO_PROXY)
        }

        val client = builder.build()
        client.dispatcher.maxRequests = config.getMaxRequest()

        return client
    }

}