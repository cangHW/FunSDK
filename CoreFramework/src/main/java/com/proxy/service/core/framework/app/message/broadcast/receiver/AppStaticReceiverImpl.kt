package com.proxy.service.core.framework.app.message.broadcast.receiver

import android.content.Intent
import com.proxy.service.core.constants.CoreConfig

/**
 * @author: cangHX
 * @data: 2024/9/23 16:24
 * @desc:
 */
class AppStaticReceiverImpl : BaseReceiver() {

    companion object {
        private const val TAG = "${CoreConfig.TAG}Msg_Broadcast_Static"
        private const val ACTION = "com.proxy.service.core.framework.Event.Static"

        fun createIntent(toPkg: String): Intent {
            val intent = Intent(ACTION)
            intent.setClassName(toPkg, AppStaticReceiverImpl::class.java.name)
            return intent
        }
    }

    override fun getLogTag(): String {
        return TAG
    }

    override fun getAction(): String {
        return ACTION
    }

}