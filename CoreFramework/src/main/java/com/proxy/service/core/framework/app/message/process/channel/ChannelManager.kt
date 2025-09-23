package com.proxy.service.core.framework.app.message.process.channel

import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.channel.provider.ProviderFactory

/**
 * @author: cangHX
 * @data: 2025/9/18 17:47
 * @desc:
 */
object ChannelManager {

    fun send(toPkg: String, channelName: String, message: ShareMessage): ShareMessage? {
        return when (channelName) {
            ChannelEnum.AUTO.name -> {
                sendForAuto(toPkg, message)
            }

            ChannelEnum.PROVIDER.name -> {
                ProviderFactory.getInstance().send(toPkg, message)
            }

            else -> {
                null
            }
        }
    }

    private fun sendForAuto(toPkg: String, message: ShareMessage): ShareMessage? {
        return ProviderFactory.getInstance().send(toPkg, message)
    }

}