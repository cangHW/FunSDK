package com.proxy.service.core.framework.system.sound.base

import com.proxy.service.core.framework.system.sound.enums.ContentTypeEnum
import com.proxy.service.core.framework.system.sound.enums.UsageTypeEnum

/**
 * @author: cangHX
 * @data: 2024/12/5 20:43
 * @desc:
 */
interface IBuilderGet {

    fun getSoundPoolName(): String

    fun getMaxStreams(): Int

    fun getUsageType(): UsageTypeEnum

    fun getContentType(): ContentTypeEnum

    fun getLeftVolume(): Float

    fun getRightVolume(): Float

    fun getRate(): Float

}