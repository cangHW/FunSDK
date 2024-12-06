package com.proxy.service.core.framework.system.sound.base

import com.proxy.service.core.framework.system.sound.config.SoundConfig
import com.proxy.service.core.framework.system.sound.enums.ContentTypeEnum
import com.proxy.service.core.framework.system.sound.enums.UsageTypeEnum

/**
 * @author: cangHX
 * @data: 2024/12/5 20:43
 * @desc:
 */
interface IBuilder {

    /**
     * 设置标签
     * */
    fun setTag(tag: String): IBuilder

    /**
     * 设置音频将用于什么场景
     * */
    fun setUsageEnum(usageEnum: UsageTypeEnum): IBuilder

    /**
     * 设置音频类型
     * */
    fun setContentTypeEnum(contentTypeEnum: ContentTypeEnum): IBuilder

    /**
     * 创建配置
     * */
    fun build(): SoundConfig
}