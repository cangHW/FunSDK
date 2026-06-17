package com.proxy.service.webserver.base

import com.proxy.service.base.BaseService
import com.proxy.service.webserver.base.config.WebServerConfig
import com.proxy.service.webserver.base.plugin.IWebServerPlugin
import com.proxy.service.webserver.base.plugin.PluginDataResponse
import com.proxy.service.webserver.base.server.ServerInfo

/**
 * 嵌入式 Web 控制台服务
 *
 * @author: cangHX
 * @date: 2026/6/15 17:12
 */
interface WebServerService : BaseService {

    /**
     * 初始化嵌入式 Web 控制台配置
     */
    fun init(config: WebServerConfig)

    /**
     * 启动嵌入式 Web 服务
     *
     * @param pluginId 引用方标识，用于引用计数；多个调用方可共享同一服务实例
     */
    fun start(pluginId: String): ServerInfo?

    /**
     * 停止嵌入式 Web 服务并释放端口
     *
     * @param pluginId 引用方标识，与 [start] 传入值一致
     */
    fun stop(pluginId: String)

    /**
     * Web 服务是否正在运行
     */
    fun isRunning(): Boolean

    /**
     * 获取服务访问信息（端口、LAN URL 等）
     */
    fun getServerInfo(): ServerInfo?

    /**
     * 注册 Web 控制台插件
     *
     * 插件负责提供独立 HTML 页面及数据接口
     *
     * @param plugin 各业务 SDK 模块实现的插件
     * @return 注册成功返回 true；同 pluginId 已存在时返回 false
     */
    fun registerPlugin(plugin: IWebServerPlugin): Boolean

    /**
     * 注销 Web 控制台插件
     *
     * @param pluginId 插件标识，需与 [IWebServerPlugin.getPluginId] 一致
     * @return 是否注销成功
     */
    fun unregisterPlugin(pluginId: String): Boolean

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
    ): PluginDataResponse
}
