package com.proxy.service.apm.info.config.controller.performance.leak

import com.proxy.service.apm.info.config.controller.base.IBaseConfig
import com.proxy.service.apm.info.constants.Constants
import java.util.concurrent.TimeUnit

/**
 * 仅覆盖 Java/Kotlin 堆对象泄漏；Native 堆泄漏不在本配置范围内。
 *
 * @author: cangHX
 * @date: 2026/6/5
 */
interface IMemoryLeakConfig : IBaseConfig<IMemoryLeakConfig> {

    /**
     * 设置 destroy 后开始 GC 确认的等待时长。仅 `time > 0` 时生效。
     *
     * 默认 [Constants.MONITOR_MEMORY_LEAK_RETAINED_CHECK_DELAY_MS]
     */
    fun setRetainedCheckDelayMs(time: Long, unit: TimeUnit): IMemoryLeakConfig

    /**
     * 设置 [java.lang.ref.ReferenceQueue] 轮询间隔。仅 `time > 0` 时生效。
     *
     * 默认 [Constants.MONITOR_MEMORY_LEAK_REFERENCE_QUEUE_POLL_INTERVAL_MS]
     */
    fun setReferenceQueuePollIntervalMs(time: Long, unit: TimeUnit): IMemoryLeakConfig

    /**
     * 设置前台触发 hprof dump 的 retained 数量阈值。仅 `threshold > 0` 时生效。
     *
     * 默认 [Constants.MONITOR_MEMORY_LEAK_RETAINED_VISIBLE_THRESHOLD]
     */
    fun setRetainedVisibleThreshold(threshold: Int): IMemoryLeakConfig

    /**
     * 设置后台触发 hprof dump 的 retained 数量阈值。仅 `threshold > 0` 时生效。
     *
     * 默认 [Constants.MONITOR_MEMORY_LEAK_RETAINED_BACKGROUND_THRESHOLD]
     */
    fun setRetainedBackgroundThreshold(threshold: Int): IMemoryLeakConfig

    /**
     * 设置 Debug 泄漏 Toast 开关。
     *
     * 默认 `true`；Release 包通常走 LITE 路径，Toast 仍受 Debug 与 [IMemoryLeakConfigGet.getEnableDebugToast] 共同约束。
     */
    fun setEnableDebugToast(enable: Boolean): IMemoryLeakConfig

    /**
     * 构建实例
     */
    fun build(): MemoryLeakConfig
}
