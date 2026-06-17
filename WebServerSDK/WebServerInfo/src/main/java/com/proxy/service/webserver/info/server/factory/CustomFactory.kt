package com.proxy.service.webserver.info.server.factory

import com.proxy.service.webserver.base.constants.Constants
import com.proxy.service.webserver.base.constants.RouteErrors
import com.proxy.service.webserver.base.plugin.PluginRegistry
import com.proxy.service.webserver.base.route.CustomWebPaths
import com.proxy.service.webserver.info.server.factory.base.BaseDispatch
import com.proxy.service.webserver.info.server.util.HttpJsonResponse
import com.proxy.service.webserver.info.server.util.NanoHttpRequestAdapter
import com.proxy.service.webserver.info.server.util.NanoHttpResponseAdapter
import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoHTTPD.Response

/**
 * @author: cangHX
 * @date: 2026/6/16 14:50
 * @desc: 自定义请求, 分发给对应插件
 */
class CustomFactory : BaseDispatch() {

    companion object {
        private const val TAG = "${Constants.LOG_TAG_START}CustomFactory"
    }

    override fun intercept(uri: String, session: IHTTPSession): Boolean {
        return CustomWebPaths.isCustomUri(uri)
    }

    override fun run(uri: String, session: IHTTPSession): Response {
        val customUri = CustomWebPaths.parseCustomUri(uri)
            ?: return HttpJsonResponse.error(
                Response.Status.NOT_FOUND,
                RouteErrors.customNotMatched(uri)
            )

        val plugin = PluginRegistry.get(customUri.pluginId)
            ?: return HttpJsonResponse.error(
                Response.Status.NOT_FOUND,
                "plugin not found: ${customUri.pluginId}"
            )

        val request = NanoHttpRequestAdapter(session)
        val result = plugin.requestCustom(uri, request)
        if (result != null) {
            return NanoHttpResponseAdapter.toNanoResponse(result)
        }
        return HttpJsonResponse.error(Response.Status.NOT_FOUND, RouteErrors.customNotMatched(uri))
    }
}
