package com.proxy.service.widget.info.notification.channel.support

import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationChannelGroupCompat
import androidx.core.app.NotificationManagerCompat
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.widget.info.notification.channel.config.ChannelConfig

/**
 * @author: cangHX
 * @data: 2025/12/24 11:26
 * @desc:
 */
object ChannelCompat {

    fun doWork(config: ChannelConfig) {
        val manager = NotificationManagerCompat.from(CsContextManager.getApplication())

        config.getGroupId()?.let { id ->
            config.getGroupName()?.let { name ->
                val builder = NotificationChannelGroupCompat.Builder(id)
                builder.setName(name)
                builder.setDescription(config.getGroupDescription())
                manager.createNotificationChannelGroup(builder.build())
            }
        }

        val builder = NotificationChannelCompat.Builder(
            config.getChannelId(),
            config.getChannelPriority().getPriorityCompat()
        )
        builder.setName(config.getChannelName())

        builder.setDescription(config.getChannelDescription())

        config.getGroupId()?.let { id ->
            builder.setGroup(id)
        }

        config.isLightsEnable()?.let {
            builder.setLightsEnabled(it)
        }
        config.getLightColor()?.let {
            builder.setLightColor(it)
        }

        config.isVibrationEnable()?.let {
            builder.setVibrationEnabled(it)
        }
        config.getVibrationPattern()?.let {
            builder.setVibrationPattern(it)
        }

        config.getSoundUri()?.let { uri ->
            builder.setSound(uri, null)
        }

        config.getShowBadge()?.let {
            builder.setShowBadge(it)
        }

        manager.createNotificationChannel(builder.build())
    }

}