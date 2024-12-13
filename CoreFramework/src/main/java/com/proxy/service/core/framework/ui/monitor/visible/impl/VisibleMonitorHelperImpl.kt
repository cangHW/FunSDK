package com.proxy.service.core.framework.ui.monitor.visible.impl

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.ui.monitor.visible.base.IVisibleMonitorHelper
import com.proxy.service.core.framework.ui.monitor.visible.config.VisibleConfig
import com.proxy.service.core.framework.ui.monitor.visible.config.VisibleMonitorConfig
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.handler.controller.ITaskDisposable
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/12/4 20:33
 * @desc:
 */
class VisibleMonitorHelperImpl(
    private var config: VisibleMonitorConfig?
) : IVisibleMonitorHelper, VisibleControl.ControlCallback, LifecycleEventObserver {

    private val isShow = AtomicBoolean(false)

    private val pool = CsTask.launchTaskGroup(VisibleConfig.LOOP_NAME_FOR_CALL)
    private val task = CallbackRunnable(this)

    private var control: VisibleControl? = VisibleControl(config, this)

    private var disposable: ITaskDisposable? = null

    init {
        config?.getLifecycleOwner()?.lifecycle?.addObserver(this)
    }

    override fun start() {
        CsLogger.tag(VisibleConfig.TAG).d("monitor start. view: ${config?.getBindView()}")
        control?.start()
    }

    override fun reset() {
        CsLogger.tag(VisibleConfig.TAG).d("monitor reset. view: ${config?.getBindView()}")
        control?.reset()
        isShow.set(false)
    }

    override fun stop() {
        CsLogger.tag(VisibleConfig.TAG).d("monitor stop. view: ${config?.getBindView()}")
        control?.stop()
    }

    override fun destroy() {
        CsLogger.tag(VisibleConfig.TAG).d("monitor destroy. view: ${config?.getBindView()}")
        config = null
        control?.destroy()
        control = null
    }

    override fun onShow() {
        val duration = config?.getDuration() ?: return

        CsLogger.tag(VisibleConfig.TAG)
            .d("view show, ready to callback. view: ${config?.getBindView()}")
        disposable?.disposeTask()
        disposable = pool?.setDelay(duration, TimeUnit.MILLISECONDS)?.start(task)
    }

    override fun onGone() {
        CsLogger.tag(VisibleConfig.TAG).d("view gone. view: ${config?.getBindView()}")
        disposable?.disposeTask()
        pool?.start {
            if (isShow.compareAndSet(true, false)) {
                try {
                    config?.getViewVisibleMonitorCallback()?.onGone(config?.getTag())
                } catch (throwable: Throwable) {
                    CsLogger.tag(VisibleConfig.TAG).e(throwable)
                }
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        config?.getLifecycleOwner()?.let {
            val currentState = it.lifecycle.currentState
            if (currentState == Lifecycle.State.DESTROYED) {
                CsLogger.tag(VisibleConfig.TAG).d("page destroy. lifecycleOwner: $it")
                it.lifecycle.removeObserver(this)
                destroy()
            }
        }
    }

    private class CallbackRunnable(helper: VisibleMonitorHelperImpl) : Runnable {

        private val weak: WeakReference<VisibleMonitorHelperImpl> = WeakReference(helper)

        override fun run() {
            val helper = weak.get() ?: return
            val config = helper.config ?: return

            val isShow = helper.isShow
            val tag = config.getTag()
            val callback = config.getViewVisibleMonitorCallback()

            if (isShow.compareAndSet(false, true)) {
                CsLogger.tag(VisibleConfig.TAG)
                    .d("callback view show. view: ${config.getBindView()}")

                try {
                    callback?.onShow(tag)
                } catch (throwable: Throwable) {
                    CsLogger.tag(VisibleConfig.TAG).e(throwable)
                }
            }
        }
    }

}