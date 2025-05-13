package com.proxy.service.core.framework.app.context.lifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.proxy.service.core.framework.app.context.cache.ICache
import com.proxy.service.core.framework.app.context.cache.SynchronizedCache
import com.proxy.service.core.framework.app.context.callback.AbstractActivityLifecycle
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/7/2 11:23
 * @desc:
 */
class ActivityStatusLifecycleImpl : ActivityLifecycleCallbacks {

    companion object {
        private val any = Any()

        private val mInstance by lazy { ActivityStatusLifecycleImpl() }

        fun getInstance(): ActivityStatusLifecycleImpl {
            return mInstance
        }
    }

    private val globalLifecycleSync: ICache<AbstractActivityLifecycle, Any> = SynchronizedCache()
    private val globalLifecycleAsync: ICache<AbstractActivityLifecycle, Any> = SynchronizedCache()

    private val lifecycleSync: ICache<AbstractActivityLifecycle, Activity> = SynchronizedCache()
    private val lifecycleAsync: ICache<AbstractActivityLifecycle, Activity> = SynchronizedCache()

    fun addAbstractActivityLifecycle(
        activity: Activity?,
        isSync: Boolean,
        activityLifecycle: AbstractActivityLifecycle
    ) {
        if (isSync) {
            activity?.let {
                lifecycleSync.putAsync(activityLifecycle, it)
            } ?: let {
                globalLifecycleSync.putAsync(activityLifecycle, any)
            }
        } else {
            activity?.let {
                lifecycleAsync.putAsync(activityLifecycle, it)
            } ?: let {
                globalLifecycleAsync.putAsync(activityLifecycle, any)
            }
        }
    }

    fun removeAbstractActivityLifecycle(activityLifecycle: AbstractActivityLifecycle) {
        globalLifecycleSync.removeAsync(activityLifecycle)
        globalLifecycleAsync.removeAsync(activityLifecycle)
        lifecycleSync.removeAsync(activityLifecycle)
        lifecycleAsync.removeAsync(activityLifecycle)
    }


    // *****************  OnCreate  ************************
    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        forEach(activity) {
            it.onActivityPreCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        forEach(activity) {
            it.onActivityCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
        forEach(activity) {
            it.onActivityPostCreated(activity, savedInstanceState)
        }
    }


    // *****************  OnStart  ************************
    override fun onActivityPreStarted(activity: Activity) {
        forEach(activity) {
            it.onActivityPreStarted(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        forEach(activity) {
            it.onActivityStarted(activity)
        }
    }

    override fun onActivityPostStarted(activity: Activity) {
        forEach(activity) {
            it.onActivityPostStarted(activity)
        }
    }


    // *****************  OnResume  ************************
    override fun onActivityPreResumed(activity: Activity) {
        forEach(activity) {
            it.onActivityPreResumed(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        forEach(activity) {
            it.onActivityResumed(activity)
        }
    }

    override fun onActivityPostResumed(activity: Activity) {
        forEach(activity) {
            it.onActivityPostResumed(activity)
        }
    }


    // *****************  OnPause  ************************
    override fun onActivityPrePaused(activity: Activity) {
        forEach(activity) {
            it.onActivityPrePaused(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        forEach(activity) {
            it.onActivityPaused(activity)
        }
    }

    override fun onActivityPostPaused(activity: Activity) {
        forEach(activity) {
            it.onActivityPostPaused(activity)
        }
    }


    // *****************  OnStop  ************************
    override fun onActivityPreStopped(activity: Activity) {
        forEach(activity) {
            it.onActivityPreStopped(activity)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        forEach(activity) {
            it.onActivityStopped(activity)
        }
    }

    override fun onActivityPostStopped(activity: Activity) {
        forEach(activity) {
            it.onActivityPostStopped(activity)
        }
    }


    // *****************  OnSaveInstanceState  ************************
    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
        forEach(activity) {
            it.onActivityPreSaveInstanceState(activity, outState)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        forEach(activity) {
            it.onActivitySaveInstanceState(activity, outState)
        }
    }

    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        forEach(activity) {
            it.onActivityPostSaveInstanceState(activity, outState)
        }
    }


    // *****************  OnDestroy  ************************
    override fun onActivityPreDestroyed(activity: Activity) {
        forEach(activity) {
            it.onActivityPreDestroyed(activity)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        forEach(activity) {
            it.onActivityDestroyed(activity)
        }
    }

    override fun onActivityPostDestroyed(activity: Activity) {
        forEach(activity) {
            it.onActivityPostDestroyed(activity)
        }
        removeByActivity(activity)
    }


    private fun forEach(activity: Activity, callback: (AbstractActivityLifecycle) -> Unit) {
        globalLifecycleSync.forEachSync {
            callback(it.key)
        }

        globalLifecycleAsync.forEachAsync {
            CsTask.mainThread()
                ?.call(object : ICallable<String> {
                    override fun accept(): String {
                        callback(it.key)
                        return ""
                    }
                })?.start()
        }

        lifecycleSync.filterSync {
            it.value == activity
        }.forEach {
            callback(it.key)
        }

        lifecycleAsync.filterAsync(
            predicate = { it.value == activity },
            observer = {
                CsTask.mainThread()
                    ?.call(object : ICallable<String> {
                        override fun accept(): String {
                            callback(it.key)
                            return ""
                        }
                    })?.start()
            }
        )
    }

    private fun removeByActivity(activity: Activity) {
        lifecycleSync.removeAsync { it.value == activity }
        lifecycleAsync.removeAsync { it.value == activity }
    }
}