package com.proxy.service.apihttp.webserver

import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.api.CloudSystem
import com.proxy.service.api.service.listener.Converter
import com.proxy.service.apihttp.base.request.RequestService
import com.proxy.service.apihttp.base.request.config.RequestConfig
import com.proxy.service.apihttp.webserver.interceptor.HttpProxyInterceptor
import com.proxy.service.apihttp.webserver.plugin.ApiHttpWebServerPlugin
import com.proxy.service.core.application.base.CsBaseConfig
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.service.webserver.CsWebServer

/**
 * @author: cangHX
 * @date: 2026/6/21 16:47
 * @desc:
 */
@CloudApiService(serviceTag = "${CoreConfig.SERVICE_TAG_CONFIG_PREFIX}/ApiHttpWebServer")
class ApiHttpWebServerConfig : CsBaseConfig(), Converter<RequestService> {

    override fun onCreate(application: Application, isDebug: Boolean) {
        CloudSystem.addConverter(RequestService::class.java, this)
        CsWebServer.registerPlugin(ApiHttpWebServerPlugin())
    }

    override fun converter(t: RequestService): RequestService {
        return RequestServiceImpl(t)
    }

    private class RequestServiceImpl(
        private val service: RequestService
    ) : RequestService by service{
        override fun init(config: RequestConfig) {
            val newConfig = config.newBuilder()
                .addInterceptor(HttpProxyInterceptor())
                .build()
            service.init(newConfig)
        }
    }
}