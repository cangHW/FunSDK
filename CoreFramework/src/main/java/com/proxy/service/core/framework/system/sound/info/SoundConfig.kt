package com.proxy.service.core.framework.system.sound.info

import com.proxy.service.core.framework.system.sound.base.IBuilder
import com.proxy.service.core.framework.system.sound.base.IBuilderGet
import com.proxy.service.core.framework.system.sound.config.Config
import com.proxy.service.core.framework.system.sound.enums.ContentTypeEnum
import com.proxy.service.core.framework.system.sound.enums.UsageTypeEnum

/**
 * @author: cangHX
 * @data: 2024/12/5 20:46
 * @desc:
 */
class SoundConfig private constructor(private val builder: IBuilderGet) : IBuilderGet {

    override fun getSoundPoolTag(): String {
        return builder.getSoundPoolTag()
    }

    override fun getMaxStreams(): Int {
        return builder.getMaxStreams()
    }

    override fun getUsageType(): UsageTypeEnum {
        return builder.getUsageType()
    }

    override fun getContentType(): ContentTypeEnum {
        return builder.getContentType()
    }

    override fun getLeftVolume(): Float {
        return builder.getLeftVolume()
    }

    override fun getRightVolume(): Float {
        return builder.getRightVolume()
    }

    override fun getRate(): Float {
        return builder.getRate()
    }

    companion object {
        fun builder(): IBuilder {
            return Builder()
        }
    }

    private class Builder : IBuilder, IBuilderGet {

        private var tag: String = Config.DEFAULT_POOL_NAME

        private var maxStreams: Int = Config.DEFAULT_MAX_STREAMS

        private var usageType: UsageTypeEnum = UsageTypeEnum.USAGE_UNKNOWN
        private var contentType: ContentTypeEnum = ContentTypeEnum.CONTENT_TYPE_UNKNOWN

        private var leftVolume: Float = Config.DEFAULT_LEFT_VOLUME
        private var rightVolume: Float = Config.DEFAULT_RIGHT_VOLUME

        private var rate: Float = Config.DEFAULT_RATE

        override fun setSoundPoolTag(tag: String): IBuilder {
            this.tag = tag
            return this
        }

        override fun setMaxStreams(maxStreams: Int): IBuilder {
            this.maxStreams = maxStreams
            return this
        }

        override fun setUsageEnum(usageType: UsageTypeEnum): IBuilder {
            this.usageType = usageType
            return this
        }

        override fun setContentTypeEnum(contentType: ContentTypeEnum): IBuilder {
            this.contentType = contentType
            return this
        }

        override fun setVolume(leftVolume: Float, rightVolume: Float): IBuilder {
            this.leftVolume = leftVolume
            this.rightVolume = rightVolume
            return this
        }

        override fun setVolume(volume: Float): IBuilder {
            this.leftVolume = volume
            this.rightVolume = volume
            return this
        }

        override fun setRate(rate: Float): IBuilder {
            this.rate = rate
            return this
        }

        override fun build(): SoundConfig {
            return SoundConfig(this)
        }

        override fun getSoundPoolTag(): String {
            return tag
        }

        override fun getMaxStreams(): Int {
            return maxStreams
        }

        override fun getUsageType(): UsageTypeEnum {
            return usageType
        }

        override fun getContentType(): ContentTypeEnum {
            return contentType
        }

        override fun getLeftVolume(): Float {
            return leftVolume
        }

        override fun getRightVolume(): Float {
            return rightVolume
        }

        override fun getRate(): Float {
            return rate
        }
    }

}