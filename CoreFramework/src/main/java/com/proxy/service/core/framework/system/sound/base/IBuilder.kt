package com.proxy.service.core.framework.system.sound.base

import androidx.annotation.FloatRange
import com.proxy.service.core.framework.system.sound.info.SoundConfig
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
     *
     * 默认值见 [com.proxy.service.core.framework.system.sound.config.Config]
     * */
    fun setSoundPoolTag(tag: String): IBuilder

    /**
     * 设置最大音频数量, 建议 64 以下, 需要考虑内存占用等情况
     *
     * 默认值见 [com.proxy.service.core.framework.system.sound.config.Config]
     * */
    fun setMaxStreams(maxStreams: Int): IBuilder

    /**
     * 设置音频将用于什么场景
     * */
    fun setUsageEnum(usageType: UsageTypeEnum): IBuilder

    /**
     * 设置音频类型
     * */
    fun setContentTypeEnum(contentType: ContentTypeEnum): IBuilder

    /**
     * 设置音量
     *
     * 默认值见 [com.proxy.service.core.framework.system.sound.config.Config]
     * */
    fun setVolume(
        @FloatRange(from = 0.0, to = 1.0) leftVolume: Float,
        @FloatRange(from = 0.0, to = 1.0) rightVolume: Float
    ): IBuilder

    /**
     * 设置音量
     *
     * 默认值见 [com.proxy.service.core.framework.system.sound.config.Config]
     * */
    fun setVolume(@FloatRange(from = 0.0, to = 1.0) volume: Float): IBuilder

    /**
     * 设置播放速率
     *
     * 默认值见 [com.proxy.service.core.framework.system.sound.config.Config]
     * */
    fun setRate(@FloatRange(from = 0.5, to = 2.0) rate: Float): IBuilder

    /**
     * 创建配置
     * */
    fun build(): SoundConfig
}