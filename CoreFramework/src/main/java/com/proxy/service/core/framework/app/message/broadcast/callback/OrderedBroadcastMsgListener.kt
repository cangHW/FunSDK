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
        action: String,
        fromPkg: String,
        fromProcessName: String,
        data: Uri?,
        extras: Bundle?
    ) {
        // do nothing
    }

    /**
     * 接收到消息
     *
     * @param action            广播消息事件
     * @param fromPkg           广播发送方的包名
     * @param fromProcessName   广播发送方的进程名
     * @param data              广播发送方传递的数据
     * @param extras            广播发送方传递的数据
     * @param resultExtras      准备返回给广播发送方的数据
     */
    fun onOrderReceive(
        context: Context,
        action: String,
        fromPkg: String,
        fromProcessName: String,
        data: Uri?,
        extras: Bundle?,
        resultExtras: Bundle?
    ): Bundle?

}