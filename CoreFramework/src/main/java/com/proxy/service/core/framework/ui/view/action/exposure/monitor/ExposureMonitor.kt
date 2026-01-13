package com.proxy.service.core.framework.ui.view.action.exposure.monitor

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.ui.constants.UiViewConstants
import com.proxy.service.core.framework.ui.view.action.exposure.controller.ExposureController
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
class ExposureMonitor(
    private val view: WeakReference<View?>,
    private val area: Float,
    private val delayMillis: Long,
    private var callback: MonitorCallback?
) : ExposureController, MonitorCallback, MonitorContinueCallback {

    private val isViewShow = AtomicBoolean(false)

    private val pool = CsTask.launchTaskGroup(UiViewConstants.EXPOSURE_LOOP_NAME_FOR_CHECK)
    private val task by lazy {
        MonitorRunnable(view, area, this, this)
    }

    private var disposable: ITaskDisposable? = null


    override fun start() {
        disposable?.disposeTask()
        pool?.start {
            disposable?.disposeTask()
            disposable = pool.start(task)
        }
    }

    override fun reset() {
        disposable?.disposeTask()
        pool?.start {
            disposable?.disposeTask()
            isViewShow.set(false)
        }
    }

    override fun stop() {
        disposable?.disposeTask()
        pool?.start {
            disposable?.disposeTask()
        }
    }

    override fun release() {
        disposable?.disposeTask()
        pool?.start {
            disposable?.disposeTask()
            view.clear()
            callback = null
        }
    }


    override fun onShow() {
        if (isViewShow.compareAndSet(false, true)) {
            callback?.onShow()
        }
    }

    override fun onGone() {
        if (isViewShow.compareAndSet(true, false)) {
            callback?.onGone()
        }
    }

    override fun onRunNext() {
        disposable = pool?.setDelay(delayMillis, TimeUnit.MILLISECONDS)?.start(task)
    }

    private class MonitorRunnable(
        private val view: WeakReference<View?>,
        private val area: Float,
        private val callback: MonitorCallback,
        private val continueCallback: MonitorContinueCallback
    ) : Runnable {

        override fun run() {
            val view = view.get() ?: return
            startCheck(view, area)

            try {
                continueCallback.onRunNext()
            } catch (throwable: Throwable) {
                CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).e(throwable)
            }
        }

        /**
         * 开始检测
         * */
        private fun startCheck(view: View, area: Float) {
            try {
                // 1. 检查 window 状态
                val windowState = checkWindowVisibleState(view)
                if (!windowState) {
                    callback.onGone()
                    return
                }
            } catch (throwable: Throwable) {
                CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).e(throwable)
            }

            try {
                // 2. 检查 view 状态
                val viewState = checkViewVisibleState(view)
                if (!viewState) {
                    callback.onGone()
                    return
                }
            } catch (throwable: Throwable) {
                CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).e(throwable)
            }

            try {
                // 3. 检查 view 显示区域大小
                if (checkShowArea(view, area)) {
                    callback.onShow()
                } else {
                    callback.onGone()
                }
            } catch (throwable: Throwable) {
                CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).e(throwable)
            }
        }

        /**
         * 检查显示隐藏状态
         */
        private fun checkViewVisibleState(view: View): Boolean {
            if (view.visibility != View.VISIBLE) {
                return false
            }
            var currentView = view
            while (currentView.parent is ViewGroup) {
                val currentParent = currentView.parent as ViewGroup
                if (currentParent.visibility != View.VISIBLE) {
                    return false
                }
                currentView = currentParent
            }
            return true
        }

        /**
         * 检查可见区域
         */
        private fun checkShowArea(view: View, area: Float): Boolean {
            val rect = Rect()
            val flag = view.getLocalVisibleRect(rect)
            if (!flag) {
                return false
            }
            return rect.width() * rect.height() >= view.measuredWidth * view.measuredHeight * area
        }

        /**
         * 检查窗口可见状态
         */
        private fun checkWindowVisibleState(view: View): Boolean {
            return view.windowVisibility == View.VISIBLE
        }

    }


}