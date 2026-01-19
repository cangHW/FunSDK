package com.proxy.service.core.framework.app.message.broadcast.callback

import android.content.Context
import android.net.Uri
import android.os.Bundle

/**
 * 接收有序广播回调
 *
 * @author: cangHX
 * @data: 2024/12/2 15:20
 * @desc:
 */
interface OrderedBroadcastMsgListener : BroadcastMsgListener {

    override fun onReceive(
        context: Context,
        fromPkg: String,
        processName: String,
        data: Uri?,
        extras: Bundle?
    ) {
        // do nothing
    }

    /**
     * 接收到消息
     */
    fun onOrderReceive(
        context: Context,
        fromPkg: String,
        processName: String,
        data: Uri?,
        extras: Bundle?
    ): Bundle?

}