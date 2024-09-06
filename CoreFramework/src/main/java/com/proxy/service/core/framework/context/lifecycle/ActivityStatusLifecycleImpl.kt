package com.proxy.service.core.framework.context.lifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.proxy.service.core.framework.context.callback.AbstractActivityLifecycle
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.callback.MultiRunnableEmitter
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import com.proxy.service.threadpool.base.thread.task.IMultiRunnable

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

    private val activityLifecycleList = ArrayList<AbstractActivityLifecycle>()
    private val activityLifecycleMap = HashMap<AbstractActivityLifecycle, Activity>()

    fun addAbstractActivityLifecycle(
        activity: Activity?,
        activityLifecycle: AbstractActivityLifecycle
    ) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                activity?.let {
                    if (!activityLifecycleMap.containsKey(activityLifecycle)) {
                        synchronized(activityLifecycleMap) {
                            if (!activityLifecycleMap.containsKey(activityLifecycle)) {
                                activityLifecycleMap[activityLifecycle] = it
                            }
                        }
                    }
                } ?: let {
                    if (!activityLifecycleList.contains(activityLifecycle)) {
                        synchronized(activityLifecycleList) {
                            if (!activityLifecycleList.contains(activityLifecycle)) {
                                activityLifecycleList.add(activityLifecycle)
                            }
                        }
                    }
                }
                return ""
            }
        })?.start()
    }

    fun removeAbstractActivityLifecycle(activityLifecycle: AbstractActivityLifecycle) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                synchronized(activityLifecycleList) {
                    activityLifecycleList.remove(activityLifecycle)
                }
                synchronized(activityLifecycleMap) {
                    activityLifecycleMap.remove(activityLifecycle)
                }
                return ""
            }
        })?.start()
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
        if (activityLifecycleList.size <= 0 && activityLifecycleMap.size <= 0) {
            return
        }

        CsTask.computationThread()?.call(object : IMultiRunnable<AbstractActivityLifecycle> {
            override fun accept(emitter: MultiRunnableEmitter<AbstractActivityLifecycle>) {
                synchronized(activityLifecycleList) {
                    activityLifecycleList.forEach {
                        emitter.onNext(it)
                    }
                }
                synchronized(activityLifecycleMap) {
                    activityLifecycleMap.filter {
                        it.value == activity
                    }.forEach {
                        emitter.onNext(it.key)
                    }
                }
                emitter.onComplete()
            }
        })?.mainThread()?.doOnNext(object : IConsumer<AbstractActivityLifecycle> {
            override fun accept(value: AbstractActivityLifecycle) {
                callback(value)
            }
        })?.start()
    }

    private fun removeByActivity(activity: Activity) {
        if (activityLifecycleList.size <= 0 && activityLifecycleMap.size <= 0) {
            return
        }

        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                synchronized(activityLifecycleMap) {
                    val list = ArrayList<AbstractActivityLifecycle>()
                    activityLifecycleMap.filter {
                        it.value == activity
                    }.forEach {
                        list.add(it.key)
                    }
                    list.forEach {
                        activityLifecycleMap.remove(it)
                    }
                }
                return ""
            }
        })?.start()
    }
}