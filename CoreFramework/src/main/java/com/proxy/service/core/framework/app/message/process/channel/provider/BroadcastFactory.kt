package com.proxy.service.core.framework.app.message.process.channel.provider

import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.proxy.service.core.framework.app.install.CsInstallUtils
import com.proxy.service.core.framework.app.message.broadcast.factory.StaticBroadcast
import com.proxy.service.core.framework.app.message.broadcast.callback.OrderBroadcastCallback
import com.proxy.service.core.framework.app.message.broadcast.callback.OrderedBroadcastMsgListener
import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.constants.ShareDataConstants
import com.proxy.service.core.framework.app.message.process.request.RequestDispatch
import com.proxy.service.core.framework.app.message.process.response.ResponseDispatch
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.CountDownLatch

/**
 * @author: cangHX
 * @data: 2025/9/30 10:55
 * @desc:
 */
class BroadcastFactory private constructor() : BaseFactory(), OrderedBroadcastMsgListener {

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

        val latch = CountDownLatch(1)

        var responseBundle: Bundle? = null
        val requestBundle = createBundleByShareMessage(message)
        StaticBroadcast(ShareDataConstants.SHARE_DATA_BROADCAST_ACTION_NAME)
            .setToPackage(toPkg)
            .setExtras(requestBundle)
            .sendOrder(object : OrderBroadcastCallback {
                override fun onFinal(isReceiverAvailable: Boolean, bundle: Bundle) {
                    responseBundle = bundle
                    latch.countDown()
                }
            })


        latch.await()

        val result = getShareMessageFromBundle(responseBundle ?: Bundle())
        CsLogger.tag(ShareDataConstants.TAG)
            .d("BroadcastFactory call msg. fromPkg=$toPkg, message=$result")

        return result
    }

    override fun onOrderReceive(
        context: Context,
        fromPkg: String,
        fromProcessName: String,
        data: Uri?,
        extras: Bundle?,
        resultExtras: Bundle?
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