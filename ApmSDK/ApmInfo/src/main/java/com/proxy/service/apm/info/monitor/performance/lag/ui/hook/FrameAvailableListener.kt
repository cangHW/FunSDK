package com.proxy.service.apm.info.monitor.performance.lag.ui.hook

import android.app.Activity
import android.view.FrameMetrics

/**
 * 单帧渲染指标就绪回调。
 */
interface FrameAvailableListener {

    fun onFrameMetrics(activity: Activity, metrics: FrameMetrics)

    /**
     * API 24 以下 Choreographer 降级：相邻 VSYNC 间隔（纳秒）。
     */
    fun onFrameInterval(activity: Activity, intervalNs: Long)
}
