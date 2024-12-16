package com.proxy.service.core.framework.app.worker.config.builder

import com.proxy.service.core.framework.app.worker.config.RetryConfig
import com.proxy.service.core.framework.app.worker.enums.RetryPolicyEnum
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/12/13 18:34
 * @desc:
 */
interface IRetryConfigBuilder {

    /**
     * 重试的延迟模式
     * */
    fun setRetryPolicy(retryPolicy: RetryPolicyEnum): IRetryConfigBuilder

    /**
     * 重试的延迟时间 [10 * 1000, 5 * 60 * 60 * 1000]
     * */
    fun setRetryDelay(retryDelay: Long, unit: TimeUnit): IRetryConfigBuilder

    /**
     * 重试次数
     * */
    fun setRetryCount(retryCount: Int): IRetryConfigBuilder

    /**
     * 创建配置
     * */
    fun build(): RetryConfig
}