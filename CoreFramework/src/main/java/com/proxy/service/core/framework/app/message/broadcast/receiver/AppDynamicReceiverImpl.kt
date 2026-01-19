package com.proxy.service.core.framework.app.message.broadcast.receiver

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2026/1/19 10:31
 * @desc:
 */
class AppDynamicReceiverImpl : BaseReceiver() {

    companion object {
        private const val TAG = "${CoreConfig.TAG}Msg_Broadcast_Dynamic"
        private const val ACTION = "com.proxy.service.core.framework.Event.Dynamic"

        private val isStart = AtomicBoolean(false)
        private val receiver = AppDynamicReceiverImpl()

        fun createIntent(): Intent {
            return Intent(ACTION)
        }

        fun registerNumChanged(registerNum: Int) {
            if (registerNum > 0) {
                if (isStart.compareAndSet(false, true)) {
                    registerReceiver()
                }
            } else {
                if (isStart.compareAndSet(true, false)) {
                    unregisterReceiver()
                }
            }
        }

        private fun registerReceiver() {
            CsLogger.tag(TAG).i("registerReceiver")

            val intentFilter = IntentFilter(ACTION)
            val context = CsContextManager.getApplication()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(receiver, intentFilter, Context.RECEIVER_EXPORTED)
            } else {
                context.registerReceiver(receiver, intentFilter)
            }
        }

        private fun unregisterReceiver() {
            CsLogger.tag(TAG).i("unregisterReceiver")

            CsContextManager.getApplication().unregisterReceiver(receiver)
        }
    }

    override fun getLogTag(): String {
        return TAG
    }

    override fun getAction(): String {
        return ACTION
    }

}