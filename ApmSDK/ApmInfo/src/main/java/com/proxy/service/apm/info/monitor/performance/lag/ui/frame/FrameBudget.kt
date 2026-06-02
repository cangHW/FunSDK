package com.proxy.service.apm.info.monitor.performance.lag.ui.frame

import com.proxy.service.apm.info.monitor.performance.lag.ui.report.FrameMetricsSnapshot

/**
 * 单帧渲染时间预算，用于计算 overrun 与 [JankSeverity]。
 *
 * @author: cangHX
 * @date: 2026/5/28
 */
data class FrameBudget(
    /**
     * 本帧有效预算（纳秒）。
     *
     * 与 [FrameMetricsSnapshot.totalDurationNs]
     * 比较得 `overrunNs = total - budgetNs`。
     */
    val budgetNs: Long,
    /**
     * 预算来源，便于报告区分系统 deadline 与降级估算。
     */
    val source: BudgetSource,
)
