package com.proxy.service.apihttp.info.okhttp.interceptor

import com.proxy.service.apihttp.base.annotation.CsBaseUrl
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.core.framework.log.CsLogger
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation


/**
 * @author: cangHX
 * @data: 2024/5/23 16:54
 * @desc:
 */
class BaseUrlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        try {
            request.tag(Invocation::class.java)?.method()?.getAnnotation(CsBaseUrl::class.java)
                ?.let {
                    if (it.baseUrl.isNotBlank()) {
                        val dUrl = Config.getApiConfig().getBaseUrl()
                        request.url.toString().replace(dUrl, it.baseUrl).toHttpUrlOrNull()
                            ?.let { httpUrl ->
                                request = request.newBuilder().url(httpUrl).build()
                            }
                    }
                }
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        }

        return chain.proceed(request)
    }
}