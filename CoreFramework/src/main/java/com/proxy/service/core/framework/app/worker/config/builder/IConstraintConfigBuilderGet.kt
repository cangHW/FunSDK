package com.proxy.service.core.framework.app.worker.config.builder

import com.proxy.service.core.framework.app.worker.enums.NetworkTypeEnum

/**
 * @author: cangHX
 * @data: 2024/12/13 18:49
 * @desc:
 */
interface IConstraintConfigBuilderGet {

    /**
     * 获取执行需要的网络状态
     * */
    fun getRequiredNetworkType(): NetworkTypeEnum

    /**
     * 获取是否必须在设备充电模式下执行
     */
    fun getRequiresCharging(): Boolean

    /**
     * 获取是否必须在设备空闲模式下执行
     */
    fun getRequiresDeviceIdle(): Boolean

    /**
     * 获取是否必须在设备不处于低电量模式时执行
     */
    fun getRequiresBatteryNotLow(): Boolean

    /**
     * 获取是否必须在设备不处于低存储空间模式时执行
     * */
    fun getRequiresStorageNotLow(): Boolean
}