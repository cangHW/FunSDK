package com.proxy.service.core.framework.app.message.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.WeakHashMap

/**
 * @author: cangHX
 * @data: 2024/9/23 16:24
 * @desc:
 */
class BroadcastReceiverImpl : BroadcastReceiver() {

    companion object {
        private const val TAG = "${CoreConfig.TAG}EventReceiver"
        const val ACTION = "com.proxy.service.core.framework.Event"

        private val any = Any()
        private val weakReceiverHashMap = WeakHashMap<MessageReceiverListener, Any>()

        /**
         * 添加弱引用回调
         * */
        fun addWeakReceiverListener(listener: MessageReceiverListener) {
            weakReceiverHashMap[listener] = any
        }

        /**
         * 移除弱引用回调
         * */
        fun removeReceiverListener(listener: MessageReceiverListener) {
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