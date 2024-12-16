package com.proxy.service.core.framework.app.worker.config.builder

import com.proxy.service.core.framework.app.worker.enums.WorkPolicyEnum
import com.proxy.service.core.framework.app.worker.enums.WorkTypeEnum

/**
 * @author: cangHX
 * @data: 2024/12/16 10:07
 * @desc:
 */
interface IInitTaskConfigBuilderGet {

    /**
     * 获取任务类型
     * */
    fun getWorkType(): WorkTypeEnum

    /**
     * 获取任务添加模式
     * */
    fun getWorkPolicy(): WorkPolicyEnum

    /**
     * 获取初始执行延迟时间.
     */
    fun getDelayDuration(): Long

    /**
     * 获取循环执行间隔时间.
     */
    fun getLoopDuration(): Long

    /**
     * 获取循环任务的灵活执行时间.
     */
    fun getFlexDuration(): Long

}