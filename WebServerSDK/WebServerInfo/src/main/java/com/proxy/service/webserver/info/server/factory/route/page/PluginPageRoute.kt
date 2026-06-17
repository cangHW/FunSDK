package com.proxy.service.webserver.info.server.factory.route.page

import com.proxy.service.webserver.base.constants.Constants
import com.proxy.service.webserver.base.plugin.PluginRegistry
import com.proxy.service.webserver.base.route.PluginWebPaths
import com.proxy.service.webserver.info.server.util.HttpJsonResponse
import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoHTTPD.Response

/**
 * @author: cangHX
 * @date: 2026/6/17 09:48
 * @desc: 插件静态资源路由
 */
class PluginPageRoute : BasePageRoute() {

    companion object {
        private const val TAG = "${Constants.LOG_TAG_START}PluginPageRoute"
    }

    override fun intercept(uri: String, session: IHTTPSession): Boolean {
        return PluginWebPaths.isPluginStaticUri(uri)
    }

    override fun run(uri: String, session: IHTTPSession): Response {
        val staticUri = PluginWebPaths.parsePluginStaticUri(uri)
            ?: return HttpJsonResponse.error(
                Response.Status.NOT_FOUND,
                "plugin asset not found: $uri"
            )

        val plugin = PluginRegistry.get(staticUri.pluginId)
            ?: return HttpJsonResponse.error(
                Response.Status.NOT_FOUND,
                "plugin not found: ${staticUri.pluginId}"
            )

        val assetPath = plugin.getAssetPath(staticUri.relativePath)
        val response = serveMergedAsset(assetPath, guessMimeType(assetPath))
        if (response != null) {
            return response
        }

        return HttpJsonResponse.error(Response.Status.NOT_FOUND, "plugin asset not found: $uri")
    }
}
