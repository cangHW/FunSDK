package com.proxy.service.apm.info.monitor.performance.lag.ui.hook.choreographer

import android.view.Choreographer
import com.proxy.service.apm.info.monitor.base.AbstractHook
import com.proxy.service.apm.info.monitor.performance.lag.ui.hook.FrameAvailableListener
import com.proxy.service.core.framework.app.context.CsContextManager
import java.util.concurrent.atomic.AtomicBoolean

/**
 * API 24 以下降级：用相邻 VSYNC 间隔近似帧时长。
 */
class ChoreographerFrameHook(
    private val listener: FrameAvailableListener,
) : AbstractHook<Any>() {

    private val installed = AtomicBoolean(false)
    private var lastFrameTimeNanos: Long = 0L

    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (!installed.get()) {
                return
            }
            val activity = CsContextManager.getTopActivity()
            if (activity == null || activity.isFinishing || activity.isDestroyed) {
                scheduleNextFrame()
                return
            }
            if (frameTimeNanos != 0L && lastFrameTimeNanos != 0L) {
                val intervalNs = frameTimeNanos - lastFrameTimeNanos
                if (intervalNs > 0L) {
                    listener.onFrameInterval(activity, intervalNs)
                }
            }
            lastFrameTimeNanos = frameTimeNanos
            scheduleNextFrame()
        }
    }

    override fun start(t: Any?): Boolean {
        if (installed.compareAndSet(false, true)) {
            lastFrameTimeNanos = 0L
            Choreographer.getInstance().postFrameCallback(frameCallback)
        }
        return true
    }

    override fun stop(t: Any?) {
        if (installed.compareAndSet(true, false)) {
            Choreographer.getInstance().removeFrameCallback(frameCallback)
            lastFrameTimeNanos = 0L
        }
    }

    private fun scheduleNextFrame() {
        if (installed.get()) {
            Choreographer.getInstance().postFrameCallback(frameCallback)
        }
    }
}
