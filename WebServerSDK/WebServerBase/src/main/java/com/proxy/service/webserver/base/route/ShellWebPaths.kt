package com.proxy.service.webserver.base.route

import com.proxy.service.webserver.base.constants.Constants

/**
 * Shell 静态资源 Web 访问路径约定
 *
 * 与 [FileRouteLocalPaths] 配合使用：
 * - [FileRouteLocalPaths]：Web 相对路径 → APK assets 路径（服务端读文件）
 * - [ShellWebPaths]：Shell 页与公共静态资源的 Web URL 解析
 *
 * Web 访问约定：
 * - `/`、`/index.html` — Shell 主页
 * - `/static/res/{relativePath}` — Shell 公共静态资源
 */
object ShellWebPaths {

    private const val ROOT_PATH = "/"

    /**
     * 是否为 Shell 主页 URI
     */
    fun isShellRootPage(uri: String): Boolean {
        return uri == ROOT_PATH || uri == "/${Constants.PAGE_INDEX_HTML}"
    }

    /**
     * 构建 Shell 公共静态资源 Web URL
     *
     * @param relativePath 相对路径，如 js/shell.js、css/shell.css
     * @return 如 `/static/res/js/shell.js`
     */
    fun buildShellResUrl(relativePath: String): String {
        val cleanPath = relativePath.trim().trimStart('/')
        return "${Constants.API_STATIC_RES_PATH}$cleanPath"
    }

    /**
     * 解析 Shell 公共静态资源请求 URI
     *
     * 将 `/static/res/js/shell.js` 解析为 `js/shell.js`
     *
     * @param uri 已归一化的请求路径（不含 query）
     * @return assets 相对路径；不匹配前缀或路径为空时返回 null
     */
    fun parseShellResUri(uri: String): String? {
        if (!uri.startsWith(Constants.API_STATIC_RES_PATH)) {
            return null
        }
        val relativePath = uri.removePrefix(Constants.API_STATIC_RES_PATH).trim('/')
        return relativePath.ifBlank { null }
    }

    /**
     * 是否为合法的 Shell 公共静态资源 URI
     */
    fun isShellResUri(uri: String): Boolean {
        return parseShellResUri(uri) != null
    }
}
