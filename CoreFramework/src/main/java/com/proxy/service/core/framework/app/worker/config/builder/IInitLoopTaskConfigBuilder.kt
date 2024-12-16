package com.proxy.service.core.framework.app.worker.config.builder

import com.proxy.service.core.framework.app.worker.config.InitConfig
import com.proxy.service.core.framework.app.worker.enums.WorkPolicyEnum
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/12/16 10:07
 * @desc:
 */
interface IInitLoopTaskConfigBuilder {

    /**
     * 设置任务工作模式
     * */
    fun setWorkPolicy(workPolicy: WorkPolicyEnum): IInitLoopTaskConfigBuilder

    /**
     * 循环任务的灵活执行时间.
     * 灵活时间最短为 5 * 60 * 1000, 最大不超过当前循环间隔时间
     *
     * @desc 例如：如果你设置了一个循环任务，loopDuration 为 15 分钟，flexDuration 为 5 分钟。
     * 那么这个任务会在每个 15 分钟周期的最后 5 分钟内执行。
     * 也就是说，这个工作的执行时间是灵活的，可以在这最后 5 分钟内的任何时间点执行(在满足执行条件的前提下，如果当前不满足执行条件，会在这个灵活时间内等待条件满足)。
     * 用于提高执行成功率。
     */
    fun setFlexDuration(flexDuration: Long, unit: TimeUnit): IInitLoopTaskConfigBuilder

    /**
     * 创建配置
     * */
    fun build(): InitConfig
}