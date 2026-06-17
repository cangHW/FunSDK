package com.proxy.service.webserver.base.route

import com.proxy.service.webserver.base.constants.Constants

/**
 * 插件自定义 API URI 解析结果
 *
 * @param pluginId 插件 ID
 * @param subPath  `/custom/{pluginId}/` 之后的相对路径，可为空
 */
data class CustomUri(
    val pluginId: String,
    val subPath: String,
)

/**
 * 插件自定义 API Web 路径约定
 *
 * Web 访问约定：`/custom/{pluginId}/{subPath}`
 * 例如：
 * - `/custom/apihttp/session/list`
 * - `/custom/apihttp/session/{sessionId}/decide`
 *
 * 完整 URI 由插件在 [com.proxy.service.webserver.base.plugin.IWebServerPlugin.requestCustom] 内处理；
 * 请求通过 [com.proxy.service.webserver.base.http.WebServerHttpRequest] 传入，与 NanoHTTPD 解耦。
 */
object CustomWebPaths {

    /**
     * 构建插件自定义 API Web URL
     *
     * @param pluginId 插件 ID
     * @param subPath  自定义子路径，如 session/list
     * @return 如 `/custom/apihttp/session/list`
     */
    fun buildCustomUrl(pluginId: String, subPath: String): String {
        val cleanSubPath = subPath.trim().trimStart('/')
        return if (cleanSubPath.isBlank()) {
            "${Constants.API_CUSTOM_ROOT_PATH}$pluginId"
        } else {
            "${Constants.API_CUSTOM_ROOT_PATH}$pluginId/$cleanSubPath"
        }
    }

    /**
     * 解析插件自定义 API 请求 URI
     *
     * @param uri 已归一化的请求路径（不含 query）
     * @return 解析成功返回 [CustomUri]，否则 null
     */
    fun parseCustomUri(uri: String): CustomUri? {
        if (!uri.startsWith(Constants.API_CUSTOM_ROOT_PATH)) {
            return null
        }
        val remainder = uri.removePrefix(Constants.API_CUSTOM_ROOT_PATH).trim('/')
        if (remainder.isBlank()) {
            return null
        }
        val slashIndex = remainder.indexOf('/')
        if (slashIndex < 0) {
            return CustomUri(pluginId = remainder, subPath = "")
        }
        val pluginId = remainder.substring(0, slashIndex)
        val subPath = remainder.substring(slashIndex + 1).trim('/')
        if (pluginId.isBlank()) {
            return null
        }
        return CustomUri(pluginId, subPath)
    }

    /**
     * 是否为合法的插件自定义 API URI
     */
    fun isCustomUri(uri: String): Boolean {
        return parseCustomUri(uri) != null
    }
}
