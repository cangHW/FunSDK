package com.proxy.service.core.framework.app.message.broadcast.factory.base

import android.net.Uri
import android.os.Bundle
import com.proxy.service.core.framework.app.message.broadcast.callback.OrderBroadcastCallback

/**
 * @author: cangHX
 * @data: 2026/1/19 11:14
 * @desc:
 */
interface IBroadcast<T> {

    /**
     * 接收方包名
     * */
    fun setToPackage(toPkg: String): T

    /**
     * 待发送内容, 默认为空
     * */
    fun setUri(uri: Uri): T

    /**
     * 待发送内容, 默认为空
     * */
    fun setExtras(extras: Bundle): T

    /**
     * 发送无序广播
     * */
    fun send()

    /**
     * 发送有序广播
     *
     * @param callback  消息完成回调
     * */
    fun sendOrder(callback: OrderBroadcastCallback)
}