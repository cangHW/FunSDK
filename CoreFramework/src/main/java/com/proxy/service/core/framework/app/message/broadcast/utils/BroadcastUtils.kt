package com.proxy.service.core.framework.app.message.broadcast.utils

/**
 * @author: cangHX
 * @data: 2026/4/7 14:01
 * @desc:
 */
object BroadcastUtils {

    private const val ACTION_APP_PREFIX = "app_"

    fun checkAppAction(action: String): String {
        return if (action.startsWith(ACTION_APP_PREFIX)) {
            action
        } else {
            "$ACTION_APP_PREFIX$action"
        }
    }

    fun parseAppAction(action: String): String {
        if (action.startsWith(ACTION_APP_PREFIX)) {
            return action.replaceFirst(ACTION_APP_PREFIX, "")
        }
        return action
    }

}