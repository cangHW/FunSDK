package com.proxy.service.widget.info.notification.notification.config

import android.app.PendingIntent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.proxy.service.widget.info.notification.notification.controller.BaseController

/**
 * @author: cangHX
 * @data: 2025/12/16 14:06
 * @desc:
 */
interface IBuilderGet {

    fun getChannelId(): String

    fun getGroupKey(): String?
    fun isGroupSummary(): Boolean

    fun getSmallIconController(): BaseController
    fun getLargeIconController(): BaseController

    fun getContentTitle(): CharSequence?
    fun getContentText(): CharSequence?
    fun getSubText(): CharSequence?
    fun getContentInfo(): CharSequence?
    fun getCustomContentView(): RemoteViews?
    fun getCustomBigContentView(): RemoteViews?

    fun getContentIntent(): PendingIntent?
    fun getDeleteIntent(): PendingIntent?
    fun getActions(): MutableList<NotificationCompat.Action>

    fun isAutoCancel(): Boolean
    fun isOngoing(): Boolean

    fun getCategory(): String?

    fun getWhen(): Long
    fun getExtras(): Bundle?
}