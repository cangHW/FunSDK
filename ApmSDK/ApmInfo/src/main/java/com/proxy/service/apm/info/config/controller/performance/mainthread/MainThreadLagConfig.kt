package com.proxy.service.apm.info.config.controller.performance.mainthread

import com.proxy.service.apm.info.config.controller.base.BaseConfig
import com.proxy.service.apm.info.constants.Constants
import java.util.concurrent.TimeUnit

/**
 * 运行时配置（阈值、采样间隔、缓存策略等）。
 *
 * @author: cangHX
 * @date: 2026/5/22 14:20
 */
class MainThreadLagConfig private constructor(
    private val config: IMainThreadLagConfigGet
) : IMainThreadLagConfigGet by config {

    companion object {
        fun builder(): IMainThreadLagConfig {
            return Builder()
        }
    }

    class Builder : BaseConfig<IMainThreadLagConfig>(), IMainThreadLagConfig, IMainThreadLagConfigGet {

        private var blockThresholdMs = Constants.MONITOR_PERF_MAIN_THREAD_LAG_WALL_TIME

        override fun setBlockThresholdTime(time: Long, unit: TimeUnit): IMainThreadLagConfig {
            if (time > 0) {
                blockThresholdMs = unit.toMillis(time)
            }
            return this
        }

        override fun build(): MainThreadLagConfig {
            return MainThreadLagConfig(this)
        }

        override fun getInstance(): IMainThreadLagConfig {
            return this
        }

        override fun getBlockThresholdTime(): Long {
            return blockThresholdMs
        }

    }
}