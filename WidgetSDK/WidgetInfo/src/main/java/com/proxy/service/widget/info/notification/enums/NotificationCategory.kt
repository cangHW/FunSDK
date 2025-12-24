package com.proxy.service.widget.info.notification.enums

import androidx.core.app.NotificationCompat

/**
 * @author: cangHX
 * @data: 2025/12/16 14:45
 * @desc: 通知类别, 效果例如：在“请勿打扰”模式下，某些类别的通知（如 CATEGORY_ALARM 或 CATEGORY_CALL）可能会被系统允许显示，而其他类别的通知可能会被屏蔽
 */
sealed class NotificationCategory private constructor(val category: String) {

    /**
     * 表示即时消息或聊天通知。系统可能会优先显示此类通知。
     * */
    object MESSAGE : NotificationCategory(NotificationCompat.CATEGORY_MESSAGE)

    /**
     * 表示警报通知，例如闹钟或定时器。
     * */
    object ALARM : NotificationCategory(NotificationCompat.CATEGORY_ALARM)

    /**
     * 表示来电或视频通话通知。
     * */
    object CALL : NotificationCategory(NotificationCompat.CATEGORY_CALL)

    /**
     * 自定义类别
     * */
    open class Custom(category: String) : NotificationCategory(category)
}