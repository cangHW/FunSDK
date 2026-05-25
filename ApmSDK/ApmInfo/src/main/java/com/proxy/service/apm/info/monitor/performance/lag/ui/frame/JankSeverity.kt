package com.proxy.service.apm.info.monitor.performance.lag.ui.frame

/**
 * UI 渲染单帧卡顿严重度。
 *
 * 由 [FrameJankEvaluator] 根据帧预算 overrun 与 layout/draw 分项倍数得出；
 * MODERATE 及以上触发日志，SEVERE 在 Debug 下可 Toast。
 *
 * @author: cangHX
 * @date: 2026/5/28
 */
enum class JankSeverity {

    /** 未超预算，且分项未触发更高等级。 */
    NONE,

    /** 刚超过帧预算（missed deadline），overrun 在 MILD 倍数区间内。 */
    MILD,

    /** 明显卡顿：overrun 或 layout/draw 达到 MODERATE 倍数阈值。 */
    MODERATE,

    /** 严重卡顿：overrun 或 layout/draw 达到 SEVERE 倍数阈值，或命中绝对兜底。 */
    SEVERE,
}
