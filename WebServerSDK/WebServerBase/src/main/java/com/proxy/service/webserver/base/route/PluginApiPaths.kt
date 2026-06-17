package com.proxy.service.webserver.base.route

import com.proxy.service.webserver.base.constants.Constants

/**
 * 插件通用 API URI 解析结果
 *
 * @param pluginId 插件 ID
 * @param action   接口动作，如 config、request
 */
data class PluginApiUri(
    val pluginId: String,
    val action: String,
)

/**
 * 插件通用 API Web 路径约定
 *
 * Web 访问约定：
 * - `GET /plugin/list` — 插件列表
 * - `POST /plugin/{pluginId}/config` — 更新配置
 * - `GET /plugin/{pluginId}/request` — 请求插件数据
 */
object PluginApiPaths {

    /**
     * 是否为插件列表接口 URI
     */
    fun isPluginListUri(uri: String): Boolean {
        return uri == Constants.API_PLUGIN_LIST_URL
    }

    /**
     * 构建插件通用 API Web URL
     *
     * @param pluginId 插件 ID
     * @param action   接口动作
     * @return 如 `/plugin/demo/config`
     */
    fun buildPluginApiUrl(pluginId: String, action: String): String {
        val cleanAction = action.trim().trim('/')
        return "${Constants.API_PLUGIN_ROOT_PATH}$pluginId/$cleanAction"
    }

    /**
     * 解析插件通用 API 请求 URI
     *
     * 将 `/plugin/demo/config` 解析为 [PluginApiUri]("demo", "config")
     *
     * @param uri 已归一化的请求路径（不含 query）
     * @return 解析成功返回 [PluginApiUri]，否则 null
     */
    fun parsePluginApiUri(uri: String): PluginApiUri? {
        if (!uri.startsWith(Constants.API_PLUGIN_ROOT_PATH)) {
            return null
        }
        val remainder = uri.removePrefix(Constants.API_PLUGIN_ROOT_PATH)
        val slashIndex = remainder.indexOf('/')
        if (slashIndex <= 0) {
            return null
        }
        val pluginId = remainder.substring(0, slashIndex)
        val action = remainder.substring(slashIndex + 1).trim('/')
        if (pluginId.isBlank() || action.isBlank() || action.contains('/')) {
            return null
        }
        return PluginApiUri(pluginId, action)
    }

    /**
     * 是否为指定动作的插件通用 API URI
     */
    fun isPluginApiAction(uri: String, action: String): Boolean {
        val parsed = parsePluginApiUri(uri) ?: return false
        return parsed.action == action
    }
}
