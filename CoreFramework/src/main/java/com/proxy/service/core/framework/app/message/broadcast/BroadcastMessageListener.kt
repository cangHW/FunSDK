package com.proxy.service.core.framework.app.message.broadcast

import android.content.Context
import android.net.Uri
import android.os.Bundle

/**
 * 接收消息回调
 *
 * @author: cangHX
 * @data: 2024/12/2 15:20
 * @desc:
 */
interface BroadcastMessageListener {

    /**
     * 接收到消息
     */
    fun onReceive(context: Context, fromPkg: String, data: Uri?, extras: Bundle?)

}