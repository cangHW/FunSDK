package com.proxy.service.core.framework.app.message.process.channel.provider

import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.proxy.service.core.framework.app.install.CsInstallUtils
import com.proxy.service.core.framework.app.message.broadcast.BroadcastMessageListener
import com.proxy.service.core.framework.app.message.broadcast.BroadcastReceiverImpl
import com.proxy.service.core.framework.app.message.broadcast.OrderedBroadcastMessageListener
import com.proxy.service.core.framework.app.message.process.bean.MessageType
import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.bean.ShareMessageFactory
import com.proxy.service.core.framework.app.message.process.channel.ChannelManager
import com.proxy.service.core.framework.app.message.process.constants.ShareDataConstants
import com.proxy.service.core.framework.app.message.process.request.RequestDispatch
import com.proxy.service.core.framework.app.message.process.response.ResponseDispatch
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2025/9/30 10:55
 * @desc:
 */
class BroadcastFactory private constructor() : BaseFactory(), OrderedBroadcastMessageListener {

    companion object {
        private val _instance by lazy { BroadcastFactory() }

        fun getInstance(): BroadcastFactory {
            return _instance
        }
    }

    override fun isCanSend(toPkg: String): Boolean {
        if (!CsInstallUtils.isInstallApp(toPkg)) {
            CsLogger.tag(ShareDataConstants.TAG)
                .e("The target application could not be found. We continued to try sending messages via broadcast, but there is no guarantee of success. toPkg=$toPkg")
        }
        return true
    }

    override fun send(toPkg: String, message: ShareMessage): ShareMessage? {
        isCanSend(toPkg)

        CsLogger.tag(ShareDataConstants.TAG)
            .d("BroadcastFactory send msg. toPkg=$toPkg, message=$message")

        val requestBundle = createBundleByShareMessage(message)
        val responseBundle = BroadcastReceiverImpl.sendOrder(
            ShareDataConstants.SHARE_DATA_BROADCAST_ACTION_NAME,
            toPkg,
            null,
            requestBundle
        )
        val result = getShareMessageFromBundle(responseBundle)
        CsLogger.tag(ShareDataConstants.TAG)
            .d("BroadcastFactory call msg. fromPkg=$toPkg, message=$result")
        return result
    }

    override fun onOrderReceive(
        context: Context,
        fromPkg: String,
        data: Uri?,
        extras: Bundle?
    ): Bundle? {
        val message: ShareMessage = getShareMessageFromBundle(extras) ?: return null
        CsLogger.tag(ShareDataConstants.TAG)
            .d("BroadcastFactory receive msg. fromPkg=$fromPkg, message=$message")

        val resultMessage = if (message.isRequest()) {
            RequestDispatch.dispatch(fromPkg, message)
        } else if (message.isResponse()) {
            ResponseDispatch.dispatch(message)
        } else {
            null
        }
        if (resultMessage != null) {
            return createBundleByShareMessage(resultMessage)
        }
        return null
    }
}