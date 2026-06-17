package com.proxy.service.debugbridge.nano.server

import android.content.Context
import com.proxy.service.debugbridge.base.plugin.PluginPagePaths
import com.proxy.service.debugbridge.base.plugin.PluginRegistry
import com.proxy.service.debugbridge.base.session.Decision
import com.proxy.service.debugbridge.base.session.SessionManager
import com.proxy.service.debugbridge.nano.NanoDebugBridgeKernel
import com.proxy.service.core.framework.data.json.CsJsonUtils
import fi.iki.elonen.NanoHTTPD
import java.io.ByteArrayInputStream
import java.io.InputStream

/**
 * @author: cangHX
 * @date: 2026/6/15
 * @desc: NanoHTTPD — Shell 路由 + 插件独立 assets 页面
 */
internal class NanoDebugServer(
    private val context: Context,
    port: Int,
    private val accessToken: String?
) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession): Response {
        if (!checkToken(session)) {
            return textResponse(Response.Status.UNAUTHORIZED, "invalid token")
        }

        val uri = normalizeUri(session.uri)
        val method = session.method

        return when {
            uri == "/" || uri == "/index.html" -> serveShellAsset("index.html")
                ?: textResponse(Response.Status.NOT_FOUND, "index missing")
            uri.startsWith("/debug/") -> serveShellAsset(uri.removePrefix("/debug/"))
                ?: textResponse(Response.Status.NOT_FOUND, "asset missing")
            uri == "/api/plugins" && method == Method.GET -> jsonResponse(NanoDebugBridgeKernel.buildPluginsJson())
            uri == "/api/sessions" && method == Method.GET -> jsonResponse(NanoDebugBridgeKernel.buildSessionJson())
            uri.startsWith("/api/session/") && uri.endsWith("/detail") && method == Method.GET -> {
                serveSessionDetail(uri.removePrefix("/api/session/").removeSuffix("/detail"))
            }
            uri.startsWith("/api/session/") && uri.endsWith("/decide") && method == Method.POST -> {
                handleDecide(session, uri.removePrefix("/api/session/").removeSuffix("/decide"))
            }
            uri.startsWith("/plugin/") -> servePluginRoute(uri)
            uri.startsWith("/session/") -> redirectLegacySession(uri.removePrefix("/session/"))
            else -> textResponse(Response.Status.NOT_FOUND, "not found: $uri")
        }
    }

    /**
     * /plugin/{pluginId}/session/{sessionId}
     * /plugin/{pluginId}/{asset...}
     */
    private fun servePluginRoute(uri: String): Response {
        val parts = uri.trim('/').split('/')
        if (parts.size < 2 || parts[0] != "plugin") {
            return textResponse(Response.Status.NOT_FOUND, "invalid plugin route")
        }
        val pluginId = parts[1]
        if (parts.size >= 4 && parts[2] == "session") {
            val sessionId = parts[3]
            val plugin = PluginRegistry.get(pluginId)
            val assetPath = plugin?.sessionPageAssetPath
                ?: PluginPagePaths.defaultSessionPageAsset(pluginId)
            return serveMergedAsset(assetPath, "text/html; charset=utf-8")
                ?: serveFallbackSessionPage(sessionId)
        }
        val relativePath = parts.drop(2).joinToString("/")
        if (relativePath.isBlank()) {
            return textResponse(Response.Status.NOT_FOUND, "plugin asset missing")
        }
        val assetPath = PluginPagePaths.resolvePluginAssetPath(pluginId, relativePath)
        return serveMergedAsset(assetPath, guessMimeType(relativePath))
            ?: textResponse(Response.Status.NOT_FOUND, "plugin asset not found: $assetPath")
    }

    private fun redirectLegacySession(sessionId: String): Response {
        val debugSession = SessionManager.get(sessionId)
            ?: return textResponse(Response.Status.NOT_FOUND, "session not found")
        val target = PluginPagePaths.buildSessionPageUrl(debugSession.pluginId, sessionId)
        return newFixedLengthResponse(Response.Status.REDIRECT, MIME_PLAINTEXT, "")
            .apply { addHeader("Location", target) }
    }

    private fun serveFallbackSessionPage(sessionId: String): Response {
        SessionManager.get(sessionId)
            ?: return textResponse(Response.Status.NOT_FOUND, "session not found")
        return serveShellAsset("session.html")
            ?: textResponse(Response.Status.NOT_FOUND, "fallback session page missing")
    }

    private fun serveShellAsset(relativePath: String): Response? {
        val assetPath = "${PluginPagePaths.SHELL_ASSET_ROOT}/${relativePath.trimStart('/')}"
        return serveMergedAsset(assetPath, guessMimeType(relativePath))
    }

    private fun serveMergedAsset(assetPath: String, mime: String): Response? {
        return try {
            openAsset(assetPath)?.use { input ->
                val bytes = input.readBytes()
                newFixedLengthResponse(
                    Response.Status.OK,
                    mime,
                    ByteArrayInputStream(bytes),
                    bytes.size.toLong()
                )
            }
        } catch (_: Throwable) {
            null
        }
    }

    private fun openAsset(assetPath: String): InputStream? {
        return try {
            context.assets.open(assetPath)
        } catch (_: Throwable) {
            null
        }
    }

    private fun checkToken(session: IHTTPSession): Boolean {
        val token = accessToken ?: return true
        if (session.parms["token"] == token) {
            return true
        }
        return session.headers["X-Debug-Token"] == token
    }

    private fun serveSessionDetail(sessionId: String): Response {
        val debugSession = SessionManager.get(sessionId)
            ?: return textResponse(Response.Status.NOT_FOUND, "session not found")

        val plugin = PluginRegistry.get(debugSession.pluginId)
        val body = linkedMapOf<String, Any?>(
            "id" to debugSession.id,
            "pluginId" to debugSession.pluginId,
            "pluginName" to (plugin?.name ?: debugSession.pluginId),
            "renderType" to (plugin?.renderType ?: "raw"),
            "tag" to debugSession.tag,
            "metadata" to debugSession.metadata,
            "display" to (plugin?.toDisplayJson(debugSession) ?: debugSession.payload),
            "sessionPageUrl" to PluginPagePaths.buildSessionPageUrl(debugSession.pluginId, sessionId),
            "sessionPageAssetPath" to (plugin?.sessionPageAssetPath
                ?: PluginPagePaths.defaultSessionPageAsset(debugSession.pluginId))
        )
        return jsonResponse(CsJsonUtils.toJson(body) ?: "{}")
    }

    private fun handleDecide(httpSession: IHTTPSession, sessionId: String): Response {
        val files = HashMap<String, String>()
        return try {
            httpSession.parseBody(files)
            val body = files["postData"] ?: ""
            val map = CsJsonUtils.fromJsonToMap(body) ?: emptyMap()
            val action = map["action"] ?: "pass"
            val decision = when (action) {
                "replace" -> Decision.Replace(
                    body = map["body"] ?: "",
                    code = map["code"]?.toIntOrNull() ?: 200,
                    message = map["message"] ?: "OK"
                )
                "abort" -> Decision.Abort(map["reason"] ?: "Aborted by debugger")
                else -> Decision.PassThrough
            }
            val debugSession = SessionManager.get(sessionId)
            if (debugSession != null) {
                PluginRegistry.get(debugSession.pluginId)?.applyDecision(debugSession, decision)
            }
            val success = SessionManager.resolve(sessionId, decision)
            jsonResponse("""{"success":$success}""")
        } catch (throwable: Throwable) {
            textResponse(Response.Status.BAD_REQUEST, throwable.message ?: "bad request")
        }
    }

    private fun jsonResponse(json: String): Response {
        return newFixedLengthResponse(
            Response.Status.OK,
            "application/json; charset=utf-8",
            json
        )
    }

    private fun textResponse(status: Response.Status, message: String): Response {
        return newFixedLengthResponse(status, MIME_PLAINTEXT, message)
    }

    private fun normalizeUri(uri: String): String {
        val path = uri.substringBefore('?')
        return if (path.endsWith("/") && path.length > 1) path.dropLast(1) else path
    }

    private fun guessMimeType(path: String): String {
        return when {
            path.endsWith(".html", ignoreCase = true) -> "text/html; charset=utf-8"
            path.endsWith(".js", ignoreCase = true) -> "application/javascript; charset=utf-8"
            path.endsWith(".css", ignoreCase = true) -> "text/css; charset=utf-8"
            path.endsWith(".json", ignoreCase = true) -> "application/json; charset=utf-8"
            path.endsWith(".svg", ignoreCase = true) -> "image/svg+xml"
            path.endsWith(".png", ignoreCase = true) -> "image/png"
            else -> "application/octet-stream"
        }
    }
}
