package com.proxy.service.core.framework.app.worker.config.builder

import com.proxy.service.core.framework.app.worker.config.InitConfig
import com.proxy.service.core.framework.app.worker.enums.WorkPolicyEnum
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/12/16 10:07
 * @desc:
 */
interface IInitOnceTaskConfigBuilder {

    /**
     * 设置任务工作模式
     * */
    fun setWorkPolicy(workPolicy: WorkPolicyEnum): IInitOnceTaskConfigBuilder

    /**
     * 设置初始执行延迟时间
     */
    fun setDelayDuration(delayDuration: Long, unit: TimeUnit): IInitOnceTaskConfigBuilder

    /**
     * 创建配置
     * */
    fun build(): InitConfig
}