package com.proxy.service.apihttp.webserver.plugin

import com.proxy.service.apihttp.webserver.internal.HttpProxyManager
import com.proxy.service.apihttp.webserver.model.HttpProxyDecision
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webserver.base.constants.ApiResponseKeys
import com.proxy.service.webserver.base.http.WebServerHttpRequest
import com.proxy.service.webserver.base.http.WebServerHttpResponse
import com.proxy.service.webserver.base.plugin.IWebServerPlugin
import com.proxy.service.webserver.base.plugin.PluginDataResponse
import com.proxy.service.webserver.base.route.CustomWebPaths
import org.json.JSONObject

/**
 * ApiHttp Web 控制台插件：HTTP 代理 / 拦截
 */
class ApiHttpWebServerPlugin : IWebServerPlugin {

    companion object {
        const val PLUGIN_ID = "apihttp"
        private const val TAG = "CsApiHttp_Plugin"
    }

    override fun getPluginId(): String = PLUGIN_ID

    override fun getPluginTitle(): String = "ApiHttp"

    override fun onConfigChanged(config: String) {
        HttpProxyManager.updateConfigFromJson(config)
    }

    override fun requestData(): JSONObject {
        return HttpProxyManager.overviewJson()
    }

    override fun requestCustom(uri: String, request: WebServerHttpRequest): WebServerHttpResponse? {
        val customUri = CustomWebPaths.parseCustomUri(uri) ?: return null
        if (customUri.pluginId != PLUGIN_ID) {
            return null
        }
        return when (val subPath = customUri.subPath.trim('/')) {
            "config" -> handleConfig(request)
            "reset" -> handleReset(request)
            "sessions" -> handleSessionList(request)
            else -> handleSessionSubPath(subPath, request)
        }
    }

    override fun handlePluginData(payload: String): PluginDataResponse {
        return PluginDataResponse.SUCCESS
    }

    private fun handleConfig(request: WebServerHttpRequest): WebServerHttpResponse {
        return when (request.method.toUpperCase()) {
            "GET" -> WebServerHttpResponse.okJson(HttpProxyManager.configJson().toString())
            "POST" -> {
                val body = request.readFormBody()
                val config = body[ApiResponseKeys.CONFIG] ?: body["body"] ?: ""
                if (config.isNotBlank()) {
                    HttpProxyManager.updateConfigFromJson(config)
                }
                WebServerHttpResponse.okJson(HttpProxyManager.configJson().toString())
            }
            else -> WebServerHttpResponse.error(405, "method not allowed")
        }
    }

    private fun handleReset(request: WebServerHttpRequest): WebServerHttpResponse {
        if (request.method.toUpperCase() != "POST") {
            return WebServerHttpResponse.error(405, "POST required")
        }
        HttpProxyManager.resetConfig()
        return WebServerHttpResponse.okJson(HttpProxyManager.configJson().toString())
    }

    private fun handleSessionList(request: WebServerHttpRequest): WebServerHttpResponse {
        if (request.method.toUpperCase() != "GET") {
            return WebServerHttpResponse.error(405, "GET required")
        }
        val json = JSONObject().put("list", HttpProxyManager.listCapturesJson())
        return WebServerHttpResponse.okJson(json.toString())
    }

    private fun handleSessionSubPath(
        subPath: String,
        request: WebServerHttpRequest,
    ): WebServerHttpResponse {
        if (!subPath.startsWith("sessions/")) {
            return WebServerHttpResponse.error(404, "not found: $subPath")
        }
        val remainder = subPath.removePrefix("sessions/").trim('/')
        val parts = remainder.split('/')
        if (parts.isEmpty() || parts[0].isBlank()) {
            return WebServerHttpResponse.error(404, "session id required")
        }
        val sessionId = parts[0]
        val action = parts.getOrNull(1) ?: "detail"

        return when (action) {
            "detail" -> handleSessionDetail(sessionId, request)
            "decide" -> handleSessionDecide(sessionId, request)
            else -> WebServerHttpResponse.error(404, "unknown action: $action")
        }
    }

    private fun handleSessionDetail(
        sessionId: String,
        request: WebServerHttpRequest,
    ): WebServerHttpResponse {
        if (request.method.toUpperCase() != "GET") {
            return WebServerHttpResponse.error(405, "GET required")
        }
        val capture = HttpProxyManager.getCapture(sessionId)
            ?: return WebServerHttpResponse.error(404, "session not found: $sessionId")
        return WebServerHttpResponse.okJson(HttpProxyManager.captureToJson(capture).toString())
    }

    private fun handleSessionDecide(
        sessionId: String,
        request: WebServerHttpRequest,
    ): WebServerHttpResponse {
        if (request.method.toUpperCase() != "POST") {
            return WebServerHttpResponse.error(405, "POST required")
        }
        val body = request.readFormBody()
        val payloadText = body["payload"] ?: body[ApiResponseKeys.CONFIG] ?: "{}"
        val decision = parseDecision(payloadText)
        val resolved = HttpProxyManager.resolveDecision(sessionId, decision)
        if (!resolved) {
            return WebServerHttpResponse.error(404, "pending session not found: $sessionId")
        }
        CsLogger.tag(TAG).d("session decided: $sessionId -> ${decision.javaClass.simpleName}")
        return WebServerHttpResponse.okJson(JSONObject().put("ok", true).toString())
    }

    private fun parseDecision(payloadText: String): HttpProxyDecision {
        return try {
            val json = JSONObject(payloadText)
            when (json.optString("action", "pass")) {
                "replace" -> HttpProxyDecision.Replace(
                    body = json.optString("body", ""),
                    code = json.optInt("code", 200),
                    message = json.optString("message", "OK"),
                )
                "abort" -> HttpProxyDecision.Abort(
                    reason = json.optString("reason", "Aborted by ApiHttp proxy"),
                )
                else -> HttpProxyDecision.PassThrough
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            HttpProxyDecision.PassThrough
        }
    }
}
