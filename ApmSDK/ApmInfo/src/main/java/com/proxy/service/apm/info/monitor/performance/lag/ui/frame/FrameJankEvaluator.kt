package com.proxy.service.apm.info.monitor.performance.lag.ui.frame

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.lag.ui.report.FrameCoreMetrics
import com.proxy.service.apm.info.monitor.performance.lag.ui.report.FrameMetricsSnapshot
import java.util.concurrent.TimeUnit

/**
 * 基于帧预算 overrun 与分项倍数判定卡顿严重度。
 */
object FrameJankEvaluator {

    fun quickSeverity(core: FrameCoreMetrics, budget: FrameBudget): JankSeverity {
        val budgetNs = budget.budgetNs
        val overrunNs = core.totalDurationNs - budgetNs
        val missedDeadline = overrunNs > 0L

        var severity = severityFromOverrun(overrunNs, budgetNs)
        severity = higherSeverity(
            severity,
            severityFromPhases(core.layoutMeasureDurationNs, core.drawDurationNs, budgetNs),
        )

        if (!missedDeadline && severity == JankSeverity.NONE) {
            severity = JankSeverity.NONE
        } else if (severity == JankSeverity.NONE && missedDeadline) {
            severity = JankSeverity.MILD
        }

        val floorMs = Constants.MONITOR_UI_LAG_ABSOLUTE_SEVERE_FLOOR_MS
        if (floorMs > 0L) {
            val totalMs = TimeUnit.NANOSECONDS.toMillis(core.totalDurationNs)
            if (totalMs >= floorMs) {
                severity = higherSeverity(severity, JankSeverity.SEVERE)
            }
        }
        return severity
    }

    fun overrunNs(core: FrameCoreMetrics, budget: FrameBudget): Long {
        return core.totalDurationNs - budget.budgetNs
    }

    fun resolvePrimaryBottleneck(snapshot: FrameMetricsSnapshot, budgetNs: Long): String {
        val candidates = linkedMapOf(
            "layoutMeasure" to snapshot.layoutMeasureDurationNs,
            "draw" to snapshot.drawDurationNs,
            "unknownDelay" to snapshot.unknownDelayDurationNs,
            "input" to snapshot.inputHandlingDurationNs,
            "animation" to snapshot.animationDurationNs,
            "commandIssue" to snapshot.commandIssueDurationNs,
            "sync" to snapshot.syncDurationNs,
            "swapBuffers" to snapshot.swapBuffersDurationNs,
            "gpu" to snapshot.gpuDurationNs,
            "total" to snapshot.totalDurationNs,
        )
        val maxEntry = candidates.maxByOrNull { it.value } ?: return "total"
        if (maxEntry.key == "total" && snapshot.totalDurationNs > budgetNs) {
            return "total"
        }
        return maxEntry.key
    }

    private fun severityFromOverrun(
        overrunNs: Long,
        budgetNs: Long,
    ): JankSeverity {
        if (overrunNs <= 0L) {
            return JankSeverity.NONE
        }
        val mildLimit = (budgetNs * Constants.MONITOR_UI_LAG_MILD_OVERRUN_MULTIPLIER).toLong()
        val moderateLimit = (budgetNs * Constants.MONITOR_UI_LAG_MODERATE_OVERRUN_MULTIPLIER).toLong()
        val severeLimit = (budgetNs * Constants.MONITOR_UI_LAG_SEVERE_OVERRUN_MULTIPLIER).toLong()
        return when {
            overrunNs > severeLimit -> JankSeverity.SEVERE
            overrunNs > moderateLimit -> JankSeverity.MODERATE
            overrunNs > mildLimit -> JankSeverity.MODERATE
            else -> JankSeverity.MILD
        }
    }

    private fun severityFromPhases(
        layoutNs: Long,
        drawNs: Long,
        budgetNs: Long,
    ): JankSeverity {
        val moderateLimit = (budgetNs * Constants.MONITOR_UI_LAG_MODERATE_PHASE_MULTIPLIER).toLong()
        val severeLimit = (budgetNs * Constants.MONITOR_UI_LAG_SEVERE_PHASE_MULTIPLIER).toLong()
        var severity = JankSeverity.NONE
        for (duration in longArrayOf(layoutNs, drawNs)) {
            if (duration <= 0L) {
                continue
            }
            severity = higherSeverity(
                severity,
                when {
                    duration > severeLimit -> JankSeverity.SEVERE
                    duration > moderateLimit -> JankSeverity.MODERATE
                    duration > budgetNs -> JankSeverity.MILD
                    else -> JankSeverity.NONE
                },
            )
        }
        return severity
    }

    private fun higherSeverity(a: JankSeverity, b: JankSeverity): JankSeverity {
        return if (a.ordinal >= b.ordinal) a else b
    }
}
