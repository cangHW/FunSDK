package com.proxy.service.widget.info.notification.constants

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.widget.info.notification.enums.NotificationPriority
import com.proxy.service.widget.info.notification.enums.NotificationVisibility

/**
 * @author: cangHX
 * @data: 2025/12/16 15:03
 * @desc:
 */
object NotificationConstants {

    const val TAG = "${CoreConfig.TAG}Widget_Notify"

    const val CHANNEL_ID_DEFAULT = "default_channel_id"
    const val CHANNEL_NAME_DEFAULT = "默认渠道"

    const val CHANNEL_GROUP_ID_DEFAULT = "default_channel_group_id"
    const val CHANNEL_GROUP_NAME_DEFAULT = "默认组"

    const val IS_GROUP_SUMMARY = false

    const val AUTO_CANCEL = true

    const val ONGOING = false

    val PRIORITY_DEFAULT = NotificationPriority.DEFAULT

    val VISIBILITY_DEFAULT = NotificationVisibility.PRIVATE

}