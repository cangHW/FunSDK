package com.proxy.service.debugbridge.base.plugin

/**
 * 插件页面 assets 路径与 Web 路由约定
 *
 * assets 目录约定：debug/plugins/{pluginId}/session.html
 * Web 访问约定：/plugin/{pluginId}/session/{sessionId}
 */
object PluginPagePaths {

    const val PLUGIN_ASSET_ROOT = "debug/plugins"

    const val SHELL_ASSET_ROOT = "debug"

    fun defaultSessionPageAsset(pluginId: String): String {
        return "$PLUGIN_ASSET_ROOT/$pluginId/session.html"
    }

    fun buildSessionPageUrl(pluginId: String, sessionId: String): String {
        return "/plugin/$pluginId/session/$sessionId"
    }

    fun resolvePluginAssetPath(pluginId: String, relativePath: String): String {
        val cleanPath = relativePath.trim().trimStart('/')
        return "$PLUGIN_ASSET_ROOT/$pluginId/$cleanPath"
    }
}
