package com.proxy.service.core.framework.app.worker.config.builder

import com.proxy.service.core.framework.app.worker.config.ConstraintConfig
import com.proxy.service.core.framework.app.worker.enums.NetworkTypeEnum

/**
 * @author: cangHX
 * @data: 2024/12/13 18:49
 * @desc:
 */
interface IConstraintConfigBuilder {

    /**
     * 设置执行需要的网络状态
     * */
    fun setRequiredNetworkType(requiredNetworkType: NetworkTypeEnum): IConstraintConfigBuilder

    /**
     * 设置是否必须在设备充电模式下执行
     */
    fun setRequiresCharging(requiresCharging: Boolean): IConstraintConfigBuilder

    /**
     * 设置是否必须在设备空闲模式下执行
     */
    fun setRequiresDeviceIdle(requiresDeviceIdle: Boolean): IConstraintConfigBuilder

    /**
     * 设置是否必须在设备不处于低电量模式时执行
     */
    fun setRequiresBatteryNotLow(requiresBatteryNotLow: Boolean): IConstraintConfigBuilder

    /**
     * 设置是否必须在设备不处于低存储空间模式时执行
     * */
    fun setRequiresStorageNotLow(requiresStorageNotLow: Boolean): IConstraintConfigBuilder

    /**
     * 创建配置
     * */
    fun build(): ConstraintConfig
}