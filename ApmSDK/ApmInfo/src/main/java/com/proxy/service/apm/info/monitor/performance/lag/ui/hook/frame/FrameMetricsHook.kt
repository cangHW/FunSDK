package com.proxy.service.apm.info.monitor.performance.lag.ui.hook.frame

import android.app.Activity
import android.os.Handler
import android.view.Window
import androidx.annotation.RequiresApi
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.base.AbstractHook
import com.proxy.service.apm.info.monitor.performance.lag.ui.hook.FrameAvailableListener

/**
 * API 24+：仅注册 [Window.OnFrameMetricsAvailableListener]，每帧一次回调。
 */
@RequiresApi(24)
class FrameMetricsHook(
    private val activity: Activity,
    private val handler: Handler,
    private val listener: FrameAvailableListener,
) : AbstractHook<Any>() {

    companion object {
        private const val TAG = "${Constants.TAG}FrameMetricsHook"
    }

    private val windowListener = Window.OnFrameMetricsAvailableListener { _, metrics, _ ->
        if (metrics == null) {
            return@OnFrameMetricsAvailableListener
        }
        if (activity.isFinishing || activity.isDestroyed) {
            return@OnFrameMetricsAvailableListener
        }
        listener.onFrameMetrics(activity, metrics)
    }

    override fun start(t: Any?): Boolean {
        try {
            activity.window.addOnFrameMetricsAvailableListener(windowListener, handler)
        } catch (_: Throwable) {
        }
        return true
    }

    override fun stop(t: Any?) {
        try {
            activity.window.removeOnFrameMetricsAvailableListener(windowListener)
        } catch (_: Throwable) {
        }
    }
}
