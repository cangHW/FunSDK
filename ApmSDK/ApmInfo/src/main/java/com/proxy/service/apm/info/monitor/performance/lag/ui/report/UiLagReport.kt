package com.proxy.service.apm.info.monitor.performance.lag.ui.report

import com.proxy.service.apm.info.monitor.performance.lag.ui.frame.BudgetSource
import com.proxy.service.apm.info.monitor.performance.lag.ui.frame.JankSeverity
import com.proxy.service.apm.info.monitor.performance.lag.ui.engine.FrameWatchEngine
import com.proxy.service.apm.info.monitor.performance.lag.ui.reporter.UiLagNotifyReporter

/**
 * UI 渲染单帧卡顿通知事件。
 *
 * 由 [FrameWatchEngine] 在 [JankSeverity.MODERATE] 及以上时构建，
 * 经 [UiLagNotifyReporter] 输出日志；[JankSeverity.SEVERE] 在 Debug 下可弹 Toast。
 *
 * @author: cangHX
 * @date: 2026/5/28
 */
data class UiLagReport(
    /** 发生卡顿的 Activity 类短名（`Activity.javaClass.simpleName`）。 */
    val activitySimpleName: String,
    /** 本帧卡顿严重度，通知路径上仅为 MODERATE 或 SEVERE。 */
    val severity: JankSeverity,
    /**
     * 耗时最高的渲染阶段标识，用于辅助定位瓶颈。
     *
     * 常见取值：`layoutMeasure`、`draw`、`commandIssue`、`gpu`、`total` 等。
     */
    val primaryBottleneck: String,
    /** 本帧总渲染耗时（毫秒），对应 [android.view.FrameMetrics.TOTAL_DURATION]。 */
    val totalDurationMs: Long,
    /** 超出帧预算的时长（毫秒），`totalDuration - frameBudget`；可为负表示提前完成。 */
    val frameOverrunMs: Long,
    /** 本帧有效渲染预算（毫秒），用于计算 overrun。 */
    val frameBudgetMs: Long,
    /** [frameBudgetMs] 的数据来源（系统 deadline / 刷新率 / 固定兜底）。 */
    val budgetSource: BudgetSource,
)
