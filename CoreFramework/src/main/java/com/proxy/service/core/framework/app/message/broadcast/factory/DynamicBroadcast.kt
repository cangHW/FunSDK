package com.proxy.service.core.framework.app.message.broadcast.factory

import android.content.Intent
import com.proxy.service.core.framework.app.message.broadcast.receiver.AppDynamicReceiverImpl
import com.proxy.service.core.framework.app.message.broadcast.factory.base.AbstractDynamicBroadcast

/**
 * @author: cangHX
 * @data: 2026/1/19 11:05
 * @desc:
 */
class DynamicBroadcast(action: String) : AbstractDynamicBroadcast(action) {

    override fun createIntent(): Intent {
        return AppDynamicReceiverImpl.createIntent()
    }

}