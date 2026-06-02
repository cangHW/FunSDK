package com.proxy.service.apm.info.monitor.performance.lag.ui.hook.frame

import android.app.Activity
import android.os.Handler
import android.view.Window
import androidx.annotation.RequiresApi
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.lag.ui.hook.AbstractHook
import com.proxy.service.apm.info.monitor.performance.lag.ui.hook.FrameAvailableListener
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * API 24+：仅注册 [Window.OnFrameMetricsAvailableListener]，每帧一次回调。
 */
@RequiresApi(24)
class FrameMetricsHook(
    private val activity: Activity,
    private val handler: Handler,
    private val listener: FrameAvailableListener,
) : AbstractHook() {

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

    override fun install() {
        try {
            activity.window.addOnFrameMetricsAvailableListener(windowListener, handler)
        } catch (_: Throwable) {
        }
    }

    override fun uninstall() {
        try {
            activity.window.removeOnFrameMetricsAvailableListener(windowListener)
        } catch (_: Throwable) {
        }
    }
}
