package com.proxy.service.widget.info.notification.notification

import android.app.Notification
import androidx.core.app.NotificationCompat
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.io.sp.CsSpManager
import com.proxy.service.core.framework.io.sp.SpMode
import com.proxy.service.widget.info.notification.channel.config.ChannelConfig
import com.proxy.service.widget.info.notification.notification.config.NotificationConfig
import com.proxy.service.widget.info.notification.notification.support.NotifyCompat

/**
 * @author: cangHX
 * @data: 2025/12/19 18:46
 * @desc:
 */
class NotifyManager {

    companion object {

        private const val SP_GROUP_NOTIFICATION = "GroupNotification"
        private const val KEY_GROUP_MAX_ID = "group_max_id"
        private val lock = Any()

        private val _instance by lazy { NotifyManager() }

        fun getInstance(): NotifyManager {
            return _instance
        }
    }

    fun createGroupNotification(config: NotificationConfig, callback: (Notification) -> Unit) {
        val context = CsContextManager.getApplication()
        val builder = NotificationCompat.Builder(context, config.getChannelId())
        builder.setContentTitle(config.getContentTitle())
        builder.setContentText(config.getContentText())
        builder.setSubText(config.getSubText())
        builder.setContentInfo(config.getContentInfo())

        builder.setContentIntent(config.getContentIntent())
        builder.setDeleteIntent(config.getDeleteIntent())

        builder.setAutoCancel(config.isAutoCancel())

        builder.setWhen(config.getWhen())
        builder.setExtras(config.getExtras())

        config.getGroupKey()?.let {
            builder.setGroup(it)
            builder.setGroupSummary(true)
        }

        config.getSmallIconController().doRun(builder) {
            callback(builder.build())
        }
    }

    fun createNotification(
        config: NotificationConfig,
        channel: ChannelConfig,
        callback: (Notification) -> Unit
    ) {
        val context = CsContextManager.getApplication()
        val builder = NotificationCompat.Builder(context, config.getChannelId())
        builder.setContentTitle(config.getContentTitle())
        builder.setContentText(config.getContentText())
        builder.setSubText(config.getSubText())
        builder.setContentInfo(config.getContentInfo())
        config.getCustomContentView()?.let {
            builder.setCustomContentView(it)
        }
        config.getCustomBigContentView()?.let {
            builder.setCustomBigContentView(it)
        }

        builder.setContentIntent(config.getContentIntent())
        builder.setDeleteIntent(config.getDeleteIntent())
        config.getActions().forEach {
            builder.addAction(it)
        }

        builder.setAutoCancel(config.isAutoCancel())
        builder.setOngoing(config.isOngoing())

        config.getCategory()?.let {
            builder.setCategory(it)
        }

        builder.setWhen(config.getWhen())
        builder.setExtras(config.getExtras())

        config.getGroupKey()?.let {
            builder.setGroup(it)
            builder.setGroupSummary(config.isGroupSummary())
        }

        NotifyCompat.doWork(builder, channel)

        config.getSmallIconController().doRun(builder) {
            config.getLargeIconController().doRun(builder) {
                callback(builder.build())
            }
        }
    }


    fun getGroupNotificationId(groupKey: String): Int {
        synchronized(lock) {
            val sp = CsSpManager.name(SP_GROUP_NOTIFICATION)
                .mode(SpMode.MULTI_PROCESS_MODE)
                .getController()
            var groupId = sp.getInt(groupKey, 0)
            if (groupId <= 0) {
                val currentMax = sp.getInt(KEY_GROUP_MAX_ID, 0)
                groupId = currentMax + 1
                sp.put(KEY_GROUP_MAX_ID, groupId)
                sp.put(groupKey, groupId)
            }
            return groupId
        }
    }
}