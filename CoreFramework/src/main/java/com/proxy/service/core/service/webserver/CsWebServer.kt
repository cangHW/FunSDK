package com.proxy.service.core.service.webserver

import com.proxy.service.api.CloudSystem
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webserver.base.WebServerService
import com.proxy.service.webserver.base.config.WebServerConfig
import com.proxy.service.webserver.base.plugin.IWebServerPlugin
import com.proxy.service.webserver.base.plugin.PluginDataResponse
import com.proxy.service.webserver.base.server.ServerInfo

/**
 * web 服务器框架入口
 *
 * @author: cangHX
 * @date: 2026/6/18 17:43
 * @desc:
 */
object CsWebServer {

    private const val TAG = "${CoreConfig.TAG}WebServer"

    private var service: WebServerService? = null

    private fun getService(): WebServerService? {
        if (service == null) {
            service = CloudSystem.getService(WebServerService::class.java)
        }
        if (service == null) {
            CsLogger.tag(TAG)
                .e("Please check to see if it is referenced. <io.github.canghw:Service-WebServer:xxx>")
        }
        return service
    }

    private var config = WebServerConfig.builder().build()

    /**
     * 设置嵌入式 Web 控制台配置
     */
    fun setConfig(config: WebServerConfig) {
        this.config = config
    }

    /**
     * 启动嵌入式 Web 服务
     *
     * @param pluginId 插件ID
     */
    fun start(pluginId: String): ServerInfo? {
        val service = getService()
        service?.init(config)
        return service?.start(pluginId)
    }

    /**
     * 停止嵌入式 Web 服务并释放端口
     *
     * @param pluginId 插件ID，与 [start] 传入值一致
     */
    fun stop(pluginId: String) {
        getService()?.stop(pluginId)
    }

    /**
     * Web 服务是否正在运行
     */
    fun isRunning(): Boolean {
        return getService()?.isRunning() ?: false
    }

    /**
     * 获取服务访问信息（端口、LAN URL 等）
     */
    fun getServerInfo(): ServerInfo? {
        return service?.getServerInfo()
    }

    /**
     * 注册 Web 控制台插件
     *
     * 插件负责提供独立 HTML 页面及数据接口
     *
     * @param plugin 各业务 SDK 模块实现的插件
     * @return 注册成功返回 true；同 pluginId 已存在时返回 false
     */
    fun registerPlugin(plugin: IWebServerPlugin): Boolean {
        return getService()?.registerPlugin(plugin) ?: false
    }

    /**
     * 注销 Web 控制台插件
     *
     * @param pluginId 插件标识，需与 [IWebServerPlugin.getPluginId] 一致
     * @return 是否注销成功
     */
    fun unregisterPlugin(pluginId: String): Boolean {
        return getService()?.unregisterPlugin(pluginId) ?: false
    }

    /**
     * 向插件提交业务数据并由插件处理
     *
     * Native 侧将 payload 交给对应插件的 [IWebServerPlugin.handlePluginData]，
     * 由插件执行业务逻辑后返回 [PluginDataResponse]。
     *
     * @param pluginId 插件标识，需与 [IWebServerPlugin.getPluginId] 一致
     * @param payload 业务数据 JSON 字符串
     */
    fun submitPluginData(
        pluginId: String,
        payload: String,
    ): PluginDataResponse {
        val result = getService()?.submitPluginData(pluginId, payload)
        return result ?: PluginDataResponse.FAILED("referenced error.")
    }

}