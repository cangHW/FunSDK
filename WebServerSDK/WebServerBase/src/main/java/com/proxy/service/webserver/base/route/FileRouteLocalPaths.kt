package com.proxy.service.webserver.base.route

import com.proxy.service.webserver.base.constants.Constants

/**
 * 本地 assets 文件路径约定
 *
 * 与 Web 路径工具类配合使用，负责将浏览器请求的相对路径映射为 APK assets 内的物理路径：
 * - [ShellWebPaths] + [defaultShellRootPageAssetPath] / [resolveShellAssetPath] — Shell 页与公共资源
 * - [PluginWebPaths] + [resolvePluginAssetPath] — 插件静态资源
 *
 * assets 目录约定：
 * - Shell：`{SHELL_ASSET_ROOT}/` → 默认 `webserver/`
 * - 插件：`{PLUGIN_ASSET_ROOT}/{pluginId}/` → 默认 `webserver/plugins/{pluginId}/`
 *
 * 读取文件由 Route 层通过 [com.proxy.service.core.framework.io.file.read.CsFileReadUtils] 完成。
 */
object FileRouteLocalPaths {

    /**
     * Shell 主页 assets 路径
     *
     * 对应 Web 访问：`/`、`/index.html`（见 [ShellWebPaths.isShellRootPage]）
     *
     * @return 如 `webserver/index.html`
     */
    fun defaultShellRootPageAssetPath(): String {
        return "${Constants.SHELL_ASSET_ROOT}/${Constants.PAGE_INDEX_HTML}"
    }

    /**
     * 解析 Shell 公共静态资源 assets 路径
     *
     * 对应 Web 访问：`/static/res/{relativePath}`（见 [ShellWebPaths.parseShellResUri]）
     *
     * @param relativePath Web 相对路径，如 `js/shell.js`、`css/shell.css`
     * @return 如 `webserver/js/shell.js`
     */
    fun resolveShellAssetPath(relativePath: String): String {
        val cleanPath = relativePath.trim().trimStart('/')
        return "${Constants.SHELL_ASSET_ROOT}/$cleanPath"
    }

    /**
     * 解析插件静态资源 assets 路径
     *
     * 对应 Web 访问：`/static/plugin/{pluginId}/{relativePath}`
     * （见 [PluginWebPaths.parsePluginStaticUri]）
     *
     * @param pluginId     插件 ID
     * @param relativePath Web 相对路径，如 `index.html`、`session.html`、`js/app.js`
     * @return 如 `webserver/plugins/demo/index.html`
     */
    fun resolvePluginAssetPath(pluginId: String, relativePath: String): String {
        val cleanPath = relativePath.trim().trimStart('/')
        return "${Constants.PLUGIN_ASSET_ROOT}/$pluginId/$cleanPath"
    }
}
