package com.proxy.service.apm.info.config.controller.performance.leak

import com.proxy.service.apm.info.config.controller.base.IBaseConfigGet
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.leak.MemoryLeakMonitor
import com.proxy.service.apm.info.monitor.performance.leak.composite.FullCompositeReporter
import com.proxy.service.apm.info.monitor.performance.leak.watcher.ObjectWatcher

/**
 * [MemoryLeakConfig] 只读访问接口。
 *
 * 由 [MemoryLeakMonitor]、[ObjectWatcher]、[FullCompositeReporter] 及 Reporter 层读取。
 *
 * 检测链路：Activity/Fragment [android.app.Activity.onDestroy] 后弱引用 watch →
 * 延迟 [getRetainedCheckDelayMs] → GC 确认 retained →
 * Debug 包走 FULL（hprof + Shark 引用链），Release 包走 LITE（类名/描述告警）。
 *
 * 总开关见 [IBaseConfigGet.getEnable]；FULL/LITE 由 `CsAppUtils.isDebuggable()` 决定，不在本接口配置。
 *
 * @author: cangHX
 * @date: 2026/6/5
 */
interface IMemoryLeakConfigGet : IBaseConfigGet {

    /**
     * 对象进入 watch 后，需等待的时长（毫秒），再触发 GC 确认是否 retained。
     *
     * 供 [ObjectWatcher.checkDelayedRetained] 使用。过短易误报，过长则发现偏晚。
     * 默认 [Constants.MONITOR_MEMORY_LEAK_RETAINED_CHECK_DELAY_MS]
     */
    fun getRetainedCheckDelayMs(): Long

    /**
     * [ObjectWatcher] 轮询 [java.lang.ref.ReferenceQueue] 的间隔（毫秒）。
     *
     * 影响已正常回收的对象从 watch 列表移除的及时性。
     * 默认 [Constants.MONITOR_MEMORY_LEAK_REFERENCE_QUEUE_POLL_INTERVAL_MS]
     */
    fun getReferenceQueuePollIntervalMs(): Long

    /**
     * FULL 模式、应用处于前台时，累计 retained 数量达到该阈值才触发 hprof dump。
     *
     * 供 [FullCompositeReporter.maybeDump] 使用，避免前台频繁 dump 卡顿。
     * 默认 [Constants.MONITOR_MEMORY_LEAK_RETAINED_VISIBLE_THRESHOLD]
     */
    fun getRetainedVisibleThreshold(): Int

    /**
     * FULL 模式、应用处于后台时，累计 retained 数量达到该阈值即触发 hprof dump。
     *
     * 进后台时 [FullCompositeReporter.whenAppBackground] 会重新评估 pending 列表。
     * 默认 [Constants.MONITOR_MEMORY_LEAK_RETAINED_BACKGROUND_THRESHOLD]
     */
    fun getRetainedBackgroundThreshold(): Int

    /**
     * Debug 环境下检测到泄漏时是否弹出 Toast。
     *
     * 由 [com.proxy.service.apm.info.monitor.performance.leak.reporter.MemoryLeakToastHelper] 消费；
     * 仍受 [Constants.MONITOR_MEMORY_LEAK_NOTIFY_THROTTLE_MS] 限流，日志始终输出。
     */
    fun getEnableDebugToast(): Boolean
}
