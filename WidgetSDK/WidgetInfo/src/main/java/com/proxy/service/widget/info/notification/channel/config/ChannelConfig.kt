package com.proxy.service.widget.info.notification.channel.config

import android.net.Uri
import com.proxy.service.widget.info.notification.constants.NotificationConstants
import com.proxy.service.widget.info.notification.enums.NotificationPriority
import com.proxy.service.widget.info.notification.enums.NotificationVisibility

/**
 * @author: cangHX
 * @data: 2025/12/16 14:04
 * @desc:
 */
class ChannelConfig private constructor(private val builder: IBuilderGet) : IBuilderGet {

    override fun getChannelId(): String {
        return builder.getChannelId()
    }

    override fun getChannelName(): String {
        return builder.getChannelName()
    }

    override fun getChannelPriority(): NotificationPriority {
        return builder.getChannelPriority()
    }

    override fun getChannelDescription(): String {
        return builder.getChannelDescription()
    }

    override fun getGroupId(): String? {
        return builder.getGroupId()
    }

    override fun getGroupName(): String? {
        return builder.getGroupName()
    }

    override fun getGroupDescription(): String {
        return builder.getGroupDescription()
    }

    override fun isLightsEnable(): Boolean? {
        return builder.isLightsEnable()
    }

    override fun getLightColor(): Int? {
        return builder.getLightColor()
    }

    override fun isVibrationEnable(): Boolean? {
        return builder.isVibrationEnable()
    }

    override fun getVibrationPattern(): LongArray? {
        return builder.getVibrationPattern()
    }

    override fun getSoundUri(): Uri? {
        return builder.getSoundUri()
    }

    override fun getLockscreenVisibility(): NotificationVisibility {
        return builder.getLockscreenVisibility()
    }

    override fun getBypassDnd(): Boolean? {
        return builder.getBypassDnd()
    }

    override fun getShowBadge(): Boolean? {
        return builder.getShowBadge()
    }

    companion object {

        /**
         * 创建通知渠道构造器
         *
         * @param channelId     渠道 ID
         * @param channelName   渠道名称
         * */
        fun builder(channelId: String, channelName: String): IBuilder {
            val id = channelId.ifBlank {
                NotificationConstants.CHANNEL_ID_DEFAULT
            }
            val name = channelName.ifBlank {
                NotificationConstants.CHANNEL_NAME_DEFAULT
            }
            return Builder(id, name)
        }
    }

    class Builder(
        private val channelId: String,
        private val channelName: String
    ) : IBuilder, IBuilderGet {

        private var groupId: String? = null
        private var groupName: String? = null
        private var groupDescription: String = ""

        private var priority: NotificationPriority = NotificationConstants.PRIORITY_DEFAULT
        private var description: String = ""

        private var enableLights: Boolean? = null
        private var lightColor: Int? = null

        private var enableVibration: Boolean? = null
        private var vibrationPattern: LongArray? = null

        private var sound: Uri? = null

        private var visibility: NotificationVisibility = NotificationConstants.VISIBILITY_DEFAULT

        private var bypassDnd: Boolean? = null
        private var showBadge: Boolean? = null

        override fun setChannelPriority(priority: NotificationPriority): IBuilder {
            this.priority = priority
            return this
        }

        override fun setChannelDescription(description: String): IBuilder {
            this.description = description
            return this
        }

        override fun setGroup(groupId: String, groupName: String, description: String): IBuilder {
            this.groupId = groupId.ifBlank {
                NotificationConstants.CHANNEL_GROUP_ID_DEFAULT
            }
            this.groupName = groupName.ifBlank {
                NotificationConstants.CHANNEL_GROUP_NAME_DEFAULT
            }
            this.groupDescription = description
            return this
        }

        override fun enableLights(lights: Boolean): IBuilder {
            this.enableLights = lights
            return this
        }

        override fun setLightColor(argb: Int): IBuilder {
            this.lightColor = argb
            return this
        }

        override fun enableVibration(vibration: Boolean): IBuilder {
            this.enableVibration = vibration
            return this
        }

        override fun setVibrationPattern(vibrationPattern: LongArray): IBuilder {
            this.vibrationPattern = vibrationPattern
            return this
        }

        override fun setSound(sound: Uri?): IBuilder {
            this.sound = sound
            return this
        }

        override fun setLockscreenVisibility(visibility: NotificationVisibility): IBuilder {
            this.visibility = visibility
            return this
        }

        override fun setBypassDnd(bypassDnd: Boolean): IBuilder {
            this.bypassDnd = bypassDnd
            return this
        }

        override fun setShowBadge(showBadge: Boolean): IBuilder {
            this.showBadge = showBadge
            return this
        }

        override fun build(): ChannelConfig {
            return ChannelConfig(this)
        }

        override fun getChannelId(): String {
            return channelId
        }

        override fun getChannelName(): String {
            return channelName
        }

        override fun getChannelPriority(): NotificationPriority {
            return priority
        }

        override fun getChannelDescription(): String {
            return description
        }

        override fun getGroupId(): String? {
            return groupId
        }

        override fun getGroupName(): String? {
            return groupName
        }

        override fun getGroupDescription(): String {
            return groupDescription
        }

        override fun isLightsEnable(): Boolean? {
            return enableLights
        }

        override fun getLightColor(): Int? {
            return lightColor
        }

        override fun isVibrationEnable(): Boolean? {
            return enableVibration
        }

        override fun getVibrationPattern(): LongArray? {
            return vibrationPattern
        }

        override fun getSoundUri(): Uri? {
            return sound
        }

        override fun getLockscreenVisibility(): NotificationVisibility {
            return visibility
        }

        override fun getBypassDnd(): Boolean? {
            return bypassDnd
        }

        override fun getShowBadge(): Boolean? {
            return showBadge
        }

    }
}