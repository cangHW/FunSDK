package com.proxy.service.widget.info.notification.channel.config

import android.net.Uri
import androidx.annotation.ColorInt
import com.proxy.service.widget.info.notification.constants.NotificationConstants
import com.proxy.service.widget.info.notification.enums.NotificationPriority
import com.proxy.service.widget.info.notification.enums.NotificationVisibility

/**
 * @author: cangHX
 * @data: 2025/12/17 20:41
 * @desc:
 */
interface IBuilder {

    /**
     * 设置通知渠道的优先级, 决定通知的显示顺序和是否显示横幅, 默认: [NotificationConstants.PRIORITY_DEFAULT]
     * */
    fun setChannelPriority(priority: NotificationPriority): IBuilder

    /**
     * 设置通知渠道的描述信息
     * */
    fun setChannelDescription(description: String): IBuilder

    /**
     * 设置渠道组信息
     *
     * @param groupId       渠道组 ID
     * @param groupName     渠道组名称
     * @param description   渠道组介绍
     * */
    fun setGroup(groupId: String, groupName: String, description: String = ""): IBuilder

    /**
     * 是否启用 LED 灯光效果
     * */
    fun enableLights(lights: Boolean): IBuilder

    /**
     * LED 灯光颜色
     *
     * @param argb  颜色, 例如：Color.RED 或 0xFF00FF00
     * */
    fun setLightColor(@ColorInt argb: Int): IBuilder

    /**
     * 是否启用振动效果
     * */
    fun enableVibration(vibration: Boolean): IBuilder

    /**
     * 设置振动模式
     *
     * @param vibrationPattern  振动模式, 例如: longArrayOf(0, 100, 200, 300)
     * */
    fun setVibrationPattern(vibrationPattern: LongArray): IBuilder

    /**
     * 设置声音, 自定义通知到达时的声音
     *
     * @param sound 设置 null 则取消通知声音
     * */
    fun setSound(sound: Uri?): IBuilder

    /**
     * 设置通知在锁屏上的可见性, 控制通知内容在锁屏上的显示方式, 默认: [NotificationConstants.VISIBILITY_DEFAULT]
     * */
    fun setLockscreenVisibility(visibility: NotificationVisibility): IBuilder

    /**
     * 设置通知是否绕过“请勿打扰”模式, 通知是否可以在“请勿打扰”模式下显示
     *
     * 需要权限
     * <uses-permission android:name="android.permission.ACCESS_NOTIFICATION_POLICY" />
     * */
    fun setBypassDnd(bypassDnd: Boolean): IBuilder

    /**
     * 是否显示通知角标, 通知是否显示在应用图标的角标上
     * */
    fun setShowBadge(showBadge: Boolean): IBuilder

    /**
     * 构造对象
     * */
    fun build(): ChannelConfig
}