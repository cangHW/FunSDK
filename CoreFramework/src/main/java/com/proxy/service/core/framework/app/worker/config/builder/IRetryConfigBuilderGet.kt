package com.proxy.service.core.framework.app.worker.config.builder

import com.proxy.service.core.framework.app.worker.enums.RetryPolicyEnum

/**
 * @author: cangHX
 * @data: 2024/12/13 18:34
 * @desc:
 */
interface IRetryConfigBuilderGet {

    /**
     * 获取重试的延迟模式
     * */
    fun getRetryPolicy(): RetryPolicyEnum

    /**
     * 获取重试的延迟时间, 单位：毫秒
     * */
    fun getRetryDelay(): Long

    /**
     * 获取重试次数
     * */
    fun getRetryCount(): Int
}