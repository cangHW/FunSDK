package com.proxy.service.apm.info.monitor.performance.lag.ui.bean

import android.os.Build
import android.view.FrameMetrics
import androidx.annotation.RequiresApi

/**
 * FrameMetrics 快判所需核心字段（每帧读取，用于 MODERATE+ 门槛判断）。
 */
data class FrameCoreMetrics(
    val totalDurationNs: Long,
    val layoutMeasureDurationNs: Long,
    val drawDurationNs: Long,
    val deadlineNs: Long,
    val firstDrawFrame: Boolean,
) {
    companion object {

        @RequiresApi(Build.VERSION_CODES.N)
        fun from(metrics: FrameMetrics): FrameCoreMetrics {
            val api31 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            return FrameCoreMetrics(
                totalDurationNs = metrics.getMetric(FrameMetrics.TOTAL_DURATION),
                layoutMeasureDurationNs = metrics.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION),
                drawDurationNs = metrics.getMetric(FrameMetrics.DRAW_DURATION),
                deadlineNs = if (api31) {
                    metrics.getMetric(FrameMetrics.DEADLINE)
                } else {
                    0L
                },
                firstDrawFrame = api31 && metrics.getMetric(FrameMetrics.FIRST_DRAW_FRAME) != 0L,
            )
        }

        fun fromInterval(intervalNs: Long): FrameCoreMetrics {
            return FrameCoreMetrics(
                totalDurationNs = intervalNs,
                layoutMeasureDurationNs = 0L,
                drawDurationNs = 0L,
                deadlineNs = 0L,
                firstDrawFrame = false,
            )
        }
    }
}
