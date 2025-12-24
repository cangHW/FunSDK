package com.proxy.service.widget.info.notification.channel.config

import android.net.Uri
import com.proxy.service.widget.info.notification.enums.NotificationPriority
import com.proxy.service.widget.info.notification.enums.NotificationVisibility

/**
 * @author: cangHX
 * @data: 2025/12/17 20:41
 * @desc:
 */
interface IBuilderGet {

    fun getChannelId(): String

    fun getChannelName(): String

    fun getChannelPriority(): NotificationPriority

    fun getChannelDescription(): String

    fun getGroupId(): String?

    fun getGroupName(): String?

    fun getGroupDescription(): String

    fun isLightsEnable(): Boolean?

    fun getLightColor(): Int?

    fun isVibrationEnable(): Boolean?

    fun getVibrationPattern(): LongArray?

    fun getSoundUri(): Uri?

    fun getLockscreenVisibility(): NotificationVisibility

    fun getBypassDnd(): Boolean?

    fun getShowBadge(): Boolean?
}