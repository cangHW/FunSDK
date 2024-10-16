package com.proxy.service.core.framework.app.context.lifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.proxy.service.core.framework.app.context.callback.OnAppShowStatusChangedCallback
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.callback.MultiRunnableEmitter
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import com.proxy.service.threadpool.base.thread.task.IMultiRunnable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/7/2 11:23
 * @desc:
 */
class AppShowStatusLifecycleImpl : ActivityLifecycleCallbacks {

    companion object {
        private val mInstance by lazy { AppShowStatusLifecycleImpl() }
        fun getInstance(): AppShowStatusLifecycleImpl {
            return mInstance
        }
    }

    private val appShowStatusChangedCallbacks = ArrayList<OnAppShowStatusChangedCallback>()

    fun addAppShowStatusChangedCallback(callback: OnAppShowStatusChangedCallback) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                if (!appShowStatusChangedCallbacks.contains(callback)) {
                    synchronized(appShowStatusChangedCallbacks) {
                        appShowStatusChangedCallbacks.add(callback)
                    }
                }
                return ""
            }
        })?.start()
    }

    fun removeAppShowStatusChangedCallback(callback: OnAppShowStatusChangedCallback) {
        CsTask.computationThread()?.call(object :ICallable<String>{
            override fun accept(): String {
                synchronized(appShowStatusChangedCallbacks) {
                    appShowStatusChangedCallbacks.remove(callback)
                }
                return ""
            }
        })?.start()
    }


    private var isForeground = AtomicBoolean(false)
    private var configCount = 0
    private var foregroundCount = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        if (configCount < 0) {
            ++configCount
            return
        }
        ++foregroundCount
    }

    override fun onActivityResumed(activity: Activity) {
        if (isForeground.compareAndSet(false, true)) {
            forEach {
                it.onAppForeground()
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        if (activity.isChangingConfigurations) {
            --configCount
            return
        }
        --foregroundCount
        if (foregroundCount > 0) {
            return
        }
        if (isForeground.compareAndSet(true, false)) {
            forEach {
                it.onAppBackground()
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    private fun forEach(callback: (OnAppShowStatusChangedCallback) -> Unit) {
        if (appShowStatusChangedCallbacks.size <= 0) {
            return
        }
        CsTask.computationThread()?.call(object : IMultiRunnable<OnAppShowStatusChangedCallback> {
            override fun accept(emitter: MultiRunnableEmitter<OnAppShowStatusChangedCallback>) {
                if (appShowStatusChangedCallbacks.size > 0) {
                    synchronized(appShowStatusChangedCallbacks) {
                        appShowStatusChangedCallbacks.forEach {
                            emitter.onNext(it)
                        }
                    }
                }
                emitter.onComplete()
            }
        })?.mainThread()?.doOnNext(object : IConsumer<OnAppShowStatusChangedCallback> {
            override fun accept(value: OnAppShowStatusChangedCallback) {
                callback(value)
            }
        })?.start()
    }

}