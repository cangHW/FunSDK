package com.proxy.service.core.framework.app.message.broadcast

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
interface OrderedBroadcastMessageListener : BroadcastMessageListener {

    override fun onReceive(context: Context, fromPkg: String, data: Uri?, extras: Bundle?) {

    }

    /**
     * 接收到消息
     */
    fun onOrderReceive(context: Context, fromPkg: String, data: Uri?, extras: Bundle?): Bundle?

}