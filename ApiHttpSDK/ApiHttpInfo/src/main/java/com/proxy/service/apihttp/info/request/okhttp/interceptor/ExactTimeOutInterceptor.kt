package com.proxy.service.apihttp.info.request.okhttp.interceptor

import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.request.annotation.CsExactTimeOut
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.callback.OnFailedCallback
import com.proxy.service.threadpool.base.thread.task.ICallable
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import java.net.SocketTimeoutException

/**
 * @author: cangHX
 * @data: 2024/5/23 18:43
 * @desc:
 */
class ExactTimeOutInterceptor : Interceptor {

    companion object {
        private const val TAG = "${Constants.LOG_REQUEST_TAG_START}ExactTimeOut"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        var response: Response? = null
        var error: Throwable? = null
        try {
            request.tag(Invocation::class.java)?.method()?.getAnnotation(CsExactTimeOut::class.java)
                ?.let {
                    CsLogger.tag(TAG).d("ExactTimeOut = ${it.unit.toMillis(it.timeout)}毫秒. url = ${request.url}")
                    response = CsTask.ioThread()?.call(object : ICallable<Response> {
                        override fun accept(): Response {
                            return chain.proceed(request)
                        }
                    })?.timeout(it.timeout, it.unit)
                        ?.setOnFailedCallback(object : OnFailedCallback {
                            override fun onCallback(throwable: Throwable) {
                                CsLogger.tag(TAG).d("timeout. url = ${request.url}")
                                error = throwable
                            }
                        })
                        ?.blockGetFirst()
                }
        } catch (throwable: Throwable) {
            error = throwable
            CsLogger.tag(TAG).e(throwable)
        }

        val rep = response
        if (rep != null) {
            return rep
        }

        if (error != null) {
            throw SocketTimeoutException(error?.message ?: "timeout. url = ${request.url}")
        }

        return chain.proceed(request)
    }
}