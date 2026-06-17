package com.proxy.service.webserver.info.server.factory.route.page

import com.proxy.service.webserver.base.constants.Constants
import com.proxy.service.webserver.base.route.FileRouteLocalPaths
import com.proxy.service.webserver.base.route.ShellWebPaths
import com.proxy.service.webserver.info.server.util.HttpJsonResponse
import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoHTTPD.Response

/**
 * @author: cangHX
 * @date: 2026/6/17 09:48
 * @desc: Shell 主页与公共静态资源：/、/index.html、/static/res/{relativePath}
 */
class RootPageRoute : BasePageRoute() {

    companion object {
        private const val TAG = "${Constants.LOG_TAG_START}RootPageRoute"
    }

    override fun intercept(uri: String, session: IHTTPSession): Boolean {
        return ShellWebPaths.isShellRootPage(uri) || ShellWebPaths.isShellResUri(uri)
    }

    override fun run(uri: String, session: IHTTPSession): Response {
        if (ShellWebPaths.isShellRootPage(uri)) {
            return serveRootPage()
        }

        val relativePath = ShellWebPaths.parseShellResUri(uri)
        if (relativePath != null) {
            return serveShellAsset(relativePath, uri)
        }

        return HttpJsonResponse.error(Response.Status.NOT_FOUND, "shell page not found: $uri")
    }

    private fun serveRootPage(): Response {
        val rootPagePath = FileRouteLocalPaths.defaultShellRootPageAssetPath()
        val response = serveMergedAsset(rootPagePath, guessMimeType(rootPagePath))
        if (response != null) {
            return response
        }
        return HttpJsonResponse.error(Response.Status.NOT_FOUND, "index missing")
    }

    private fun serveShellAsset(relativePath: String, uri: String): Response {
        val assetPath = FileRouteLocalPaths.resolveShellAssetPath(relativePath)
        val response = serveMergedAsset(assetPath, guessMimeType(relativePath))
        if (response != null) {
            return response
        }
        return HttpJsonResponse.error(Response.Status.NOT_FOUND, "asset not found: $uri")
    }
}
