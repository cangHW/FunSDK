package com.proxy.service.webserver.info.server.factory.route.plugin

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webserver.base.constants.Constants
import com.proxy.service.webserver.base.plugin.IWebServerPlugin
import com.proxy.service.webserver.base.plugin.PluginRegistry
import com.proxy.service.webserver.base.constants.RouteErrors
import com.proxy.service.webserver.base.route.PluginApiPaths
import com.proxy.service.webserver.info.server.factory.base.BaseDispatch
import com.proxy.service.webserver.info.server.util.HttpJsonResponse
import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoHTTPD.Method
import fi.iki.elonen.NanoHTTPD.Response

/**
 * @author: cangHX
 * @date: 2026/6/16 20:34
 * @desc:
 */
class PluginRequestRoute : BaseDispatch() {

    companion object {
        private const val TAG = "${Constants.LOG_TAG_START}PluginRequestRoute"
    }

    override fun intercept(uri: String, session: IHTTPSession): Boolean {
        return PluginApiPaths.isPluginApiAction(uri, Constants.API_PLUGIN_REQUEST_ACTION)
    }

    override fun run(uri: String, session: IHTTPSession): Response {
        val apiUri = PluginApiPaths.parsePluginApiUri(uri)
            ?: return HttpJsonResponse.error(Response.Status.NOT_FOUND, RouteErrors.pluginNotMatched(uri))

        val plugin = PluginRegistry.get(apiUri.pluginId)
            ?: return HttpJsonResponse.error(
                Response.Status.NOT_FOUND,
                "plugin not found: ${apiUri.pluginId}"
            )

        if (session.method != Method.GET) {
            return HttpJsonResponse.error(
                Response.Status.METHOD_NOT_ALLOWED,
                "GET required for $uri"
            )
        }
        return handleRequestData(plugin)
    }

    private fun handleRequestData(plugin: IWebServerPlugin): Response {
        return try {
            val dataJson = plugin.requestData()
            HttpJsonResponse.success(dataJson)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            HttpJsonResponse.error(
                Response.Status.INTERNAL_ERROR,
                throwable.message ?: "request data failed"
            )
        }
    }
}