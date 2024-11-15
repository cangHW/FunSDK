package com.proxy.service.apihttp.info.request.okhttp.interceptor

import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.request.annotation.CsBaseUrl
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.core.framework.data.log.CsLogger
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

    companion object {
        private const val TAG = "${Constants.LOG_REQUEST_TAG_START}BaseUrl"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        try {
            request.tag(Invocation::class.java)?.method()?.getAnnotation(CsBaseUrl::class.java)
                ?.let {
                    if (it.baseUrl.isNotBlank()) {
                        val dUrl = Config.getRequestConfig().getBaseUrl()
                        request.url.toString().replace(dUrl, it.baseUrl).toHttpUrlOrNull()
                            ?.let { httpUrl ->
                                request = request.newBuilder().url(httpUrl).build()
                            }
                    }
                }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }

        return chain.proceed(request)
    }
}