package com.proxy.service.apihttp.debug

import com.proxy.service.apihttp.debug.plugin.HttpBreakpointPlugin
import com.proxy.service.debugbridge.base.DebugBridgeService

/**
 * ApiHttp 调试模块入口
 */
object ApiHttpDebug {

    fun registerPlugins(service: DebugBridgeService) {
        service.registerPlugin(HttpBreakpointPlugin())
    }
}
