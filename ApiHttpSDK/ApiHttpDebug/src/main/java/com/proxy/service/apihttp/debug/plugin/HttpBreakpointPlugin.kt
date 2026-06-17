package com.proxy.service.apihttp.debug.plugin

import com.proxy.service.debugbridge.base.session.DebugSession
import com.proxy.service.debugbridge.base.plugin.IDebugPlugin

/**
 * ApiHttp 断点调试插件
 */
class HttpBreakpointPlugin : IDebugPlugin {

    companion object {
        const val ID = "apihttp"
    }

    override val id: String = ID

    override val name: String = "ApiHttp"

    override val renderType: String = "http"

    override fun toDisplayJson(session: DebugSession): String {
        return session.payload
    }
}
