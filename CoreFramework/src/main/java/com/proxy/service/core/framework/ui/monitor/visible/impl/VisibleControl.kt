package com.proxy.service.core.framework.ui.monitor.visible.impl

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
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
class VisibleControl(
    private var config: VisibleMonitorConfig?,
    private var callback: ControlCallback?
) : IVisibleMonitorHelper {

    interface ControlCallback {
        /**
         * view 显示
         */
        fun onShow()

        /**
         * view 隐藏
         */
        fun onGone()
    }

    private val pool = CsTask.launchTaskGroup(VisibleConfig.LOOP_NAME_FOR_CHECK)
    private val isViewVisibility = AtomicBoolean(false)

    private val task = MonitorRunnable(this)
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
            isViewVisibility.set(false)
        }
    }

    override fun stop() {
        disposable?.disposeTask()
        pool?.start {
            disposable?.disposeTask()
        }
    }

    override fun destroy() {
        disposable?.disposeTask()
        pool?.start {
            config = null
            callback = null
            disposable?.disposeTask()
        }
    }

    /**
     * 检测当前是否允许回调 view 状态
     */
    private fun checkCanCallback(visibility: Boolean) {
        if (callback == null) {
            return
        }
        if (visibility == isViewVisibility.get()) {
            return
        }
        if (visibility) {
            if (isViewVisibility.compareAndSet(false, true)) {
                callback?.onShow()
            }
        } else {
            if (isViewVisibility.compareAndSet(true, false)) {
                callback?.onGone()
            }
        }
    }

    private class MonitorRunnable(control: VisibleControl) : Runnable {

        private val weak: WeakReference<VisibleControl> = WeakReference(control)

        override fun run() {
            val view = weak.get()?.config?.getBindView() ?: return
            val area = weak.get()?.config?.getArea() ?: return
            val delayMillis = weak.get()?.config?.getDelayMillis() ?: return

            startCheck(view, area)

            try {
                weak.get()?.disposable = weak.get()?.pool
                    ?.setDelay(delayMillis, TimeUnit.MILLISECONDS)
                    ?.start(this)
            } catch (throwable: Throwable) {
                CsLogger.tag(VisibleConfig.TAG).e(throwable)
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
                    weak.get()?.checkCanCallback(false)
                    return
                }
            } catch (throwable: Throwable) {
                CsLogger.tag(VisibleConfig.TAG).e(throwable)
            }

            try {
                // 2. 检查 view 状态
                val viewState = checkViewVisibleState(view)
                if (!viewState) {
                    weak.get()?.checkCanCallback(false)
                    return
                }
            } catch (throwable: Throwable) {
                CsLogger.tag(VisibleConfig.TAG).e(throwable)
            }

            try {
                // 3. 检查 view 显示区域大小
                val location: Boolean = checkShowArea(view, area)
                weak.get()?.checkCanCallback(location)
            } catch (throwable: Throwable) {
                CsLogger.tag(VisibleConfig.TAG).e(throwable)
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