package com.proxy.service.apm.info.monitor.performance.lag.ui.hook

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import com.proxy.service.apm.info.monitor.performance.lag.ui.frame.FrameBudgetResolver
import com.proxy.service.apm.info.monitor.performance.lag.ui.hook.choreographer.ChoreographerFrameHook
import com.proxy.service.apm.info.monitor.performance.lag.ui.hook.frame.FrameMetricsHook
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.context.callback.AbstractActivityLifecycle
import java.util.WeakHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * UI 渲染卡顿 Hook 安装器：API 24+ 使用 [FrameMetricsHook]；低版本降级 [ChoreographerFrameHook]。
 *
 * @author: cangHX
 * @date: 2026/5/27 17:40
 */
class FrameMetricsHookInstaller(
    private val listener: FrameAvailableListener,
) {

    companion object {
        private const val METRICS_THREAD_NAME = "UiLag-FrameMetrics"
    }

    private val installed = AtomicBoolean(false)
    private val hooks = WeakHashMap<Activity, FrameMetricsHook>()

    private var handlerThread: HandlerThread? = null
    private var metricsHandler: Handler? = null
    private var choreographerHook: ChoreographerFrameHook? = null

    private val lifecycleImpl = object : AbstractActivityLifecycle() {

        override fun onActivityResumed(activity: Activity) {
            super.onActivityResumed(activity)
            registerHook(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            super.onActivityPaused(activity)
            FrameBudgetResolver.evict(activity)
            removeHook(activity)
        }

        override fun onActivityDestroyed(activity: Activity) {
            super.onActivityDestroyed(activity)
            FrameBudgetResolver.evict(activity)
            removeHook(activity)
        }
    }

    fun install() {
        if (!installed.compareAndSet(false, true)) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CsContextManager.addActivityLifecycleCallback(null, true, lifecycleImpl)
        } else {
            choreographerHook = ChoreographerFrameHook(listener).also { it.start() }
        }
    }

    fun uninstall() {
        if (!installed.compareAndSet(true, false)) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CsContextManager.removeActivityLifecycleCallback(lifecycleImpl)
            hooks.keys.toList().forEach {
                removeHook(it)
            }
            releaseMetricsHandler()
        } else {
            choreographerHook?.stop()
            choreographerHook = null
        }
    }

    private fun registerHook(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return
        }
        if (hooks.containsKey(activity)) {
            return
        }
        val hook = FrameMetricsHook(activity, ensureMetricsHandler(), listener)
        hook.start()
        hooks[activity] = hook
    }

    private fun removeHook(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return
        }
        hooks.remove(activity)?.stop()
    }

    private fun ensureMetricsHandler(): Handler {
        metricsHandler?.let {
            return it
        }
        val thread = HandlerThread(METRICS_THREAD_NAME).also { it.start() }
        handlerThread = thread
        return Handler(thread.looper).also {
            metricsHandler = it
        }
    }

    private fun releaseMetricsHandler() {
        metricsHandler = null
        handlerThread?.quitSafely()
        handlerThread = null
    }
}
