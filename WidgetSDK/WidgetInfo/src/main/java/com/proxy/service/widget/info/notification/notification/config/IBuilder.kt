package com.proxy.service.widget.info.notification.notification.config

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import com.proxy.service.widget.info.notification.constants.NotificationConstants
import com.proxy.service.widget.info.notification.enums.NotificationCategory
import com.proxy.service.widget.info.notification.notification.pending.CsPendingIntent

/**
 * @author: cangHX
 * @data: 2025/12/16 14:06
 * @desc:
 */
interface IBuilder {

    /**
     * 设置组信息
     *
     * @param groupKey          组唯一标识, 需要优先级相同或者使用同一个组下的渠道
     * @param isGroupSummary    当前通知是否是组摘要, 只有一个通知可以设置为组摘要通知, 用于手动控制分组以及更新组摘要
     * */
    fun setGroup(
        groupKey: String,
        isGroupSummary: Boolean = NotificationConstants.IS_GROUP_SUMMARY
    ): IBuilder

    /*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

    /**
     * 设置通知的小图标, 根据 Android 的设计规范, 建议为 24x24 dp, 默认为当前应用 icon
     * */
    fun setSmallIcon(bitmap: Bitmap): IBuilder

    /**
     * 设置通知的小图标, 根据 Android 的设计规范, 建议为 24x24 dp, 默认为当前应用 icon
     * */
    fun setSmallIcon(url: String): IBuilder

    /**
     * 设置通知的小图标, 根据 Android 的设计规范, 建议为 24x24 dp, 默认为当前应用 icon
     * */
    fun setSmallIcon(@DrawableRes resId: Int): IBuilder

    /**
     * 设置通知的大图标, 根据 Android 的设计规范, 建议为 48x48 dp 或更大, 默认为空
     * */
    fun setLargeIcon(bitmap: Bitmap): IBuilder

    /**
     * 设置通知的大图标, 根据 Android 的设计规范, 建议为 48x48 dp 或更大, 默认为空
     * */
    fun setLargeIcon(url: String): IBuilder

    /**
     * 设置通知的大图标, 根据 Android 的设计规范, 建议为 48x48 dp 或更大, 默认为空
     * */
    fun setLargeIcon(@DrawableRes resId: Int): IBuilder

    /*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

    /**
     * 设置通知的标题, 显示在通知的主要内容区域，通常是通知的标题。
     * */
    fun setContentTitle(text: CharSequence): IBuilder

    /**
     * 设置通知的内容, 显示在通知的主要内容区域，通常是通知的简要描述。
     * */
    fun setContentText(text: CharSequence): IBuilder

    /**
     * 设置通知的子标题, 显示在通知的次要内容区域，通常是附加信息。
     * */
    fun setSubText(text: CharSequence): IBuilder

    /**
     * 设置通知的附加信息, 显示在通知的右侧，通常用于显示数量或其他附加信息。
     * */
    fun setContentInfo(text: CharSequence): IBuilder

    /**
     * 设置自定义 view, 用于展示自定义通知默认视图
     * */
    fun setCustomContentView(contentView: RemoteViews): IBuilder

    /**
     * 设置自定义 view, 用于展示自定义通知大视图
     * */
    fun setCustomBigContentView(contentView: RemoteViews): IBuilder

    /*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

    /**
     * 设置通知的点击事件, 当用户点击通知时触发的操作
     * */
    fun setContentIntent(intent: CsPendingIntent): IBuilder

    /**
     * 设置通知的删除事件, 当用户清除通知时触发的操作
     * */
    fun setDeleteIntent(intent: CsPendingIntent): IBuilder

    /**
     * 为通知添加按钮操作, 在通知中添加按钮，允许用户直接执行某些操作（如回复、忽略等）
     * */
    fun addAction(@DrawableRes resId: Int, text: CharSequence?, intent: CsPendingIntent): IBuilder

    /*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

    /**
     * 设置通知是否自动取消, 当用户点击通知时，是否自动清除通知, 默认: [NotificationConstants.AUTO_CANCEL]
     * */
    fun setAutoCancel(autoCancel: Boolean): IBuilder

    /**
     * 设置通知是否为持续通知, 持续通知不能被用户手动清除，适用于后台任务等场景, 默认: [NotificationConstants.ONGOING]
     * */
    fun setOngoing(ongoing: Boolean): IBuilder

    /*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

    /**
     * 设置通知的类别, 帮助系统对通知进行分类（如消息、警告等）, 默认为空。
     * */
    fun setCategory(category: NotificationCategory): IBuilder

    /**
     * 设置通知时间
     *
     * @param time  13 位时间戳
     * */
    fun setWhen(time: Long): IBuilder

    /**
     * 设置附加信息
     * */
    fun setExtras(bundle: Bundle): IBuilder

    /*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

    /**
     * 构造对象
     * */
    fun build(): NotificationConfig
}