package com.proxy.service.widget.info.notification.notification.config

import android.app.PendingIntent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.proxy.service.widget.info.notification.constants.NotificationConstants
import com.proxy.service.widget.info.notification.enums.NotificationCategory
import com.proxy.service.widget.info.notification.notification.controller.BaseController
import com.proxy.service.widget.info.notification.notification.controller.LargeIconController
import com.proxy.service.widget.info.notification.notification.controller.SmallIconController
import com.proxy.service.widget.info.notification.notification.pending.CsPendingIntent

/**
 * @author: cangHX
 * @data: 2025/12/16 14:06
 * @desc:
 */
class NotificationConfig private constructor(private val builder: IBuilderGet) : IBuilderGet {

    companion object {

        /**
         * 创建通知构造器
         *
         * @param channelId     通知渠道 ID
         * */
        fun builder(channelId: String): IBuilder {
            return Builder(channelId)
        }
    }

    override fun getChannelId(): String {
        return builder.getChannelId()
    }

    override fun getGroupKey(): String? {
        return builder.getGroupKey()
    }

    override fun isGroupSummary(): Boolean {
        return builder.isGroupSummary()
    }

    override fun getSmallIconController(): BaseController {
        return builder.getSmallIconController()
    }

    override fun getLargeIconController(): BaseController {
        return builder.getLargeIconController()
    }

    override fun getContentTitle(): CharSequence? {
        return builder.getContentTitle()
    }

    override fun getContentText(): CharSequence? {
        return builder.getContentText()
    }

    override fun getSubText(): CharSequence? {
        return builder.getSubText()
    }

    override fun getContentInfo(): CharSequence? {
        return builder.getContentInfo()
    }

    override fun getCustomContentView(): RemoteViews? {
        return builder.getCustomContentView()
    }

    override fun getCustomBigContentView(): RemoteViews? {
        return builder.getCustomBigContentView()
    }

    override fun getContentIntent(): PendingIntent? {
        return builder.getContentIntent()
    }

    override fun getDeleteIntent(): PendingIntent? {
        return builder.getDeleteIntent()
    }

    override fun getActions(): MutableList<NotificationCompat.Action> {
        return builder.getActions()
    }

    override fun isAutoCancel(): Boolean {
        return builder.isAutoCancel()
    }

    override fun isOngoing(): Boolean {
        return builder.isOngoing()
    }

    override fun getCategory(): String? {
        return builder.getCategory()
    }

    override fun getWhen(): Long {
        return builder.getWhen()
    }

    override fun getExtras(): Bundle? {
        return builder.getExtras()
    }

    class Builder(private val channelId: String) : IBuilder, IBuilderGet {

        private var groupKey: String? = null
        private var isGroupSummary: Boolean = NotificationConstants.IS_GROUP_SUMMARY

        private var smallIconController = SmallIconController.create()
        private var largeIconController = LargeIconController.create()

        private var contentTitle: CharSequence? = null
        private var contentText: CharSequence? = null
        private var subText: CharSequence? = null
        private var contentInfo: CharSequence? = null
        private var smallContentView: RemoteViews? = null
        private var largeContentView: RemoteViews? = null

        private var contentIntent: CsPendingIntent? = null
        private var deleteIntent: CsPendingIntent? = null
        private val actionList = ArrayList<NotificationCompat.Action>()

        private var autoCancel = NotificationConstants.AUTO_CANCEL
        private var ongoing = NotificationConstants.ONGOING

        private var category: NotificationCategory? = null
        private var whenTime: Long? = null
        private var extrasBundle: Bundle? = null

        override fun setGroup(groupKey: String, isGroupSummary: Boolean): IBuilder {
            this.groupKey = groupKey
            this.isGroupSummary = isGroupSummary
            return this
        }

        override fun setSmallIcon(bitmap: Bitmap): IBuilder {
            smallIconController = SmallIconController.createByBitmap(bitmap)
            return this
        }

