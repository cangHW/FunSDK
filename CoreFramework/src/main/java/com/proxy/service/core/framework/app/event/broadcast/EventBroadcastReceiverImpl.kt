package com.proxy.service.core.framework.app.event.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.app.install.InstallBroadcastReceiverImpl
import com.proxy.service.core.framework.app.install.InstallBroadcastReceiverImpl.Companion
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.WeakHashMap

/**
 * @author: cangHX
 * @data: 2024/9/23 16:24
 * @desc:
 */
class EventBroadcastReceiverImpl : BroadcastReceiver() {

    /**
     * 接收消息回调
     */
    interface ReceiverListener {
        /**
         * 接收到消息
         */
        fun onReceive(context: Context, data: Uri?, extras: Bundle?)
    }

    companion object {
        private const val TAG = "${Constants.TAG}EventReceiver"
        const val ACTION = "com.proxy.service.core.framework.Event"

        private val any = Any()
        private val weakReceiverHashMap = WeakHashMap<ReceiverListener, Any>()

        /**
         * 添加弱引用回调
         * */
        fun addWeakReceiverListener(listener: ReceiverListener) {
            weakReceiverHashMap[listener] = any
        }

        /**
         * 移除弱引用回调
         * */
        fun removeReceiverListener(listener: ReceiverListener) {
            weakReceiverHashMap.remove(listener)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) {
            return
        }
        if (intent?.action != ACTION) {
            return
        }

        val data = intent.data
        val extras = intent.extras

        CsLogger.tag(TAG).d("onReceive extras = $extras, data = $data")

        try {
            weakReceiverHashMap.iterator().forEach {
                try {
                    it.key.onReceive(context, data, extras)
                } catch (throwable: Throwable) {
                    CsLogger.tag(TAG).e(throwable)
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }
}