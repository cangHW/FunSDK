package com.proxy.service.core.framework.app.worker.config

import com.proxy.service.core.framework.app.worker.config.builder.IRetryConfigBuilder
import com.proxy.service.core.framework.app.worker.config.builder.IRetryConfigBuilderGet
import com.proxy.service.core.framework.app.worker.enums.RetryPolicyEnum
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/12/13 18:41
 * @desc:
 */
class RetryConfig private constructor(private val builder: IRetryConfigBuilderGet) :
    IRetryConfigBuilderGet {

    override fun getRetryPolicy(): RetryPolicyEnum {
        return builder.getRetryPolicy()
    }

    override fun getRetryDelay(): Long {
        return builder.getRetryDelay()
    }

    override fun getRetryCount(): Int {
        return builder.getRetryCount()
    }

    companion object {
        fun builder(): IRetryConfigBuilder {
            return Builder()
        }
    }

    private class Builder : IRetryConfigBuilder, IRetryConfigBuilderGet {

        /**
         * 重试的策略
         */
        private var retryPolicy = RetryPolicyEnum.EXPONENTIAL

        /**
         * 重试的延迟时间
         */
        private var retryDelay: Long = 10

        /**
         * 重试的延迟时间类型
         */
        private var retryTimeUnit = TimeUnit.MINUTES

        /**
         * 重试次数
         */
        private var retryCount = 0

        override fun setRetryPolicy(retryPolicy: RetryPolicyEnum): IRetryConfigBuilder {
            this.retryPolicy = retryPolicy
            return this
        }

        override fun setRetryDelay(retryDelay: Long, unit: TimeUnit): IRetryConfigBuilder {
            this.retryDelay = retryDelay
            this.retryTimeUnit = unit
            return this
        }

        override fun setRetryCount(retryCount: Int): IRetryConfigBuilder {
            this.retryCount = retryCount
            return this
        }

        override fun build(): RetryConfig {
            return RetryConfig(this)
        }

        override fun getRetryPolicy(): RetryPolicyEnum {
            return retryPolicy
        }

        override fun getRetryDelay(): Long {
            return retryTimeUnit.toMillis(retryDelay)
        }

        override fun getRetryCount(): Int {
            return retryCount
        }
    }

}