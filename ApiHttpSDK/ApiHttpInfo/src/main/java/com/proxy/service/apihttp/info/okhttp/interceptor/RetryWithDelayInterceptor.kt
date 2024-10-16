package com.proxy.service.apihttp.info.okhttp.interceptor

import com.proxy.service.apihttp.base.annotation.CsRetryWithDelay
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.callback.MultiRunnableEmitter
import com.proxy.service.threadpool.base.thread.callback.OnFailedCallback
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
        private const val TAG = "${Config.LOG_TAG_START}RetryWithDelay"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        var response: Response? = null
        var error: Throwable? = null
        try {
            request.tag(Invocation::class.java)?.method()
                ?.getAnnotation(CsRetryWithDelay::class.java)?.let {
                    response = CsTask.ioThread()?.call(object : IMultiRunnable<Response> {
                        override fun accept(emitter: MultiRunnableEmitter<Response>) {
                            var count = 0
                            val maxCount = if (it.retryCount > 0) {
                                it.retryCount + 1
                            } else {
                                1
                            }
                            val sleepTime = it.unit.toMillis(it.delay)
                            var err: Throwable? = null

                            while (count < maxCount) {
                                try {
                                    CsLogger.tag(TAG).d("retry = $count, max = $maxCount, url = ${request.url}")
                                    val rep = chain.proceed(request)
                                    if (rep.isSuccessful) {
                                        emitter.onNext(rep)
                                        emitter.onComplete()
                                        return
                                    }
                                } catch (throwable: Throwable) {
                                    err = throwable
                                    CsLogger.e(throwable)
                                }

                                try {
                                    Thread.sleep(sleepTime)
                                } catch (throwable: Throwable) {
                                    CsLogger.e(throwable)
                                }

                                count++
                            }

                            emitter.onError(
                                err ?: IOException("The retry times are used up. url = ${request.url}")
                            )
                            emitter.onComplete()
                        }
                    })?.setOnFailedCallback(object : OnFailedCallback {
                        override fun onCallback(throwable: Throwable) {
                            error = throwable
                        }
                    })?.blockGetFirst()
                }
        } catch (throwable: Throwable) {
            error = throwable
            CsLogger.e(throwable)
        }

        val rep = response
        if (rep != null) {
            return rep
        }

        if (error != null) {
            throw IOException(error?.message ?: "The retry times are used up. url = ${request.url}")
        }

        return chain.proceed(request)
    }
}