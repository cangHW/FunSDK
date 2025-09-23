package com.proxy.service.core.framework.app.message.broadcast

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.install.CsInstallUtils
import com.proxy.service.core.framework.data.log.CsLogger

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
    fun addWeakReceiverListener(listener: BroadcastMessageListener) {
        BroadcastReceiverImpl.addWeakReceiverListener(listener)
    }

    /**
     * 移除消息回调
     * */
    fun removeReceiverListener(listener: BroadcastMessageListener) {
        BroadcastReceiverImpl.removeReceiverListener(listener)
    }

    /**
     * 发送广播
     *
     * @param toPkg                 接收方包名
     * @param uri                   待发送内容，可为空
     * @param extras                待发送内容，可为空
     * */
    fun sendMessage(toPkg: String, uri: Uri?, extras: Bundle?) {
        if (!CsInstallUtils.isInstallApp(toPkg)) {
            CsLogger.tag(BroadcastReceiverImpl.TAG)
                .e("The target app is not installed. package: $toPkg")
            return
        }

        val context = CsContextManager.getApplication()
        val intent = Intent(BroadcastReceiverImpl.ACTION)
        intent.setClassName(toPkg, BroadcastReceiverImpl::class.java.name)
        intent.data = uri
        extras?.let {
            intent.putExtras(it)
        }
        context.sendBroadcast(intent)
    }

}