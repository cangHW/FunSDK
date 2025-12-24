package com.proxy.service.widget.info.notification

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.IntRange
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.widget.info.notification.channel.ChannelManager
import com.proxy.service.widget.info.notification.channel.config.ChannelConfig
import com.proxy.service.widget.info.notification.constants.NotificationConstants
import com.proxy.service.widget.info.notification.notification.NotifyManager
import com.proxy.service.widget.info.notification.notification.config.NotificationConfig

/**
 * @author: cangHX
 * @data: 2025/12/16 10:37
 * @desc:
 */
object CsNotificationManager {

    private const val MIN_NOTIFICATION_ID = 10000L

    /**
     * 注册通知渠道
     * */
    fun register(channel: ChannelConfig) {
        ChannelManager.getInstance().put(channel)
    }

    /**
     * 展示通知
     *
     * 需要权限
     * <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
     *
     * @param notificationId    需要大于等于 [MIN_NOTIFICATION_ID], 前面留给组使用
     * */
    fun sendNotification(
        @IntRange(MIN_NOTIFICATION_ID) notificationId: Int,
        config: NotificationConfig
    ) {
        if (!CsPermission.isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) {
            CsLogger.tag(NotificationConstants.TAG).e("No notification permission.")
        }

        val channel = ChannelManager.getInstance().get(config.getChannelId())

        if (channel == null) {
            CsLogger.tag(NotificationConstants.TAG)
                .e("No corresponding notification channel information was found. channelId = ${config.getChannelId()}")
            return
        }

        val manager = CsContextManager.getApplication()
            .getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager?

        if (manager == null) {
            CsLogger.tag(NotificationConstants.TAG).e("NotificationManager failed to obtain.")
            return
        }

        NotifyManager.getInstance().createNotification(config, channel) {
            manager.notify(notificationId, it)
        }
    }

    /**
     * 展示通知并自动分组
     *
     * 需要权限
     * <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
     *
     * @param notificationId    需要大于等于 [MIN_NOTIFICATION_ID], 前面留给组使用
     * */
    fun sendGroupedNotification(
        @IntRange(MIN_NOTIFICATION_ID) notificationId: Int,
        config: NotificationConfig
    ) {
        if (!CsPermission.isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) {
            CsLogger.tag(NotificationConstants.TAG).e("No notification permission.")
        }

        val channel = ChannelManager.getInstance().get(config.getChannelId())

        if (channel == null) {
            CsLogger.tag(NotificationConstants.TAG)
                .e("No corresponding notification channel information was found. channelId = ${config.getChannelId()}")
            return
        }

        val manager = CsContextManager.getApplication()
            .getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager?

        if (manager == null) {
            CsLogger.tag(NotificationConstants.TAG).e("NotificationManager failed to obtain.")
            return
        }

        NotifyManager.getInstance().apply {
            createNotification(config, channel) {
                manager.notify(notificationId, it)
            }
            val groupKey = config.getGroupKey()
            if (groupKey.isNullOrEmpty() || groupKey.isBlank()) {
                CsLogger.tag(NotificationConstants.TAG)
                    .e("The groupKey is empty and no grouping can be generated. groupKey = $groupKey")
                return
            }
            createGroupNotification(config) {
                manager.notify(getGroupNotificationId(groupKey), it)
            }
        }
    }

}