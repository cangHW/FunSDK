package com.proxy.service.webserver.base.plugin

import com.proxy.service.webserver.base.http.WebServerHttpRequest
import com.proxy.service.webserver.base.http.WebServerHttpResponse
import com.proxy.service.webserver.base.route.FileRouteLocalPaths
import com.proxy.service.webserver.base.route.PluginWebPaths
import org.json.JSONObject

/**
 * Web 控制台插件协议，用于提供独立的服务端页面与数据能力。
 *
 * @author: cangHX
 * @date: 2026/6/15 17:27
 */
interface IWebServerPlugin {

    /**
     * 插件唯一标识
     *
     * 与 assets 目录名一致，如 apihttp、imageloader
     */
    fun getPluginId(): String

    /**
     * 插件展示名称
     *
     * 显示在 Web 控制台侧边栏及插件页标题
     */
    fun getPluginTitle(): String

    /**
     * Shell iframe 加载的插件主页 Web 路径，自定义 [getRootPageUrl] 时需同步保证 [getAssetPath] 可映射
     *
     * 默认 `/static/plugin/{pluginId}/index.html`，由 `/plugin/list` 下发给 Shell
     */
    fun getRootPageUrl(): String {
        return PluginWebPaths.defaultRootPageUrl(getPluginId())
    }

    /**
     * 插件静态资源 assets 路径（HTML、JS、CSS、图片等）
     *
     * Web 访问：`/static/plugin/{pluginId}/{relativePath}`
     */
    fun getAssetPath(relativePath: String): String {
        return FileRouteLocalPaths.resolvePluginAssetPath(getPluginId(), relativePath)
    }

    /**
     * 配置更新（通常由插件页 POST `/plugin/{id}/config` 触发）
     */
    fun onConfigChanged(config: String)

    /**
     * 返回插件展示数据（通常由插件页 GET `/plugin/{id}/request` 触发）
     */
    fun requestData(): JSONObject

    /**
     * 自定义请求与返回（路径约定见 [com.proxy.service.webserver.base.route.CustomWebPaths]）
     *
     * @param uri 完整请求路径
     * @param request 与 HTTP 引擎解耦的请求抽象
     */
    fun requestCustom(uri: String, request: WebServerHttpRequest): WebServerHttpResponse? {
        return null
    }

    /**
     * 处理 Native 侧提交的业务数据
     *
     * 由 [com.proxy.service.webserver.base.WebServerService.submitPluginData] 调用，
     * 插件执行业务逻辑后返回 [PluginDataResponse]。
     *
     * @param payload 业务数据 JSON 字符串
     */
    fun handlePluginData(payload: String): PluginDataResponse
}
