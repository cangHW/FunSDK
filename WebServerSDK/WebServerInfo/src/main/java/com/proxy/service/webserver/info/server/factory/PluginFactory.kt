package com.proxy.service.webserver.info.server.factory

import com.proxy.service.webserver.base.constants.Constants
import com.proxy.service.webserver.info.server.factory.base.BaseDispatch
import com.proxy.service.webserver.info.server.factory.route.plugin.PluginConfigRoute
import com.proxy.service.webserver.info.server.factory.route.plugin.PluginListRoute
import com.proxy.service.webserver.info.server.factory.route.plugin.PluginRequestRoute
import com.proxy.service.webserver.base.constants.RouteErrors
import com.proxy.service.webserver.info.server.util.HttpJsonResponse
import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoHTTPD.Response

/**
 * @author: cangHX
 * @date: 2026/6/16 14:35
 * @desc: 插件管理、控制
 */
class PluginFactory : BaseDispatch() {

    companion object {
        private const val TAG = "${Constants.LOG_TAG_START}PluginFactory"
    }

    private val pluginListRoute = PluginListRoute()
    private val pluginConfigRoute = PluginConfigRoute()
    private val pluginRequestRoute = PluginRequestRoute()

    override fun intercept(uri: String, session: IHTTPSession): Boolean {
        if (pluginListRoute.intercept(uri, session)) {
            return true
        }

        if (pluginConfigRoute.intercept(uri, session)) {
            return true
        }

        if (pluginRequestRoute.intercept(uri, session)) {
            return true
        }
        return false
    }

    override fun run(uri: String, session: IHTTPSession): Response {
        if (pluginListRoute.intercept(uri, session)) {
            return pluginListRoute.run(uri, session)
        }

        if (pluginConfigRoute.intercept(uri, session)) {
            return pluginConfigRoute.run(uri, session)
        }

        if (pluginRequestRoute.intercept(uri, session)) {
            return pluginRequestRoute.run(uri, session)
        }

        return HttpJsonResponse.error(Response.Status.NOT_FOUND, RouteErrors.pluginNotMatched(uri))
    }

}
