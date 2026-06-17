package com.proxy.service.debugbridge.base

import com.proxy.service.base.BaseService
import com.proxy.service.debugbridge.base.config.DebugBridgeConfig
import com.proxy.service.debugbridge.base.network.AccessInfo
import com.proxy.service.debugbridge.base.plugin.IDebugPlugin
import com.proxy.service.debugbridge.base.session.Decision

/**
 * 通用调试 Web 桥接服务
 *
 * 负责在 App 内启动嵌入式 Web 服务，供电脑浏览器访问调试页面；
 * 各 SDK 通过 [IDebugPlugin] 注册插件，通过 [submitSession] 提交断点会话。
 *
 * @author: cangHX
 * @date: 2026/6/15
 * @desc:
 */
interface DebugBridgeService : BaseService {

    /**
     * 初始化调试服务配置
     *
     * @param config 端口、超时、鉴权等配置，见 [DebugBridgeConfig]
     * */
    fun init(config: DebugBridgeConfig)

    /**
     * 启动嵌入式 Web 服务
     *
     * @return 访问信息（局域网 URL 等），非 debug 或启动失败时返回 null
     * */
    fun start(): AccessInfo?

    /**
     * 停止嵌入式 Web 服务并释放端口
     * */
    fun stop()

    /**
     * Web 服务是否正在运行
     * */
    fun isRunning(): Boolean

    /**
     * 获取当前 Web 服务访问信息
     *
     * @return 未启动时返回 null
     * */
    fun getAccessInfo(): AccessInfo?

    /**
     * 注册调试插件
     *
     * 插件负责提供独立 HTML 页面路径及会话数据展示逻辑，见 [IDebugPlugin]
     *
     * @param plugin 各 SDK Debug 模块实现的插件
     * */
    fun registerPlugin(plugin: IDebugPlugin)

    /**
     * 提交调试会话并阻塞等待人工决策
     *
     * 调用方线程会阻塞，直到浏览器操作（放行/替换/中止）或超时。
     * 超时后返回 [Decision.TimeoutPassThrough]。
     *
     * @param pluginId 插件标识，需与 [IDebugPlugin.id] 一致
     * @param tag 会话标题，展示在调试列表页
     * @param payload 业务数据 JSON 字符串，由插件页面解析展示
     * @param metadata 附加元信息，如 url、method、耗时等
     * @return 人工决策结果
     * */
    fun submitSession(
        pluginId: String,
        tag: String,
        payload: String,
        metadata: Map<String, String> = emptyMap()
    ): Decision
}
