package com.proxy.service.apm.info.monitor.performance.lag.mainthread.report

import com.proxy.service.apm.info.sampler.ICompositeSamplerData

/**
 * 主线程慢分发报告数据模型。
 *
 * @author: cangHX
 * @date: 2026/5/22 19:01
 */
class MainThreadLagReport(
    /**
     * 单次 dispatch 墙钟耗时（毫秒），用户可感知的阻塞时长。
     */
    val wallDurationMs: Long,
    /**
     * 主线程 CPU 时间耗时（毫秒），辅助区分计算密集与 IO/锁等待
     * */
    val cpuDurationMs: Long,
    /**
     * dispatch 开始时间戳（System.currentTimeMillis）。
     */
    val startTimeMs: Long,
    /**
     * dispatch 结束时间戳。
     */
    val endTimeMs: Long,
    /**
     * 本次 dispatch 时间窗内的采样数据。
     */
    val samplerData: List<ICompositeSamplerData>,
    /**
     * Message 摘要（Observer 为 msg.toString，Printer 为 AOSP 日志行）
     * */
    val messageHint: String? = null
)