package com.proxy.service.webserver.info.server.factory.route.plugin

import com.proxy.service.webserver.base.constants.ApiResponseKeys
import com.proxy.service.webserver.base.plugin.PluginRegistry
import com.proxy.service.webserver.base.route.PluginApiPaths
import com.proxy.service.webserver.info.server.factory.base.BaseDispatch
import com.proxy.service.webserver.info.server.util.HttpJsonResponse
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.Method
import fi.iki.elonen.NanoHTTPD.Response
import org.json.JSONArray
import org.json.JSONObject

/**
 * @author: cangHX
 * @date: 2026/6/16 19:08
 * @desc:
 */
class PluginListRoute : BaseDispatch() {

    override fun intercept(uri: String, session: NanoHTTPD.IHTTPSession): Boolean {
        return PluginApiPaths.isPluginListUri(uri)
    }

    override fun run(uri: String, session: NanoHTTPD.IHTTPSession): Response {
        if (session.method != Method.GET) {
            return HttpJsonResponse.error(
                Response.Status.METHOD_NOT_ALLOWED,
                "GET required for $uri"
            )
        }
        return handleListPlugins()
    }

    private fun handleListPlugins(): Response {
        val root = JSONObject()
        val array = JSONArray()
        PluginRegistry.list().forEach {
            val obj = JSONObject()
            obj.put(ApiResponseKeys.ID, it.getPluginId())
            obj.put(ApiResponseKeys.TITLE, it.getPluginTitle())
            obj.put(ApiResponseKeys.PAGE_URL, it.getRootPageUrl())
            array.put(obj)
        }
        root.put(ApiResponseKeys.LIST, array)
        return HttpJsonResponse.success(root)
    }

}
