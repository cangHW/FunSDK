package com.proxy.service.widget.info.notification.enums

import androidx.core.app.NotificationCompat

/**
 * @author: cangHX
 * @data: 2025/12/16 14:47
 * @desc: 消息可见性
 */
sealed class NotificationVisibility private constructor(val visibility: Int) {

    /**
     * 在锁屏上显示完整内容。适用于非敏感通知，例如音乐播放、下载进度等。
     * */
    object PUBLIC : NotificationVisibility(NotificationCompat.VISIBILITY_PUBLIC)

    /**
     * 在锁屏上隐藏敏感内容，仅显示通知的基本信息（如标题）。
     * */
    object PRIVATE : NotificationVisibility(NotificationCompat.VISIBILITY_PRIVATE)

    /**
     * 在锁屏上完全隐藏通知。适用于非常敏感的通知，例如金融交易、隐私信息等。
     * */
    object SECRET : NotificationVisibility(NotificationCompat.VISIBILITY_SECRET)

    /**
     * 自定义可见性
     * */
    open class Custom(visibility: Int) : NotificationVisibility(visibility)

}