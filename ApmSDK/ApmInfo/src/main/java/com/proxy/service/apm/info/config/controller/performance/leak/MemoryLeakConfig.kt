package com.proxy.service.apm.info.config.controller.performance.leak

import com.proxy.service.apm.info.config.controller.base.BaseConfig
import com.proxy.service.apm.info.constants.Constants
import java.util.concurrent.TimeUnit

/**
 * Java 内存泄漏监控运行时配置。
 */
class MemoryLeakConfig private constructor(
    private val config: IMemoryLeakConfigGet
) : IMemoryLeakConfigGet by config {

    companion object {
        fun builder(): IMemoryLeakConfig = Builder()
    }

    class Builder : BaseConfig<IMemoryLeakConfig>(), IMemoryLeakConfig, IMemoryLeakConfigGet {

        private var retainedCheckDelayMs = Constants.MONITOR_MEMORY_LEAK_RETAINED_CHECK_DELAY_MS
        private var referenceQueuePollIntervalMs =
            Constants.MONITOR_MEMORY_LEAK_REFERENCE_QUEUE_POLL_INTERVAL_MS
        private var retainedVisibleThreshold = Constants.MONITOR_MEMORY_LEAK_RETAINED_VISIBLE_THRESHOLD
        private var retainedBackgroundThreshold =
            Constants.MONITOR_MEMORY_LEAK_RETAINED_BACKGROUND_THRESHOLD
        private var enableDebugToast = true

        override fun getInstance(): IMemoryLeakConfig = this

        override fun setRetainedCheckDelayMs(time: Long, unit: TimeUnit): IMemoryLeakConfig {
            if (time > 0) {
                retainedCheckDelayMs = unit.toMillis(time)
            }
            return this
        }

        override fun setReferenceQueuePollIntervalMs(time: Long, unit: TimeUnit): IMemoryLeakConfig {
            if (time > 0) {
                referenceQueuePollIntervalMs = unit.toMillis(time)
            }
            return this
        }

        override fun setRetainedVisibleThreshold(threshold: Int): IMemoryLeakConfig {
            if (threshold > 0) {
                retainedVisibleThreshold = threshold
            }
            return this
        }

        override fun setRetainedBackgroundThreshold(threshold: Int): IMemoryLeakConfig {
            if (threshold > 0) {
                retainedBackgroundThreshold = threshold
            }
            return this
        }

        override fun setEnableDebugToast(enable: Boolean): IMemoryLeakConfig {
            enableDebugToast = enable
            return this
        }

        override fun build(): MemoryLeakConfig = MemoryLeakConfig(this)

        override fun getRetainedCheckDelayMs(): Long = retainedCheckDelayMs

        override fun getReferenceQueuePollIntervalMs(): Long = referenceQueuePollIntervalMs

        override fun getRetainedVisibleThreshold(): Int = retainedVisibleThreshold

        override fun getRetainedBackgroundThreshold(): Int = retainedBackgroundThreshold

        override fun getEnableDebugToast(): Boolean = enableDebugToast
    }
}
