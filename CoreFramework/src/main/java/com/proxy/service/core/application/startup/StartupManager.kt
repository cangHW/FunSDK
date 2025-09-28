package com.proxy.service.core.application.startup

import android.app.Application
import android.os.Looper
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2025/9/24 20:20
 * @desc:
 */
class StartupManager {

    companion object {
        const val TAG = "${CoreConfig.TAG}Init"

        private val _instance by lazy { StartupManager() }

        fun getInstance(): StartupManager {
            return _instance
        }
    }

    private val initializer = InitializerCache<CsBaseApplication>()
    private var allApps = HashMap<Class<*>, CsBaseApplication>()

    fun initializer(
        allApps: HashMap<Class<*>, CsBaseApplication>,
        applications: ArrayList<CsBaseApplication>,
        appContext: Application,
        isDebug: Boolean,
        time: Long,
        unit: TimeUnit,
        shouldThrow: Boolean
    ) {
        this.allApps = allApps
        initializer.startCounting(allApps.size, time, unit)

        applications.sortWith { c1: CsBaseApplication, c2: CsBaseApplication ->
            c1.priority().compareTo(c2.priority())
        }

        val chain = ArrayList<Class<*>>()

        applications.forEach {
            if (initializer.isTaskRunningOrComplete(it.javaClass)) {
                return@forEach
            }

            chain.clear()

            if (isCanRun(it.dependsOn(), chain, appContext, isDebug)) {
                doWork(it, appContext, isDebug)
            } else {
                initializer.setTaskWaiting(it.javaClass, it)
            }
        }

        runAndWait(
            shouldSkipLoopCall = {
                initializer.isTimeout()
            },
            chain = chain,
            appContext = appContext,
            isDebug = isDebug
        )

        val isTimeout = initializer.isTimeout()
        if (isDebug || isTimeout) {
            val builder = StringBuilder()
            builder.append("[")
            initializer.forEachTaskLog {
                builder.append("\n  ").append(it)
            }
            builder.append("\n").append("]")
            if (isTimeout) {
                CsLogger.tag(TAG).e("Some tasks have timeouts. $builder")
                if (shouldThrow) {
                    throw StartupTimeoutException("init timeout.")
                }
            } else {
                CsLogger.tag(TAG).d("Module initialization usage time statistics. $builder")
            }
        }

        if (isTimeout) {
            CsTask.ioThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    runAndWait(
                        shouldSkipLoopCall = null,
                        chain = chain,
                        appContext = appContext,
                        isDebug = isDebug
                    )
                    return ""
                }
            })?.start()
        }
    }

    private fun runAndWait(
        shouldSkipLoopCall: Callable<Boolean>?,
        chain: ArrayList<Class<*>>,
        appContext: Application,
        isDebug: Boolean,
    ) {
        while (!initializer.isAllTaskComplete()) {
            initializer.awaitAnyTaskComplete()
            if (shouldSkipLoopCall?.call() == true) {
                break
            }

            initializer.forEachWaitingTask {
                chain.clear()
                if (isCanRun(it.dependsOn(), chain, appContext, isDebug)) {
                    doWork(it, appContext, isDebug)
                }
            }
        }
    }

    private fun isCanRun(
        depends: List<Class<out CsBaseApplication>>?,
        chain: ArrayList<Class<*>>,
        appContext: Application,
        isDebug: Boolean
    ): Boolean {
        if (depends.isNullOrEmpty()) {
            return true
        }

        var isComplete = true
        for (appClass in depends) {
            val status = initializer.getTaskStatus(appClass)
            if (status == InitializerCache.STATUS_COMPLETE) {
                continue
            }
            if (status == InitializerCache.STATUS_RUNNING) {
                isComplete = false
                continue
            }

            if (chain.contains(appClass)) {
                val builder = StringBuilder()
                chain.forEach {
                    builder.append(it.simpleName).append(", ")
                }
                builder.append(appClass.simpleName)
                throw CircularDependencyException("There is currently a circular dependency. $builder")
            }

            chain.add(appClass)

            val app = allApps.get(appClass)
                ?: throw NullPointerException("There is an unknown dependency. $appClass")

            if (isCanRun(app.dependsOn(), chain, appContext, isDebug)) {
                if (!doWork(app, appContext, isDebug)) {
                    isComplete = false
                }
            } else {
                initializer.setTaskWaiting(appClass, app)
                isComplete = false
            }

            chain.remove(appClass)
        }

        return isComplete
    }

    private fun doWork(
        app: CsBaseApplication,
        appContext: Application,
        isDebug: Boolean
    ): Boolean {
        initializer.setTaskRunning(app.javaClass)
        return when (app.runOnThread()) {
            CsBaseApplication.ThreadType.MAIN_THREAD -> {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    app.create(appContext, isDebug)
                    initializer.setTaskComplete(app.javaClass, false)
                    true
                } else {
                    CsTask.mainThread()?.call(object : ICallable<String> {
                        override fun accept(): String {
                            app.create(appContext, isDebug)
                            initializer.setTaskComplete(app.javaClass, true)
                            return ""
                        }
                    })?.start()
                    false
                }
            }

            CsBaseApplication.ThreadType.IO_THREAD -> {
                CsTask.ioThread()?.call(object : ICallable<String> {
                    override fun accept(): String {
                        app.create(appContext, isDebug)
                        initializer.setTaskComplete(app.javaClass, true)
                        return ""
                    }
                })?.start()
                false
            }

            CsBaseApplication.ThreadType.COMPUTATION_THREAD -> {
                CsTask.computationThread()?.call(object : ICallable<String> {
                    override fun accept(): String {
                        app.create(appContext, isDebug)
                        initializer.setTaskComplete(app.javaClass, true)
                        return ""
                    }
                })?.start()
                false
            }
        }
    }
}