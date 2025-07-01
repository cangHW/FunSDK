package com.proxy.service.apihttp.info.request.okhttp.interceptor

import com.proxy.service.apihttp.base.constants.ApiConstants
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.net.CsNetManager
import okhttp3.Interceptor
import okhttp3.Response
import java.net.SocketTimeoutException

/**
 * @author: cangHX
 * @data: 2024/6/20 09:59
 * @desc:
 */
class NetworkInterceptor: Interceptor {

    companion object {
        private const val TAG = "${ApiConstants.LOG_REQUEST_TAG_START}Network"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (!CsNetManager.isAvailable()){
            CsLogger.tag(TAG).d("Network unavailable. url = ${request.url}")
            throw SocketTimeoutException("Network unavailable. url = ${request.url}")
        }

        return chain.proceed(request)
    }
}