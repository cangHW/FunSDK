package com.proxy.service.apm.info.config.controller.performance.mainthread

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.config.controller.base.IBaseConfig
import java.util.concurrent.TimeUnit

/**
 * 配置构建接口。
 *
 * @author: cangHX
 * @date: 2026/5/22 14:41
 */
interface IMainThreadLagConfig : IBaseConfig<IMainThreadLagConfig> {

    /**
     * 判定为慢分发的墙钟耗时阈值，默认 [Constants.MONITOR_PERF_MAIN_THREAD_LAG_WALL_TIME]。
     */
    fun setBlockThresholdTime(time: Long, unit: TimeUnit): IMainThreadLagConfig

    /**
     * 构建
     * */
    fun build(): MainThreadLagConfig

}