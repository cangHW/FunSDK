package com.proxy.service.core.framework.app.message.broadcast

import android.net.Uri
import android.os.Bundle
import com.proxy.service.core.framework.app.CsAppUtils
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

    private const val ACTION_PREFIX = "app_"

    /**
     * 添加消息回调，弱引用, 同一个 action 可以绑定多个监听
     * */
    fun addWeakReceiverListener(action: String, listener: BroadcastMessageListener) {
        BroadcastReceiverImpl.addWeakReceiverListener(checkAction(action), listener)
    }

    /**
     * 移除消息回调
     * */
    fun removeReceiverListener(action: String, listener: BroadcastMessageListener) {
        BroadcastReceiverImpl.removeReceiverListener(checkAction(action), listener)
    }

    /**
     * 发送广播, 给当前应用
     *
     * @param action                发送意图
     * @param uri                   待发送内容，可为空
     * */
    fun sendMessage(action: String, uri: Uri?) {
        sendMessage(action, CsAppUtils.getPackageName(), uri, null)
    }

    /**
     * 发送广播, 给当前应用
     *
     * @param action                发送意图
     * @param extras                待发送内容，可为空
     * */
    fun sendMessage(action: String, extras: Bundle?) {
        sendMessage(action, CsAppUtils.getPackageName(), null, extras)
    }

    /**
     * 发送广播, 给当前应用
     *
     * @param action                发送意图
     * @param uri                   待发送内容，可为空
     * @param extras                待发送内容，可为空
     * */
    fun sendMessage(action: String, uri: Uri?, extras: Bundle?) {
        sendMessage(action, CsAppUtils.getPackageName(), uri, extras)
    }

    /**
     * 发送广播
     *
     * @param action                发送意图
     * @param toPkg                 接收方包名
     * @param uri                   待发送内容，可为空
     * @param extras                待发送内容，可为空
     * */
    fun sendMessage(action: String, toPkg: String, uri: Uri?, extras: Bundle?) {
        if (!CsInstallUtils.isInstallApp(toPkg)) {
            CsLogger.tag(BroadcastReceiverImpl.TAG)
                .e("The target app is not installed. package: $toPkg")
            return
        }

        BroadcastReceiverImpl.send(checkAction(action), toPkg, uri, extras)
    }


    private fun checkAction(action: String): String {
        return if (action.startsWith(ACTION_PREFIX)) {
            action
        } else {
            "$ACTION_PREFIX$action"
        }
    }
}