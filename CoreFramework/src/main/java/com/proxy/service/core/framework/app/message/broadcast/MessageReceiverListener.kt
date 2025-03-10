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
interface MessageReceiverListener {

    /**
     * 接收到消息
     */
    fun onReceive(context: Context, data: Uri?, extras: Bundle?)

}