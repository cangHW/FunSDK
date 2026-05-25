package com.proxy.service.apm.info.monitor.performance.lag.ui.bean

import android.os.Build
import android.view.FrameMetrics
import androidx.annotation.RequiresApi

/**
 * [FrameMetrics] 快照：判级与瓶颈分析所需的耗时分项（纳秒）。
 */
class FrameMetricsSnapshot(
    val totalDurationNs: Long,
    val unknownDelayDurationNs: Long,
    val inputHandlingDurationNs: Long,
    val animationDurationNs: Long,
    val layoutMeasureDurationNs: Long,
    val drawDurationNs: Long,
    val commandIssueDurationNs: Long,
    val swapBuffersDurationNs: Long,
    val syncDurationNs: Long,
    val deadlineNs: Long,
    val gpuDurationNs: Long,
    val firstDrawFrame: Boolean,
) {
    companion object {

        @RequiresApi(Build.VERSION_CODES.N)
        fun from(metrics: FrameMetrics): FrameMetricsSnapshot {
            val api31 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            val api34 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
            return FrameMetricsSnapshot(
                totalDurationNs = metrics.getMetric(FrameMetrics.TOTAL_DURATION),
                unknownDelayDurationNs = metrics.getMetric(FrameMetrics.UNKNOWN_DELAY_DURATION),
                inputHandlingDurationNs = metrics.getMetric(FrameMetrics.INPUT_HANDLING_DURATION),
                animationDurationNs = metrics.getMetric(FrameMetrics.ANIMATION_DURATION),
                layoutMeasureDurationNs = metrics.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION),
                drawDurationNs = metrics.getMetric(FrameMetrics.DRAW_DURATION),
                commandIssueDurationNs = metrics.getMetric(FrameMetrics.COMMAND_ISSUE_DURATION),
                swapBuffersDurationNs = metrics.getMetric(FrameMetrics.SWAP_BUFFERS_DURATION),
                syncDurationNs = metrics.getMetric(FrameMetrics.SYNC_DURATION),
                deadlineNs = if (api31) {
                    metrics.getMetric(FrameMetrics.DEADLINE)
                } else {
                    0L
                },
                gpuDurationNs = if (api34) {
                    metrics.getMetric(FrameMetrics.GPU_DURATION)
                } else {
                    0L
                },
                firstDrawFrame = api31 && metrics.getMetric(FrameMetrics.FIRST_DRAW_FRAME) != 0L,
            )
        }

        /**
         * API 24 以下 Choreographer 仅能提供帧间隔
         */
        fun fromFrameIntervalNs(intervalNs: Long) = FrameMetricsSnapshot(
            totalDurationNs = intervalNs,
            unknownDelayDurationNs = 0,
            inputHandlingDurationNs = 0,
            animationDurationNs = 0,
            layoutMeasureDurationNs = 0,
            drawDurationNs = 0,
            commandIssueDurationNs = 0,
            swapBuffersDurationNs = 0,
            syncDurationNs = 0,
            deadlineNs = 0,
            gpuDurationNs = 0,
            firstDrawFrame = false,
        )
    }
}
