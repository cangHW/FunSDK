package com.proxy.service.core.framework.app.context.lifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.proxy.service.core.framework.collections.base.IMap
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.app.context.callback.AbstractActivityLifecycle
import com.proxy.service.core.framework.collections.CsExcellentSet
import com.proxy.service.core.framework.collections.base.ISet
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/7/2 11:23
 * @desc:
 */
class ActivityStatusLifecycleImpl : ActivityLifecycleCallbacks {

    companion object {
        private val mInstance by lazy { ActivityStatusLifecycleImpl() }

        fun getInstance(): ActivityStatusLifecycleImpl {
            return mInstance
        }
    }

    private val globalLifecycleSync: ISet<AbstractActivityLifecycle> = CsExcellentSet()
    private val globalLifecycleAsync: ISet<AbstractActivityLifecycle> = CsExcellentSet()

    private val lifecycleSync: IMap<AbstractActivityLifecycle, Activity> = CsExcellentMap()
    private val lifecycleAsync: IMap<AbstractActivityLifecycle, Activity> = CsExcellentMap()

    fun addAbstractActivityLifecycle(
        activity: Activity?,
        isSync: Boolean,
        activityLifecycle: AbstractActivityLifecycle
    ) {
        if (isSync) {
            activity?.let {
                lifecycleSync.putSync(activityLifecycle, it)
            } ?: let {
                globalLifecycleSync.putSync(activityLifecycle)
            }
        } else {
            activity?.let {
                lifecycleAsync.putSync(activityLifecycle, it)
            } ?: let {
                globalLifecycleAsync.putSync(activityLifecycle)
            }
        }
    }

    fun removeAbstractActivityLifecycle(activityLifecycle: AbstractActivityLifecycle) {
        globalLifecycleSync.removeSync(activityLifecycle)
        globalLifecycleAsync.removeSync(activityLifecycle)
        lifecycleSync.removeSync(activityLifecycle)
        lifecycleAsync.removeSync(activityLifecycle)
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
            callback(it)
        }

        globalLifecycleAsync.forEachAsync {
            CsTask.mainThread()
                ?.call(object : ICallable<String> {
                    override fun accept(): String {
                        callback(it)
                        return ""
                    }
                })?.start()
        }

        lifecycleSync.filterSync { _, value ->
            value == activity
        }.forEach {
            callback(it.key)
        }

        lifecycleAsync.filterAsync(
            predicate = { _, value ->
                value == activity
            },
            observer = { key, _ ->
                CsTask.mainThread()
                    ?.call(object : ICallable<String> {
                        override fun accept(): String {
                            callback(key)
                            return ""
                        }
                    })?.start()
            }
        )
    }

    private fun removeByActivity(activity: Activity) {
        lifecycleSync.removeAsync { _, value ->
            value == activity
        }
        lifecycleAsync.removeAsync { _, value ->
            value == activity
        }
    }
}