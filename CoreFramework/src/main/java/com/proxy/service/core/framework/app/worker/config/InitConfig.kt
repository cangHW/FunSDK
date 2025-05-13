package com.proxy.service.core.framework.app.worker.config

import androidx.work.PeriodicWorkRequest
import com.proxy.service.core.framework.app.worker.config.builder.IInitLoopTaskConfigBuilder
import com.proxy.service.core.framework.app.worker.config.builder.IInitOnceTaskConfigBuilder
import com.proxy.service.core.framework.app.worker.config.builder.IInitTaskConfigBuilderGet
import com.proxy.service.core.framework.app.worker.enums.WorkPolicyEnum
import com.proxy.service.core.framework.app.worker.enums.WorkTypeEnum
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/12/16 10:16
 * @desc:
 */
class InitConfig private constructor(
    private val builder: IInitTaskConfigBuilderGet
) : IInitTaskConfigBuilderGet {

    override fun getWorkType(): WorkTypeEnum {
        return builder.getWorkType()
    }

    override fun getWorkPolicy(): WorkPolicyEnum {
        return builder.getWorkPolicy()
    }

    override fun getDelayDuration(): Long {
        return builder.getDelayDuration()
    }

    override fun getLoopDuration(): Long {
        return builder.getLoopDuration()
    }

    override fun getFlexDuration(): Long {
        return builder.getFlexDuration()
    }

    companion object {
        /**
         * 创建单次任务配置构建器
         * */
        fun builderOnceTask(): IInitOnceTaskConfigBuilder {
            return OnceTaskBuilder(WorkTypeEnum.ONCE)
        }

        /**
         * 创建循环任务配置构建器.
         *
         * 循环任务的间隔时间最短为 15 * 60 * 1000
         * */
        fun builderLoopTask(
            intervalTime: Long,
            unit: TimeUnit
        ): IInitLoopTaskConfigBuilder {
            val interval = Math.max(
                unit.toMillis(intervalTime),
                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS
            )
            return LoopTaskBuilder(WorkTypeEnum.LOOP, interval)
        }
    }

    private open class BaseBuilder(private val type: WorkTypeEnum) : IInitTaskConfigBuilderGet {

        /**
         * 任务添加模式
         * */
        protected var workPolicyMode: WorkPolicyEnum = WorkPolicyEnum.REPLACE

        /**
         * 初始执行延迟时间
         */
        protected var delayTime: Long = 0

        /**
         * 循环执行间隔时间
         */
        protected var loopTime: Long = 0

        /**
         * 循环执行的灵活执行时间。
         */
        protected var flexTime: Long = 0

        override fun getWorkType(): WorkTypeEnum {
            return type
        }

        override fun getWorkPolicy(): WorkPolicyEnum {
            return workPolicyMode
        }

        override fun getDelayDuration(): Long {
            return delayTime
        }

        override fun getLoopDuration(): Long {
            return loopTime
        }

        override fun getFlexDuration(): Long {
            return flexTime
        }
    }

    private class OnceTaskBuilder(type: WorkTypeEnum) : BaseBuilder(type),
        IInitOnceTaskConfigBuilder {

        override fun setWorkPolicy(workPolicy: WorkPolicyEnum): IInitOnceTaskConfigBuilder {
            this.workPolicyMode = workPolicy
            return this
        }

        override fun setDelayDuration(
            delayDuration: Long,
            unit: TimeUnit
        ): IInitOnceTaskConfigBuilder {
            if (delayDuration >= 0) {
                this.delayTime = unit.toMillis(delayDuration)
            }
            return this
        }

        override fun build(): InitConfig {
            return InitConfig(this)
        }
    }

    private class LoopTaskBuilder(
        type: WorkTypeEnum,
        intervalTime: Long
    ) : BaseBuilder(type), IInitLoopTaskConfigBuilder {

        init {
            this.loopTime = intervalTime
        }

        override fun setWorkPolicy(workPolicy: WorkPolicyEnum): IInitLoopTaskConfigBuilder {
            this.workPolicyMode = workPolicy
            return this
        }

        override fun setFlexDuration(
            flexDuration: Long,
            unit: TimeUnit
        ): IInitLoopTaskConfigBuilder {
            if (flexDuration >= 0) {
                var time = Math.max(
                    unit.toMillis(flexDuration),
                    PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS
                )
                time = Math.min(
                    time,
                    loopTime
                )
                this.flexTime = time
            }
            return this
        }

        override fun build(): InitConfig {
            return InitConfig(this)
        }
    }

}