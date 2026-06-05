package com.proxy.service.apm.info.monitor.performance.leak.report

import com.proxy.service.apm.info.monitor.performance.leak.mode.MemoryLeakMode
import com.proxy.service.apm.info.sampler.ICompositeSamplerData
import com.proxy.service.apm.info.monitor.performance.leak.composite.FullCompositeReporter
import com.proxy.service.apm.info.monitor.performance.leak.composite.LiteCompositeReporter
import com.proxy.service.apm.info.monitor.performance.leak.reporter.MemoryLeakFileReporter
import com.proxy.service.apm.info.monitor.performance.leak.reporter.MemoryLeakNotifyReporter
import com.proxy.service.apm.info.report.impl.CallbackReporter
import com.proxy.service.apm.info.monitor.performance.leak.heap.HeapDumper

/**
 * 单条泄漏实例摘要。
 *
 * FULL 模式下由 [HeapDumper] 从 Shark [shark.ApplicationLeak.leakTraces] 映射而来：Shark 可能将引用路径相同的多个实例合并为同一
 * signature，每条 [leakTrace] 对应一个泄漏实例（如 `ArrayList[0]`、`ArrayList[1]`）。
 *
 * hprof 分析失败或未 dump 时，[FullCompositeReporter]
 * 也会按 [RetainedObject] 构造 fallback 组，
 * 此时 [leakTrace] 为 null。
 *
 * @author: cangHX
 * @date: 2026/6/4 18:08
 */
class MemoryLeakGroup(
    /** Shark 泄漏签名；引用路径结构相同时各实例共享同一 signature。 */
    val signature: String,
    /** 泄漏对象的类名（如 `byte[]`、`com.example.MainActivity`）。 */
    val leakedClassName: String,
    /** 人类可读描述；FULL 成功时为 Shark labels 拼接，fallback 时为 watch 时的 description。 */
    val description: String,
    /** GC Root 到泄漏对象的引用链文本；fallback 或未分析时为 null。 */
    val leakTrace: String?
){
    /** 该泄漏类型涉及泄漏对象数量。 */
    var retainedCount: Int = 1
}

/**
 * 内存泄漏监测上报实体。
 *
 * 由 [FullCompositeReporter] 或 [LiteCompositeReporter] 构建，
 * 经 [MemoryLeakFileReporter]、[MemoryLeakNotifyReporter] 及业务 [CallbackReporter] 消费。
 *
 * @author: cangHX
 * @date: 2026/6/4 18:08
 */
class MemoryLeakReport(
    /** 本次报告对应的监测模式（FULL / LITE）。 */
    val mode: MemoryLeakMode,
    /** 分析说明。 */
    val description: String,
    /** 是否已完成 hprof dump 且 Shark 分析流程结束（含分析成功但无引用链的情况）。 */
    val hprofAnalyzed: Boolean = false,
    /** 本次 dump 检出的全部泄漏实例；LITE 为空，FULL fallback 时按 retained 对象逐条填充。 */
    val leakGroups: List<MemoryLeakGroup> = emptyList(),
    /** dump 时刻采集的设备/应用/内存采样快照；仅 FULL 成功分析路径填充。 */
    val samplerData: List<ICompositeSamplerData> = emptyList()
)
