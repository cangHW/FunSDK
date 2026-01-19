package com.proxy.service.core.framework.app.message.broadcast

import com.proxy.service.core.framework.app.message.broadcast.callback.BroadcastMsgListener
import com.proxy.service.core.framework.app.message.broadcast.callback.OrderedBroadcastMsgListener
import com.proxy.service.core.framework.app.message.broadcast.controller.CallbackController
import com.proxy.service.core.framework.app.message.broadcast.factory.DynamicBroadcast
import com.proxy.service.core.framework.app.message.broadcast.factory.StaticBroadcast
import com.proxy.service.core.framework.app.message.broadcast.factory.base.IDynamicBroadcast
import com.proxy.service.core.framework.app.message.broadcast.factory.base.IStaticBroadcast

/**
 * 广播相关操作工具，只能接收到当前类发送的广播。
 *
 * @author: cangHX
 * @data: 2024/9/23 16:07
 * @desc:
 */
object CsBroadcastManager {

    private const val ACTION_APP_PREFIX = "app_"

    /**
     * 添加带返回值的消息回调，弱引用, 同一个 action 可以绑定多个监听
     * */
    fun addWeakReceiverListener(action: String, listener: OrderedBroadcastMsgListener) {
        CallbackController.addWeakReceiverListener(checkAction(action), listener)
    }

    /**
     * 添加消息回调，弱引用, 同一个 action 可以绑定多个监听
     * */
    fun addWeakReceiverListener(action: String, listener: BroadcastMsgListener) {
        CallbackController.addWeakReceiverListener(checkAction(action), listener)
    }

    /**
     * 移除消息回调
     * */
    fun removeReceiverListener(action: String, listener: BroadcastMsgListener) {
        CallbackController.removeReceiverListener(checkAction(action), listener)
    }


    /**
     * 创建静态广播用于发送
     *
     * @param action    发送意图
     * */
    fun createStaticBroadcast(action: String): IStaticBroadcast {
        return StaticBroadcast(checkAction(action))
    }

    /**
     * 创建动态广播用于发送
     *
     * @param action    发送意图
     * */
    fun createDynamicBroadcast(action: String): IDynamicBroadcast {
        return DynamicBroadcast(checkAction(action))
    }


    private fun checkAction(action: String): String {
        return if (action.startsWith(ACTION_APP_PREFIX)) {
            action
        } else {
            "$ACTION_APP_PREFIX$action"
        }
    }
}