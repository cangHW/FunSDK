package com.proxy.service.apm.info.monitor.performance.lag.ui.frame

import com.proxy.service.apm.info.constants.Constants
import android.view.FrameMetrics
import android.view.Display

/**
 * 单帧有效渲染预算 [FrameBudget.budgetNs] 的数据来源。
 *
 * 解析顺序见 [FrameBudgetResolver]：DEADLINE → 刷新率 → 固定兜底。
 *
 * @author: cangHX
 * @date: 2026/5/28
 */
enum class BudgetSource {

    /** API 31+ [FrameMetrics.DEADLINE] 指标（与系统本帧预算一致）。 */
    DEADLINE_METRIC,

    /** 由 [Display.getRefreshRate] 推算的单帧周期（纳秒）。 */
    REFRESH_RATE,

    /** 无 deadline 且无法读取刷新率时，使用 [Constants.MONITOR_UI_LAG_FALLBACK_BUDGET_NS]。 */
    FIXED_FALLBACK,
}
