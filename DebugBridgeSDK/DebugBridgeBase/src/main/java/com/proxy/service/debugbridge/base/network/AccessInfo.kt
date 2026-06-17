package com.proxy.service.debugbridge.base.network

import com.proxy.service.debugbridge.base.plugin.PluginPagePaths

/**
 * @author: cangHX
 * @date: 2026/6/15
 * @desc:
 */
data class AccessInfo(
    val port: Int,
    val host: String,
    val lanUrl: String,
    val localhostHint: String
) {

    fun sessionUrl(pluginId: String, sessionId: String): String {
        return lanUrl + PluginPagePaths.buildSessionPageUrl(pluginId, sessionId)
    }
}
