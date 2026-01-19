package com.proxy.service.core.framework.app.message.broadcast.callback

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
interface BroadcastMsgListener {

    /**
     * 接收到消息
     *
     * @param fromPkg       消息来源包名
     * @param processName   消息来源进程名
     */
    fun onReceive(context: Context, fromPkg: String, processName: String, data: Uri?, extras: Bundle?)

}