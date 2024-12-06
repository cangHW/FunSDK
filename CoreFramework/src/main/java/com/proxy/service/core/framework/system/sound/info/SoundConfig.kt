package com.proxy.service.core.framework.system.sound.config

import com.proxy.service.core.framework.system.sound.base.IBuilder
import com.proxy.service.core.framework.system.sound.base.IBuilderGet
import com.proxy.service.core.framework.system.sound.enums.ContentTypeEnum
import com.proxy.service.core.framework.system.sound.enums.UsageTypeEnum

/**
 * @author: cangHX
 * @data: 2024/12/5 20:46
 * @desc:
 */
class SoundConfig private constructor(private val builder: IBuilderGet) : IBuilderGet {

    override fun getTag(): String {
        return builder.getTag()
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

        private var tag: String = "default_sound"
        private var usageType: UsageTypeEnum = UsageTypeEnum.USAGE_UNKNOWN
        private var contentType: ContentTypeEnum = ContentTypeEnum.CONTENT_TYPE_UNKNOWN

        private var leftVolume: Float = 1.0f
        private var rightVolume: Float = 1.0f

        private var rate: Float = 1.0f

        override fun setTag(tag: String): IBuilder {
            this.tag = tag
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

        override fun getTag(): String {
            return tag
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