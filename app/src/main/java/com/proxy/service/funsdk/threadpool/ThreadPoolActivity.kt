package com.proxy.service.funsdk.threadpool

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.funsdk.R
import com.proxy.service.threadpool.base.thread.callback.OnFailedCallback
import com.proxy.service.threadpool.base.thread.callback.OnSuccessCallback
import com.proxy.service.threadpool.base.thread.controller.ITaskDisposable
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/5/27 17:30
 * @desc:
 */
class ThreadPoolActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ThreadPoolActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_pool)
    }

    private var disposable: ITaskDisposable? = null

    fun onClick(view: View) {
        when (view.id) {
            R.id.run_normal_task -> {
                CsTask.call(object : ICallable<String> {
                    override fun accept(): String {
                        CsLogger.i("accept")
                        return "sssss"
                    }
                })?.mainThread()
                    ?.doOnNext(object : IConsumer<String> {
                        override fun accept(value: String) {
                            CsLogger.i("value = $value \n ")
                        }
                    })?.setOnSuccessCallback(object : OnSuccessCallback {
                        override fun onCallback() {
                        }
                    })
                    ?.start()
            }

            R.id.run_block_task -> {
                CsLogger.i("start")
                val response = CsTask.ioThread()
                    ?.call(object : ICallable<String> {
                        override fun accept(): String {
                            return "ssss"
                        }
                    })?.delay(2000, TimeUnit.MILLISECONDS)
                    ?.doOnNext(object : IConsumer<String> {
                        override fun accept(value: String) {
                            CsLogger.i("accept value = $value")
                            throw NullPointerException("sssss")
                        }
                    })
                    ?.timeout(1000, TimeUnit.MILLISECONDS)
                    ?.setOnFailedCallback(object : OnFailedCallback {
                        override fun onCallback(throwable: Throwable) {
                            CsLogger.e(throwable)
                        }
                    })
                    ?.blockGetLast()
                CsLogger.i("end response = $response")
            }

            R.id.run_delay_task -> {
                CsLogger.i("start")
                val response = CsTask.delay(2, TimeUnit.SECONDS)?.doOnNext(object :
                    IConsumer<Long> {
                    override fun accept(value: Long) {
                        CsLogger.i("accept value = $value")
                    }
                })?.blockGetLast()
                CsLogger.i("end response = $response")
            }

            R.id.run_interval_task -> {
                CsTask.interval(0, 1000, TimeUnit.MILLISECONDS)
                    ?.doOnNext(object : IConsumer<Long> {
                        override fun accept(value: Long) {
                            CsLogger.i("accept value = $value, time = ${System.currentTimeMillis()}")
                        }
                    })?.start()
            }

            R.id.run_repeat_task -> {
                disposable?.dispose()
                disposable = CsTask.call(object : ICallable<String> {
                    override fun accept(): String {
                        return "sss"
                    }
                })?.doOnNext(object : IConsumer<String> {
                    override fun accept(value: String) {
                        throw NullPointerException("qqqqqqqq")
                    }
                })
                    ?.repeat(3)
                    ?.setOnFailedCallback(object : OnFailedCallback {
                        override fun onCallback(throwable: Throwable) {
                            CsLogger.i(throwable)
                        }
                    })
                    ?.start()
            }
        }
    }

}