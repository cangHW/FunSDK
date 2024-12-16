package com.proxy.service.core.framework.app.worker.config

import com.proxy.service.core.framework.app.worker.config.builder.IConstraintConfigBuilder
import com.proxy.service.core.framework.app.worker.config.builder.IConstraintConfigBuilderGet
import com.proxy.service.core.framework.app.worker.enums.NetworkTypeEnum

/**
 * @author: cangHX
 * @data: 2024/12/13 18:58
 * @desc:
 */
class ConstraintConfig private constructor(
    private val builder: IConstraintConfigBuilderGet
) : IConstraintConfigBuilderGet {

    override fun getRequiredNetworkType(): NetworkTypeEnum {
        return builder.getRequiredNetworkType()
    }

    override fun getRequiresCharging(): Boolean {
        return builder.getRequiresCharging()
    }

    override fun getRequiresDeviceIdle(): Boolean {
        return builder.getRequiresDeviceIdle()
    }

    override fun getRequiresBatteryNotLow(): Boolean {
        return builder.getRequiresBatteryNotLow()
    }

    override fun getRequiresStorageNotLow(): Boolean {
        return builder.getRequiresStorageNotLow()
    }

    companion object {
        fun builder(): IConstraintConfigBuilder {
            return Builder()
        }
    }

    private class Builder : IConstraintConfigBuilder, IConstraintConfigBuilderGet {

        /**
         * 必须在对应的网络状态下执行
         */
        private var requiredNetworkType: NetworkTypeEnum = NetworkTypeEnum.NOT_REQUIRED

        /**
         * 是否必须在设备充电模式下执行
         */
        private var requiresCharging = false

        /**
         * 是否必须在设备空闲模式下执行
         */
        private var requiresDeviceIdle = false

        /**
         * 是否必须在设备不处于低电量模式时执行
         */
        private var requiresBatteryNotLow = false

        /**
         * 是否必须在设备不处于低存储空间模式时执行
         */
        private var requiresStorageNotLow = false

        override fun setRequiredNetworkType(requiredNetworkType: NetworkTypeEnum): IConstraintConfigBuilder {
            this.requiredNetworkType = requiredNetworkType
            return this
        }

        override fun setRequiresCharging(requiresCharging: Boolean): IConstraintConfigBuilder {
            this.requiresCharging = requiresCharging
            return this
        }

        override fun setRequiresDeviceIdle(requiresDeviceIdle: Boolean): IConstraintConfigBuilder {
            this.requiresDeviceIdle = requiresDeviceIdle
            return this
        }

        override fun setRequiresBatteryNotLow(requiresBatteryNotLow: Boolean): IConstraintConfigBuilder {
            this.requiresBatteryNotLow = requiresBatteryNotLow
            return this
        }

        override fun setRequiresStorageNotLow(requiresStorageNotLow: Boolean): IConstraintConfigBuilder {
            this.requiresStorageNotLow = requiresStorageNotLow
            return this
        }

        override fun build(): ConstraintConfig {
            return ConstraintConfig(this)
        }

        override fun getRequiredNetworkType(): NetworkTypeEnum {
            return requiredNetworkType
        }

        override fun getRequiresCharging(): Boolean {
            return requiresCharging
        }

        override fun getRequiresDeviceIdle(): Boolean {
            return requiresDeviceIdle
        }

        override fun getRequiresBatteryNotLow(): Boolean {
            return requiresBatteryNotLow
        }

        override fun getRequiresStorageNotLow(): Boolean {
            return requiresStorageNotLow
        }

    }

}