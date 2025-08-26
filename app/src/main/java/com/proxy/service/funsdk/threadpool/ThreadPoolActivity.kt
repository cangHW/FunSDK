package com.proxy.service.funsdk.threadpool

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityThreadPoolBinding
import com.proxy.service.threadpool.base.thread.callback.OnCompleteCallback
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
class ThreadPoolActivity : BaseActivity<ActivityThreadPoolBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ThreadPoolActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private var intervalDisposable: ITaskDisposable? = null

    override fun getViewBinding(inflater: LayoutInflater): ActivityThreadPoolBinding {
        return ActivityThreadPoolBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.run_normal_task -> {
                val time = System.currentTimeMillis()
                binding?.content?.addData(
                    "task",
                    "${getThreadIdInfo()} start 普通任务. StartTime=$time"
                )
                CsTask.call(object : ICallable<String> {
                    override fun accept(): String {
                        binding?.content?.addData(
                            "task",
                            "${getThreadIdInfo()} 普通任务 accept. StartTime=$time"
                        )
                        return "sssss"
                    }
                })?.mainThread()
                    ?.doOnNext(object : IConsumer<String> {
                        override fun accept(value: String) {
                            binding?.content?.addData(
                                "task",
                                "${getThreadIdInfo()} 普通任务 value=$value. StartTime=$time"
                            )
//                            throw IllegalArgumentException("ssss")
                        }
                    })
                    ?.setOnSuccessCallback(object :OnSuccessCallback<String>{
                        override fun onCallback(value: String) {
                            binding?.content?.addData(
                                "task",
                                "${getThreadIdInfo()} 普通任务 OnSuccess. StartTime=$time"
                            )
                        }
                    })
                    ?.setOnFailedCallback(object :OnFailedCallback{
                        override fun onCallback(throwable: Throwable) {
                            binding?.content?.addData(
                                "task",
                                "${getThreadIdInfo()} 普通任务 OnFailed. StartTime=$time"
                            )
                        }
                    })
                    ?.setOnCompleteCallback(object : OnCompleteCallback {
                        override fun onCallback() {
                            binding?.content?.addData(
                                "task",
                                "${getThreadIdInfo()} 普通任务 OnComplete. StartTime=$time"
                            )
                        }
                    })
                    ?.start()
            }

            R.id.run_block_task -> {
                val time = System.currentTimeMillis()
                binding?.content?.addData(
                    "task",
                    "${getThreadIdInfo()} start 阻塞任务. StartTime=$time"
                )
                val response = CsTask.ioThread()
                    ?.call(object : ICallable<String> {
                        override fun accept(): String {
                            binding?.content?.addData(
                                "task",
                                "${getThreadIdInfo()} 阻塞任务 accept. StartTime=$time"
                            )
                            return "ssss"
                        }
                    })?.doOnNext(object : IConsumer<String> {
                        override fun accept(value: String) {
                            binding?.content?.addData(
                                "task",
                                "${getThreadIdInfo()} 阻塞任务 value=$value. StartTime=$time"
                            )
                        }
                    })
                    ?.blockGetLast()
                binding?.content?.addData(
                    "task",
                    "${getThreadIdInfo()} 阻塞任务 response=$response. StartTime=$time"
                )
            }

            R.id.run_delay_task -> {
                val time = System.currentTimeMillis()
                binding?.content?.addData(
                    "task",
                    "${getThreadIdInfo()} start 延迟任务. StartTime=$time"
                )
                CsTask.delay(2, TimeUnit.SECONDS)?.doOnNext(object :
                    IConsumer<Long> {
                    override fun accept(value: Long) {
                        binding?.content?.addData(
                            "task",
                            "${getThreadIdInfo()} 延迟任务 value=$value. StartTime=$time"
                        )
                    }
                })?.start()
            }

            R.id.run_interval_task -> {
                val time = System.currentTimeMillis()
                binding?.content?.addData(
                    "task",
                    "${getThreadIdInfo()} start 循环任务. StartTime=$time"
                )
                intervalDisposable?.dispose()
                intervalDisposable = CsTask.interval(0, 1000, TimeUnit.MILLISECONDS)
                    ?.doOnNext(object : IConsumer<Long> {
                        override fun accept(value: Long) {
                            binding?.content?.addData(
                                "task",
                                "${getThreadIdInfo()} 循环任务 value=$value. StartTime=$time"
                            )
                        }
                    })?.start()
            }

            R.id.cancel_interval_task -> {
                intervalDisposable?.dispose()
                binding?.content?.addData("task", "${getThreadIdInfo()} 取消循环任务")
            }

            R.id.run_repeat_task -> {
                val time = System.currentTimeMillis()
                binding?.content?.addData(
                    "task",
                    "${getThreadIdInfo()} start 重复 3 次任务. StartTime=$time"
                )
                CsTask.call(object : ICallable<String> {
                    override fun accept(): String {
                        binding?.content?.addData(
                            "task",
                            "${getThreadIdInfo()} 重复任务 accept. StartTime=$time"
                        )
                        return "sss"
                    }
                })?.repeat(3)
                    ?.start()
            }

            R.id.run_handler_task -> {
                val time = System.currentTimeMillis()
                binding?.content?.addData(
                    "task",
                    "${getThreadIdInfo()} start 单线程任务. StartTime=$time"
                )
                val taskGroup = CsTask.launchTaskGroup("asd")

                taskGroup?.start {
                    binding?.content?.addData(
                        "task",
                        "${getThreadIdInfo()} 单线程任务 run. StartTime=$time"
                    )
                }
            }
        }
    }

    private fun getThreadIdInfo(): String {
        return "${Looper.getMainLooper().thread.id}-${Thread.currentThread().id}"
    }

}