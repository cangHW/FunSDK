package com.proxy.service.widget.info.notification.notification.support

import androidx.core.app.NotificationCompat
import com.proxy.service.widget.info.notification.channel.config.ChannelConfig

/**
 * @author: cangHX
 * @data: 2025/12/24 21:23
 * @desc:
 */
object NotifyCompat {

    fun doWork(builder: NotificationCompat.Builder, channel: ChannelConfig) {
        builder.setPriority(channel.getChannelPriority().getPriorityCompat())
        builder.setSound(channel.getSoundUri())
        builder.setVibrate(channel.getVibrationPattern())
        channel.getLightColor()?.let {
            builder.setLights(it, 200, 200)
        }
        builder.setVisibility(channel.getLockscreenVisibility().visibility)
    }

}