        override fun setSmallIcon(url: String): IBuilder {
            smallIconController = SmallIconController.createByUrl(url)
            return this
        }

        override fun setSmallIcon(resId: Int): IBuilder {
            smallIconController = SmallIconController.createByResId(resId)
            return this
        }

        override fun setLargeIcon(bitmap: Bitmap): IBuilder {
            largeIconController = LargeIconController.createByBitmap(bitmap)
            return this
        }

        override fun setLargeIcon(url: String): IBuilder {
            largeIconController = LargeIconController.createByUrl(url)
            return this
        }

        override fun setLargeIcon(resId: Int): IBuilder {
            largeIconController = LargeIconController.createByResId(resId)
            return this
        }

        override fun setContentTitle(text: CharSequence): IBuilder {
            this.contentTitle = text
            return this
        }

        override fun setContentText(text: CharSequence): IBuilder {
            this.contentText = text
            return this
        }

        override fun setSubText(text: CharSequence): IBuilder {
            this.subText = text
            return this
        }

        override fun setContentInfo(text: CharSequence): IBuilder {
            this.contentInfo = text
            return this
        }

        override fun setCustomContentView(contentView: RemoteViews): IBuilder {
            this.smallContentView = contentView
            return this
        }

        override fun setCustomBigContentView(contentView: RemoteViews): IBuilder {
            this.largeContentView = contentView
            return this
        }

        override fun setContentIntent(intent: CsPendingIntent): IBuilder {
            this.contentIntent = intent
            return this
        }

        override fun setDeleteIntent(intent: CsPendingIntent): IBuilder {
            this.deleteIntent = intent
            return this
        }

        override fun addAction(resId: Int, text: CharSequence?, intent: CsPendingIntent): IBuilder {
            val action = NotificationCompat.Action.Builder(resId, text, intent.getPendingIntent())
                .build()
            this.actionList.add(action)
            return this
        }

        override fun setAutoCancel(autoCancel: Boolean): IBuilder {
            this.autoCancel = autoCancel
            return this
        }

        override fun setOngoing(ongoing: Boolean): IBuilder {
            this.ongoing = ongoing
            return this
        }

        override fun setCategory(category: NotificationCategory): IBuilder {
            this.category = category
            return this
        }

        override fun setWhen(time: Long): IBuilder {
            this.whenTime = time
            return this
        }

        override fun setExtras(bundle: Bundle): IBuilder {
            this.extrasBundle = bundle
            return this
        }

        override fun build(): NotificationConfig {
            return NotificationConfig(this)
        }

        override fun getChannelId(): String {
            return channelId
        }

        override fun getGroupKey(): String? {
            return groupKey
        }

        override fun isGroupSummary(): Boolean {
            return isGroupSummary
        }

        override fun getSmallIconController(): BaseController {
            return smallIconController
        }

        override fun getLargeIconController(): BaseController {
            return largeIconController
        }

        override fun getContentTitle(): CharSequence? {
            return contentTitle
        }

        override fun getContentText(): CharSequence? {
            return contentText
        }

        override fun getSubText(): CharSequence? {
            return subText
        }

        override fun getContentInfo(): CharSequence? {
            return contentInfo
        }

        override fun getCustomContentView(): RemoteViews? {
            return smallContentView
        }

        override fun getCustomBigContentView(): RemoteViews? {
            return largeContentView
        }

        override fun getContentIntent(): PendingIntent? {
            return contentIntent?.getPendingIntent()
        }

        override fun getDeleteIntent(): PendingIntent? {
            return deleteIntent?.getPendingIntent()
        }

        override fun getActions(): MutableList<NotificationCompat.Action> {
            return actionList
        }

        override fun isAutoCancel(): Boolean {
            return autoCancel
        }

        override fun isOngoing(): Boolean {
            return ongoing
        }

        override fun getCategory(): String? {
            return category?.category
        }

        override fun getWhen(): Long {
            return whenTime ?: System.currentTimeMillis()
        }

        override fun getExtras(): Bundle? {
            return extrasBundle
        }
    }

}