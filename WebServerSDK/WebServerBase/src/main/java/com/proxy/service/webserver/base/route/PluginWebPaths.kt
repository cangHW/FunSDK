package com.proxy.service.webserver.base.route

import com.proxy.service.webserver.base.constants.Constants
import com.proxy.service.webserver.base.plugin.IWebServerPlugin

/**
 * 插件静态 URI 解析结果
 *
 * @param pluginId      插件 ID
 * @param relativePath  assets 相对路径，如 index.html、js/app.js
 */
data class PluginStaticUri(
    val pluginId: String,
    val relativePath: String,
)

/**
 * 插件静态资源 Web 访问路径约定
 *
 * 与 [FileRouteLocalPaths] 配合使用：
 * - [FileRouteLocalPaths]：Web 相对路径 → APK assets 路径（服务端读文件）
 * - [PluginWebPaths]：插件 ID + 相对路径 → 浏览器可访问的 Web URL（Shell / 前端请求）
 *
 * Web 访问约定：`/static/plugin/{pluginId}/{relativePath}`
 * 例如：
 * - `/static/plugin/demo/index.html` — 插件主页
 * - `/static/plugin/demo/session.html` — 子页面
 * - `/static/plugin/demo/js/app.js` — 静态资源
 *
 * Shell 通过 `/plugin/list` 下发的 [IWebServerPlugin.getRootPageUrl]
 * 默认由 [defaultRootPageUrl] 生成。
 */
object PluginWebPaths {

    /**
     * 构建插件静态资源的 Web 访问 URL
     *
     * @param pluginId      插件 ID
     * @param relativePath  相对路径，如 index.html、session.html、js/app.js
     * @return 如 `/static/plugin/demo/index.html`
     */
    fun buildPluginAssetUrl(pluginId: String, relativePath: String): String {
        val cleanPath = relativePath.trim().trimStart('/')
        return "${Constants.API_STATIC_PLUGIN_PATH}$pluginId/$cleanPath"
    }

    /**
     * 插件主页默认 Web URL
     *
     * Shell iframe 加载地址，对应 assets 文件 [Constants.PAGE_INDEX_HTML]
     *
     * @param pluginId 插件 ID
     * @return 如 `/static/plugin/demo/index.html`
     */
    fun defaultRootPageUrl(pluginId: String): String {
        return buildPluginAssetUrl(pluginId, Constants.PAGE_INDEX_HTML)
    }

    /**
     * 解析插件静态资源请求 URI
     *
     * 将 `/static/plugin/demo/js/app.js` 解析为
     * [PluginStaticUri]("demo", "js/app.js")
     *
     * @param uri 已归一化的请求路径（不含 query）
     * @return 解析成功返回 [PluginStaticUri]，否则 null
     */
    fun parsePluginStaticUri(uri: String): PluginStaticUri? {
        if (!uri.startsWith(Constants.API_STATIC_PLUGIN_PATH)) {
            return null
        }
        val remainder = uri.removePrefix(Constants.API_STATIC_PLUGIN_PATH)
        val slashIndex = remainder.indexOf('/')
        if (slashIndex <= 0) {
            return null
        }
        val pluginId = remainder.substring(0, slashIndex)
        val relativePath = remainder.substring(slashIndex + 1).trim('/')
        if (pluginId.isBlank() || relativePath.isBlank()) {
            return null
        }
        return PluginStaticUri(pluginId, relativePath)
    }

    /**
     * 是否为合法的插件静态资源 URI
     */
    fun isPluginStaticUri(uri: String): Boolean {
        return parsePluginStaticUri(uri) != null
    }
}
