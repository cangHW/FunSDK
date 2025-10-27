package com.proxy.service.core.framework.app.context

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.callback.AbstractActivityLifecycle
import com.proxy.service.core.framework.app.context.callback.AbstractAppConfigStateChanged
import com.proxy.service.core.framework.app.context.callback.OnAppVisibilityCallback
import com.proxy.service.core.framework.app.context.common.ComponentCallbacksImpl
import com.proxy.service.core.framework.app.context.lifecycle.ActivityLifecycleImpl
import com.proxy.service.core.framework.app.context.lifecycle.AppVisibilityImpl
import com.proxy.service.core.framework.app.context.lifecycle.TopActivityImpl
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
        return TopActivityImpl.getInstance().getAllActivity()
    }

    /**
     * 获取当前正在显示的最上层 activity
     * */
    fun getTopActivity(): Activity? {
        return TopActivityImpl.getInstance().getTopActivity()
    }

    /**
     * 关闭所有 activity
     * */
    fun finishAllActivity() {
        TopActivityImpl.getInstance().getAllActivity().forEach {
            it.finish()
        }
    }

    /**
     * 关闭除了指定 activity 之外的所有 activity
     * */
    fun finishAllActivitiesExcept(activityClassNames: List<String>) {
        CsTask.computationThread()?.call(object : IMultiRunnable<Activity> {
            override fun accept(emitter: MultiRunnableEmitter<Activity>) {
                TopActivityImpl.getInstance().getAllActivity().forEach {
                    if (!activityClassNames.contains(it.javaClass.name)) {
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
    fun finishSpecificActivities(activityClassNames: List<String>) {
        CsTask.computationThread()?.call(object : IMultiRunnable<Activity> {
            override fun accept(emitter: MultiRunnableEmitter<Activity>) {
                TopActivityImpl.getInstance().getAllActivity().forEach {
                    if (activityClassNames.contains(it.javaClass.name)) {
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
        return AppVisibilityImpl.getInstance().isInBackground()
    }

    /**
     * 添加应用显示状态变化监听
     * */
    fun addAppVisibilityCallback(callback: OnAppVisibilityCallback) {
        AppVisibilityImpl.getInstance().addAppVisibilityCallback(callback)
    }

    /**
     * 移除应用显示状态变化监听
     * */
    fun removeAppVisibilityCallback(callback: OnAppVisibilityCallback) {
        AppVisibilityImpl.getInstance().removeAppVisibilityCallback(callback)
    }

    /**
     * 添加 activity 生命周期变化监听
     *
     * @param activity      准备监听的 activity, 如果为 null 则监听全部 activity
     * @param isSyncMode    是否同步模式, 同步模式可用于在某些生命周期节点修改 activity 状态
     * */
    fun addActivityLifecycleCallback(
        activity: Activity?,
        isSyncMode: Boolean = false,
        lifecycleCallback: AbstractActivityLifecycle
    ) {
        ActivityLifecycleImpl.getInstance()
            .addActivityLifecycleCallback(
                activity,
                isSyncMode,
                lifecycleCallback
            )
    }

    /**
     * 移除 activity 生命周期变化监听
     * */
    fun removeActivityLifecycleCallback(lifecycleCallback: AbstractActivityLifecycle) {
        ActivityLifecycleImpl.getInstance()
            .removeActivityLifecycleCallback(lifecycleCallback)
    }

    /**
     * 添加 app 配置状态变化监听
     * */
    fun addAppConfigStateCallback(appConfigStateChanged: AbstractAppConfigStateChanged) {
        ComponentCallbacksImpl.getInstance().addAppConfigStateChanged(appConfigStateChanged)
    }

    /**
     * 移除 app 配置状态变化监听
     * */
    fun removeAppConfigStateCallback(appConfigStateChanged: AbstractAppConfigStateChanged) {
        ComponentCallbacksImpl.getInstance().removeAppConfigStateChanged(appConfigStateChanged)
    }
}