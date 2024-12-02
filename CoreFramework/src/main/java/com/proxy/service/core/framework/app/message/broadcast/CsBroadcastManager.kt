package com.proxy.service.core.framework.app.message.broadcast

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.proxy.service.core.framework.app.context.CsContextManager

/**
 * 广播相关操作工具，只能接收到当前类发送的广播。
 *
 * @author: cangHX
 * @data: 2024/9/23 16:07
 * @desc:
 */
object CsBroadcastManager {

    /**
     * 添加消息回调，弱引用
     * */
    fun addWeakReceiverListener(listener: MessageReceiverListener) {
        BroadcastReceiverImpl.addWeakReceiverListener(listener)
    }

    /**
     * 移除消息回调
     * */
    fun removeReceiverListener(listener: MessageReceiverListener) {
        BroadcastReceiverImpl.removeReceiverListener(listener)
    }

    /**
     * 发送广播
     *
     * @param receiverPackageName   接收方包名
     * @param uri                   待发送内容，可为空
     * @param extras                待发送内容，可为空
     * */
    fun sendBroadcast(receiverPackageName: String, uri: Uri?, extras: Bundle?) {
        val context = CsContextManager.getApplication()
        val intent = Intent(BroadcastReceiverImpl.ACTION)
        intent.setClassName(
            receiverPackageName,
            BroadcastReceiverImpl::class.java.name
        )
        intent.data = uri
        extras?.let {
            intent.putExtras(it)
        }
        context.sendBroadcast(intent)
    }

}