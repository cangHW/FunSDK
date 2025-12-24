package com.proxy.service.widget.info.notification.channel

import android.os.Build
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.widget.info.notification.channel.config.ChannelConfig
import com.proxy.service.widget.info.notification.channel.support.ChannelCompat
import com.proxy.service.widget.info.notification.channel.support.ChannelV26

/**
 * @author: cangHX
 * @data: 2025/12/18 20:31
 * @desc:
 */
class ChannelManager private constructor() {

    companion object {
        private val _instance by lazy { ChannelManager() }

        fun getInstance(): ChannelManager {
            return _instance
        }
    }

    private val channels = CsExcellentMap<String, ChannelConfig>()

    fun put(channel: ChannelConfig) {
        channels.putSync(channel.getChannelId(), channel)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ChannelV26.doWork(channel)
        }else{
            ChannelCompat.doWork(channel)
        }
    }

    fun get(channelId: String): ChannelConfig? {
        return channels.get(channelId)
    }
}