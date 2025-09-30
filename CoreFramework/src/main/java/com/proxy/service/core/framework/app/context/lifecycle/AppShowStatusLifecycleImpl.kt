package com.proxy.service.core.framework.app.context.lifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.proxy.service.core.framework.app.context.callback.OnAppShowStatusChangedCallback
import com.proxy.service.core.framework.collections.CsExcellentSet
import com.proxy.service.core.framework.collections.base.ISet
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
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

    private val appShowStatusChangedCallbacks: ISet<OnAppShowStatusChangedCallback> = CsExcellentSet()

    fun addAppShowStatusChangedCallback(callback: OnAppShowStatusChangedCallback) {
        appShowStatusChangedCallbacks.putSync(callback)
    }

    fun removeAppShowStatusChangedCallback(callback: OnAppShowStatusChangedCallback) {
        appShowStatusChangedCallbacks.removeSync(callback)
    }

    fun isInBackground(): Boolean {
        return !isForeground.get()
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
        appShowStatusChangedCallbacks.forEachAsync {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    callback(it)
                    return ""
                }
            })?.start()
        }
    }

}