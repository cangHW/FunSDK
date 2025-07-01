package com.proxy.service.apihttp.info.request.okhttp.interceptor

import com.proxy.service.apihttp.base.constants.ApiConstants
import com.proxy.service.apihttp.base.request.annotation.CsBaseUrl
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
class BaseUrlInterceptor(private val baseUrl: String) : Interceptor {

    companion object {
        private const val TAG = "${ApiConstants.LOG_REQUEST_TAG_START}BaseUrl"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        try {
            request.tag(Invocation::class.java)?.method()?.getAnnotation(CsBaseUrl::class.java)
                ?.let {
                    if (it.baseUrl.isNotBlank()) {
                        request.url.toString().replace(baseUrl, it.baseUrl).toHttpUrlOrNull()
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