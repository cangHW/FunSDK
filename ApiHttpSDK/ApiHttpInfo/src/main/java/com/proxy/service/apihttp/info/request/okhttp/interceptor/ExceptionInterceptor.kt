package com.proxy.service.apihttp.info.request.okhttp.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author: cangHX
 * @data: 2025/8/18 19:51
 * @desc:
 */
class ExceptionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            return chain.proceed(request)
        } catch (throwable: Throwable) {
            throw IOException(throwable)
        }
    }
}