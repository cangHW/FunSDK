package com.proxy.service.core.framework.app.message.broadcast.factory

import android.content.Intent
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.message.broadcast.factory.base.AbstractStaticBroadcast
import com.proxy.service.core.framework.app.message.broadcast.receiver.AppStaticReceiverImpl

/**
 * @author: cangHX
 * @data: 2026/1/19 11:05
 * @desc:
 */
class StaticBroadcast(action: String) : AbstractStaticBroadcast(action) {

    override fun createIntent(): Intent {
        val pkg = toPkg ?: CsAppUtils.getPackageName()
        return AppStaticReceiverImpl.createIntent(pkg)
    }
}