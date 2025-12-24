package com.proxy.service.widget.info.notification.channel.support

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.widget.info.notification.channel.config.ChannelConfig

/**
 * @author: cangHX
 * @data: 2025/12/18 20:55
 * @desc:
 */
object ChannelV26 {

    @RequiresApi(Build.VERSION_CODES.O)
    fun doWork(config: ChannelConfig) {
        val manager = CsContextManager.getApplication()
            .getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager?

        config.getGroupId()?.let { id ->
            config.getGroupName()?.let { name ->
                val group = NotificationChannelGroup(id, name)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    group.description = config.getGroupDescription()
                }
                manager?.createNotificationChannelGroup(group)
            }
        }

        val channel = NotificationChannel(
            config.getChannelId(),
            config.getChannelName(),
            config.getChannelPriority().getPriority()
        )
        channel.description = config.getChannelDescription()

        config.getGroupId()?.let { id ->
            channel.group = id
        }

        config.isLightsEnable()?.let {
            channel.enableLights(it)
        }
        config.getLightColor()?.let {
            channel.lightColor = it
        }

        config.isVibrationEnable()?.let {
            channel.enableVibration(it)
        }
        config.getVibrationPattern()?.let {
            channel.vibrationPattern = it
        }

        config.getSoundUri()?.let { uri ->
            channel.setSound(uri, null)
        }

        channel.lockscreenVisibility = config.getLockscreenVisibility().visibility

        config.getBypassDnd()?.let {
            channel.setBypassDnd(it)
        }

        config.getShowBadge()?.let {
            channel.setShowBadge(it)
        }

        manager?.createNotificationChannel(channel)
    }

}