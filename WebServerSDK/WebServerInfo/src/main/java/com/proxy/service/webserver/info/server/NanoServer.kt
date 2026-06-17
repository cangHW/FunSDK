package com.proxy.service.webserver.info.server

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webserver.base.config.WebServerConfig
import com.proxy.service.webserver.base.constants.Constants
import com.proxy.service.webserver.info.server.factory.CustomFactory
import com.proxy.service.webserver.info.server.factory.PluginFactory
import com.proxy.service.webserver.info.server.factory.StaticFactory
import com.proxy.service.webserver.base.constants.RouteErrors
import com.proxy.service.webserver.info.server.util.HttpJsonResponse
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.Response
import fi.iki.elonen.NanoHTTPD.Response.Status

/**
 * @author: cangHX
 * @date: 2026/6/15 17:54
 * @desc: NanoHTTPD — Shell 路由 + 插件独立 assets 页面
 */
internal class NanoServer(
    config: WebServerConfig
) : NanoHTTPD(config.getPort()) {

    companion object {
        private const val TAG = "${Constants.LOG_TAG_START}NanoHTTPD"
    }

    private val staticFactory = StaticFactory()
    private val pluginFactory = PluginFactory()
    private val customFactory = CustomFactory()

    override fun serve(session: IHTTPSession): Response {
        CsLogger.tag(TAG).d("serve: method=${session.method}, uri=${session.uri}")

        val host = normalizeUri(session.uri)
        if (staticFactory.intercept(host, session)) {
            return staticFactory.run(host, session)
        }

        if (pluginFactory.intercept(host, session)) {
            return pluginFactory.run(host, session)
        }

        if (customFactory.intercept(host, session)) {
            return customFactory.run(host, session)
        }

        return HttpJsonResponse.error(Status.NOT_FOUND, RouteErrors.notMatched(host))
    }

    private fun normalizeUri(uri: String): String {
        val path = uri.substringBefore('?')
        return if (path.endsWith("/") && path.length > 1) {
            path.dropLast(1)
        } else {
            path
        }
    }
}
