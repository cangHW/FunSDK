package com.proxy.service.core.framework.app.context

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.callback.AbstractActivityLifecycle
import com.proxy.service.core.framework.app.context.callback.AbstractAppStateChanged
import com.proxy.service.core.framework.app.context.callback.OnAppShowStatusChangedCallback
import com.proxy.service.core.framework.app.context.common.ComponentCallbacksImpl
import com.proxy.service.core.framework.app.context.lifecycle.ActivityStatusLifecycleImpl
import com.proxy.service.core.framework.app.context.lifecycle.AppShowStatusLifecycleImpl
import com.proxy.service.core.framework.app.context.lifecycle.TopActivityLifecycleImpl
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.callback.MultiRunnableEmitter
import com.proxy.service.threadpool.base.thread.task.IConsumer
import com.proxy.service.threadpool.base.thread.task.IMultiRunnable

/**
 * 上下文以及页面管理相关工具
 *
 * @author: cangHX
 * @data: 2024/4/28 17:22
 * @desc:
 */
@SuppressLint("StaticFieldLeak")
object CsContextManager {

    private const val TAG = "${CoreConfig.TAG}Context"

    /**
     * 获取 Application
     * */
    fun getApplication(): Application {
        if (ContextInit.application == null) {
            CsLogger.tag(TAG).e("CsCore is not init, or context is error.")
        }
        return ContextInit.application!!
    }

    /**
     * 获取全部 activity
     * */
    fun getAllActivity(): List<Activity> {
        return TopActivityLifecycleImpl.getInstance().getAllActivity()
    }

    /**
     * 获取当前正在显示的最上层 activity
     * */
    fun getTopActivity(): Activity? {
        return TopActivityLifecycleImpl.getInstance().getTopActivity()
    }

    /**
     * 关闭所有 activity
     * */
    fun finishAllActivity() {
        TopActivityLifecycleImpl.getInstance().getAllActivity().forEach {
            it.finish()
        }
    }

    /**
     * 关闭除了指定 activity 之外的所有 activity
     * */
    fun finishActivityWithOut(activityClassName: List<String>) {
        CsTask.computationThread()?.call(object : IMultiRunnable<Activity> {
            override fun accept(emitter: MultiRunnableEmitter<Activity>) {
                TopActivityLifecycleImpl.getInstance().getAllActivity().forEach {
                    if (!activityClassName.contains(it.javaClass.name)) {
                        emitter.onNext(it)
                    }
                }
                emitter.onComplete()
            }
        })?.mainThread()?.doOnNext(object : IConsumer<Activity> {
            override fun accept(value: Activity) {
                value.finish()
            }
        })?.start()
    }

    /**
     * 关闭指定 activity
     * */
    fun finishActivityBy(activityClassName: List<String>) {
        CsTask.computationThread()?.call(object : IMultiRunnable<Activity> {
            override fun accept(emitter: MultiRunnableEmitter<Activity>) {
                TopActivityLifecycleImpl.getInstance().getAllActivity().forEach {
                    if (activityClassName.contains(it.javaClass.name)) {
                        emitter.onNext(it)
                    }
                }
                emitter.onComplete()
            }
        })?.mainThread()?.doOnNext(object : IConsumer<Activity> {
            override fun accept(value: Activity) {
                value.finish()
            }
        })?.start()
    }

    /**
     * 当前应用是否在后台
     * */
    fun isInBackground(): Boolean {
        return AppShowStatusLifecycleImpl.getInstance().isInBackground()
    }

    /**
     * 添加应用显示状态变化监听
     * */
    fun addAppShowStatusChangedCallback(callback: OnAppShowStatusChangedCallback) {
        AppShowStatusLifecycleImpl.getInstance().addAppShowStatusChangedCallback(callback)
    }

    /**
     * 移除应用显示状态变化监听
     * */
    fun removeAppShowStatusChangedCallback(callback: OnAppShowStatusChangedCallback) {
        AppShowStatusLifecycleImpl.getInstance().removeAppShowStatusChangedCallback(callback)
    }

    /**
     * 添加 activity 生命周期变化监听
     *
     * @param activity 准备监听的 activity, 如果为 null 则监听全部 activity
     * @param isSync  是否同步模式, 同步模式可用于在某些生命周期节点修改 activity 状态
     * */
    fun addActivityLifecycleCallback(
        activity: Activity?,
        isSync: Boolean = false,
        activityLifecycle: AbstractActivityLifecycle
    ) {
        ActivityStatusLifecycleImpl.getInstance()
            .addActivityLifecycle(
                activity,
                isSync,
                activityLifecycle
            )
    }

    /**
     * 移除 activity 生命周期变化监听
     * */
    fun removeActivityLifecycleCallback(activityLifecycle: AbstractActivityLifecycle) {
        ActivityStatusLifecycleImpl.getInstance()
            .removeActivityLifecycle(activityLifecycle)
    }

    /**
     * 添加 app 状态变化监听
     * */
    fun addAppStateChangedCallback(appStateChanged: AbstractAppStateChanged) {
        ComponentCallbacksImpl.getInstance().addAppStateChanged(appStateChanged)
    }

    /**
     * 移除 app 状态变化监听
     * */
    fun removeAppStateChangedCallback(appStateChanged: AbstractAppStateChanged) {
        ComponentCallbacksImpl.getInstance().removeAppStateChanged(appStateChanged)
    }
}