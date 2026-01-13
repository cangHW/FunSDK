package com.proxy.service.core.framework.ui.view.action.exposure.controller

import android.view.View
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.ui.constants.UiViewConstants
import com.proxy.service.core.framework.ui.view.action.base.IViewActionCallback
import com.proxy.service.core.framework.ui.view.action.exposure.monitor.ExposureMonitor
import com.proxy.service.core.framework.ui.view.action.exposure.monitor.MonitorCallback
import com.proxy.service.core.framework.ui.view.action.exposure.params.ExposureParams
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.handler.controller.ITaskDisposable
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2026/1/9 15:47
 * @desc:
 */
class ExposureControllerImpl(
    private val view: WeakReference<View?>,
    private val area: Float,
    private val duration: Long,
    private val delayMillis: Long,
    private var tag: Any?,
    private var callback: IViewActionCallback<ExposureParams>?
) : ExposureController, View.OnAttachStateChangeListener, MonitorCallback {

    private val monitor by lazy {
        ExposureMonitor(view, area, delayMillis, this)
    }

    private val isShow = AtomicBoolean(false)

    private val isStartByUser = AtomicBoolean(false)
    private val isReleaseByUser = AtomicBoolean(false)

    private val isViewAttachedToWindow = AtomicBoolean(view.get()?.isAttachedToWindow ?: false)


    private val pool = CsTask.launchTaskGroup(UiViewConstants.EXPOSURE_LOOP_NAME_FOR_CALL)
    private val task = ExposedCallbackRunnable(view, isShow, tag, callback)
    private var disposable: ITaskDisposable? = null

    override fun start() {
        if (isReleaseByUser.get()) {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION)
                .w("The current exposure detector has been released and cannot be used any longer.")
            return
        }

        if (isStartByUser.compareAndSet(false, true)) {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).d("monitor start. view: ${view.get()}")
            realStart()
        } else {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION)
                .w("The exposure detection of view has been start and does not need to be start again.")
        }
    }

    override fun reset() {
        if (isReleaseByUser.get()) {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION)
                .w("The current exposure detector has been released and cannot be used any longer.")
            return
        }

        CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).d("monitor reset. view: ${view.get()}")

        isShow.set(false)
        isStartByUser.set(false)
        monitor.reset()
    }

    override fun stop() {
        if (isReleaseByUser.get()) {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION)
                .w("The current exposure detector has been released and cannot be used any longer.")
            return
        }

        if (isStartByUser.compareAndSet(true, false)) {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).d("monitor stop. view: ${view.get()}")
            realStop()
        } else {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION)
                .w("The exposure detection of view has been stop and there is no need to turn it stop again.")
        }
    }

    override fun release() {
        if (isReleaseByUser.compareAndSet(false, true)) {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).d("monitor release. view: ${view.get()}")
            monitor.release()
        } else {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION)
                .w("The exposure detection of view has been release and does not need to be release again.")
        }

        view.clear()
        tag = null
        callback = null
        disposable?.disposeTask()
        disposable = null
    }


    override fun onViewAttachedToWindow(v: View) {
        if (isViewAttachedToWindow.compareAndSet(false, true)) {
            realStart()
        }
    }

    override fun onViewDetachedFromWindow(v: View) {
        if (isViewAttachedToWindow.compareAndSet(true, false)) {
            realStop()
        }
    }


    private fun realStart() {
        if (!isStartByUser.get()) {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION)
                .w("The user has not enabled exposure detection.")
            return
        }
        if (!isViewAttachedToWindow.get()) {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION)
                .w("The current view is detached from window. view= ${view.get()}")
            return
        }
        monitor.start()
    }

    private fun realStop() {
        monitor.stop()
    }


    override fun onShow() {
        val rv = view.get() ?: return

        CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION)
            .d("view show, ready to check exposure state. view= $rv")

        disposable?.disposeTask()
        disposable = pool?.setDelay(duration, TimeUnit.MILLISECONDS)?.start(task)
    }

    override fun onGone() {
        if (view.get() == null) {
            return
        }

        disposable?.disposeTask()
        pool?.start {
            val rv = view.get() ?: return@start
            if (isShow.compareAndSet(true, false)) {
                CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION)
                    .d("view gone, callback state hidden. view= $rv")

                try {
                    callback?.onViewActionCall(ExposureParams.create(false, tag))
                } catch (throwable: Throwable) {
                    CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).e(throwable)
                }
            }
        }
    }


    private class ExposedCallbackRunnable(
        private val view: WeakReference<View?>,
        private val isShow: AtomicBoolean,
        private val tag: Any?,
        private val callback: IViewActionCallback<ExposureParams>?
    ) : Runnable {

        override fun run() {
            val rv = view.get() ?: return
            if (isShow.compareAndSet(false, true)) {
                CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION)
                    .d("callback state exposed. view= $rv")

                try {
                    callback?.onViewActionCall(ExposureParams.create(true, tag))
                } catch (throwable: Throwable) {
                    CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).e(throwable)
                }
            }
        }
    }
}