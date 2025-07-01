package com.proxy.service.apihttp.info.request.okhttp.interceptor

import com.proxy.service.apihttp.base.constants.ApiConstants
import com.proxy.service.apihttp.base.request.annotation.CsRetryWithDelay
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.callback.MultiRunnableEmitter
import com.proxy.service.threadpool.base.thread.task.IMultiRunnable
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import java.io.IOException

/**
 * @author: cangHX
 * @data: 2024/6/17 17:58
 * @desc:
 */
class RetryWithDelayInterceptor : Interceptor {

    companion object {
        private const val TAG = "${ApiConstants.LOG_REQUEST_TAG_START}RetryWithDelay"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        var error: Throwable? = null
        var response: Response? = try {
            chain.proceed(request)
        } catch (throwable: Throwable) {
            error = throwable
            CsLogger.tag(TAG).e(throwable)
            null
        }

        if (response?.isSuccessful == true) {
            return response
        }

        try {
            request.tag(Invocation::class.java)
                ?.method()
                ?.getAnnotation(CsRetryWithDelay::class.java)
                ?.let {
                    val result = CsTask.ioThread()
                        ?.call(object : IMultiRunnable<Response> {
                            override fun accept(emitter: MultiRunnableEmitter<Response>) {
                                var count = 0
                                val maxCount = Math.max(it.retryCount, 0)
                                val sleepTime = it.unit.toMillis(it.delay)

                                while (count < maxCount) {

                                    try {
                                        Thread.sleep(sleepTime)
                                    } catch (throwable: Throwable) {
                                        CsLogger.tag(TAG).d(throwable)
                                    }

                                    try {
                                        CsLogger.tag(TAG)
                                            .d("retry = ${count + 1}, max = $maxCount, url = ${request.url}")
                                        val rep = chain.proceed(request)
                                        if (rep.isSuccessful) {
                                            emitter.onNext(rep)
                                            emitter.onComplete()
                                            return
                                        }
                                    } catch (throwable: Throwable) {
                                        CsLogger.tag(TAG).d(throwable)
                                    }

                                    count++
                                }

                                emitter.onError(IOException("The retry times are used up. url = ${request.url}"))
                                emitter.onComplete()
                            }
                        })
                        ?.blockGetFirst()

                    if (result != null) {
                        response = result
                    }
                }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }

        if (response != null) {
            return response!!
        }

        error?.let {
            throw it
        }

        throw IOException("The retry times are used up. url = ${request.url}")
    }
}