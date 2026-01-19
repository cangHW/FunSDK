package com.proxy.service.core.framework.app.message.broadcast.controller

import com.proxy.service.core.framework.app.message.broadcast.callback.BroadcastMsgListener
import com.proxy.service.core.framework.app.message.broadcast.receiver.AppDynamicReceiverImpl
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.collections.CsExcellentSet
import com.proxy.service.core.framework.collections.type.Type

/**
 * @author: cangHX
 * @data: 2026/1/19 16:15
 * @desc:
 */
object CallbackController {

    private val receiverMap = CsExcellentMap<String, CsExcellentSet<BroadcastMsgListener>>()

    /**
     * 添加弱引用回调
     * */
    fun addWeakReceiverListener(action: String, listener: BroadcastMsgListener) {
        receiverMap.runInTransaction {
            var set = receiverMap.get(action)
            if (set == null) {
                set = CsExcellentSet(Type.WEAK)
                receiverMap.putSync(action, set)
            }
            set.putSync(listener)
            AppDynamicReceiverImpl.registerNumChanged(receiverMap.size())
        }
    }

    /**
     * 移除弱引用回调
     * */
    fun removeReceiverListener(action: String, listener: BroadcastMsgListener) {
        receiverMap.runInTransaction {
            val set = receiverMap.get(action)
            set?.removeSync(listener)
            if ((set?.size() ?: 0) == 0) {
                receiverMap.removeSync(action)
            }
            AppDynamicReceiverImpl.registerNumChanged(receiverMap.size())
        }
    }

    fun getReceiverList(action: String): CsExcellentSet<BroadcastMsgListener>? {
        return receiverMap.get(action)
    }
}