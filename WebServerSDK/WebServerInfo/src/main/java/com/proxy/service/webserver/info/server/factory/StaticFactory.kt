package com.proxy.service.webserver.info.server.factory

import com.proxy.service.webserver.base.constants.Constants
import com.proxy.service.webserver.info.server.factory.base.BaseDispatch
import com.proxy.service.webserver.info.server.factory.route.page.PluginPageRoute
import com.proxy.service.webserver.info.server.factory.route.page.RootPageRoute
import com.proxy.service.webserver.base.constants.RouteErrors
import com.proxy.service.webserver.info.server.util.HttpJsonResponse
import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoHTTPD.Response

/**
 * @author: cangHX
 * @date: 2026/6/16 14:57
 * @desc: 静态页面、静态资源
 */
class StaticFactory : BaseDispatch() {

    companion object {
        private const val TAG = "${Constants.LOG_TAG_START}StaticFactory"
    }

    private val rootPageRoute = RootPageRoute()
    private val pluginPageRoute = PluginPageRoute()

    override fun intercept(uri: String, session: IHTTPSession): Boolean {
        if (rootPageRoute.intercept(uri, session)) {
            return true
        }

        if (pluginPageRoute.intercept(uri, session)) {
            return true
        }
        return false
    }

    override fun run(uri: String, session: IHTTPSession): Response {
        if (rootPageRoute.intercept(uri, session)) {
            return rootPageRoute.run(uri, session)
        }

        if (pluginPageRoute.intercept(uri, session)) {
            return pluginPageRoute.run(uri, session)
        }

        return HttpJsonResponse.error(Response.Status.NOT_FOUND, RouteErrors.staticNotMatched(uri))
    }
}
