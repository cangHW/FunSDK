package com.proxy.service.apm.info.monitor.performance.lag.ui.engine

import android.app.Activity
import android.os.Build
import android.os.Debug
import android.view.FrameMetrics
import androidx.annotation.RequiresApi
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.lag.ui.report.FrameCoreMetrics
import com.proxy.service.apm.info.monitor.performance.lag.ui.report.FrameMetricsSnapshot
import com.proxy.service.apm.info.monitor.performance.lag.ui.report.UiLagReport
import com.proxy.service.apm.info.monitor.performance.lag.ui.frame.FrameBudget
import com.proxy.service.apm.info.monitor.performance.lag.ui.frame.FrameBudgetResolver
import com.proxy.service.apm.info.monitor.performance.lag.ui.frame.FrameJankEvaluator
import com.proxy.service.apm.info.monitor.performance.lag.ui.frame.JankSeverity
import com.proxy.service.apm.info.monitor.performance.lag.ui.hook.FrameAvailableListener
import com.proxy.service.apm.info.report.IReporter
import java.util.concurrent.TimeUnit

/**
 * UI 渲染卡顿引擎：Core 快判后，仅 MODERATE+ 全量解析并通知 Reporter。
 */
class FrameWatchEngine(
    private val reporter: IReporter<UiLagReport>,
) : FrameAvailableListener {

    companion object {
        private fun nsToMs(ns: Long): Long = TimeUnit.NANOSECONDS.toMillis(ns)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onFrameMetrics(activity: Activity, metrics: FrameMetrics) {
        if (shouldSkip(activity)) {
            return
        }
        val core = FrameCoreMetrics.from(metrics)
        if (core.firstDrawFrame && !Constants.MONITOR_UI_LAG_REPORT_FIRST_DRAW_FRAME) {
            return
        }
        val budget = FrameBudgetResolver.resolve(core.deadlineNs, activity)
        val severity = FrameJankEvaluator.quickSeverity(core, budget)
        if (severity.ordinal < JankSeverity.MODERATE.ordinal) {
            return
        }
        val snapshot = FrameMetricsSnapshot.from(metrics)
        publishJank(activity, core, budget, severity, snapshot)
    }

    override fun onFrameInterval(activity: Activity, intervalNs: Long) {
        if (shouldSkip(activity)) {
            return
        }
        val core = FrameCoreMetrics.fromInterval(intervalNs)
        val budget = FrameBudgetResolver.resolve(core.deadlineNs, activity)
        val severity = FrameJankEvaluator.quickSeverity(core, budget)
        if (severity.ordinal < JankSeverity.MODERATE.ordinal) {
            return
        }
        val snapshot = FrameMetricsSnapshot.fromFrameIntervalNs(intervalNs)
        publishJank(activity, core, budget, severity, snapshot)
    }

    private fun shouldSkip(activity: Activity): Boolean {
        return Debug.isDebuggerConnected()
            || activity.isFinishing
            || activity.isDestroyed
    }

    private fun publishJank(
        activity: Activity,
        core: FrameCoreMetrics,
        budget: FrameBudget,
        severity: JankSeverity,
        snapshot: FrameMetricsSnapshot,
    ) {
        val bottleneck = FrameJankEvaluator.resolvePrimaryBottleneck(snapshot, budget.budgetNs)
        val event = UiLagReport(
            activitySimpleName = activity.javaClass.simpleName,
            severity = severity,
            primaryBottleneck = bottleneck,
            totalDurationMs = nsToMs(core.totalDurationNs),
            frameOverrunMs = nsToMs(FrameJankEvaluator.overrunNs(core, budget)),
            frameBudgetMs = nsToMs(budget.budgetNs),
            budgetSource = budget.source,
        )
        reporter.publish(System.currentTimeMillis(), event)
    }
}
