package com.proxy.service.core.framework.app.message.process.constants

import com.proxy.service.core.constants.CoreConfig

/**
 * @author: cangHX
 * @data: 2025/9/15 23:12
 * @desc:
 */
object ShareDataConstants {

    const val TAG = "${CoreConfig.TAG}Msg_Process"

    /**
     * 进程消息响应的 provider 方法名称
     * */
    const val SHARE_DATA_PROVIDER_METHOD_NAME = "system_share_data_provider_method"

    /**
     * 进程消息响应的 broadcast action
     * */
    const val SHARE_DATA_BROADCAST_ACTION_NAME = "system_share_data_broadcast_action"

    /**
     * 进程消息数据传递的 key
     * */
    const val KEY_BUNDLE = "share_data_message"

}