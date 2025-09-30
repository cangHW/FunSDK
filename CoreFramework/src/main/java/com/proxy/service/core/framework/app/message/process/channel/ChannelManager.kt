package com.proxy.service.core.framework.app.message.process.channel

import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.channel.provider.BroadcastFactory
import com.proxy.service.core.framework.app.message.process.channel.provider.ProviderFactory
import com.proxy.service.core.framework.app.message.process.constants.ShareDataConstants
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2025/9/18 17:47
 * @desc:
 */
object ChannelManager {

    fun send(toPkg: String, sendChannelName: String, message: ShareMessage): ShareMessage? {
        CsLogger.tag(ShareDataConstants.TAG)
            .d("ChannelManager send. toPkg=$toPkg, sendChannelName=$sendChannelName, message=$message")
        return when (sendChannelName) {
            SendChannel.AUTO.name -> {
                sendForAuto(toPkg, message)
            }

            SendChannel.PROVIDER.name -> {
                ProviderFactory.getInstance().send(toPkg, message)
            }

            SendChannel.BROADCAST.name -> {
                BroadcastFactory.getInstance().send(toPkg, message)
            }

            else -> {
                null
            }
        }
    }

    private fun sendForAuto(toPkg: String, message: ShareMessage): ShareMessage? {
        if (ProviderFactory.getInstance().isCanSend(toPkg)){
            return ProviderFactory.getInstance().send(toPkg, message)
        }
        return BroadcastFactory.getInstance().send(toPkg, message)
    }

}