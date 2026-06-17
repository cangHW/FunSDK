package com.proxy.service.debugbridge.base.plugin

import com.proxy.service.debugbridge.base.session.DebugSession
import com.proxy.service.debugbridge.base.session.Decision

/**
 * 调试插件协议
 *
 * 各 SDK 的 Debug 模块实现此接口并注册到 [com.proxy.service.debugbridge.base.DebugBridgeService]，
 * 用于提供独立 HTML 详情页及会话数据的展示与决策落地。
 *
 * assets 约定：在模块内放置 `debug/plugins/{id}/session.html`
 * Web 访问：`/plugin/{id}/session/{sessionId}`
 *
 * @author: cangHX
 * @date: 2026/6/15
 * @desc:
 */
interface IDebugPlugin {

    /**
     * 插件唯一标识
     *
     * 与 assets 目录名一致，如 apihttp、imageloader
     * */
    val id: String

    /**
     * 插件展示名称
     *
     * 显示在调试列表页及详情页标题
     * */
    val name: String

    /**
     * 前端渲染类型
     *
     * 可选，用于列表展示或扩展区分，默认与 [id] 相同
     * */
    val renderType: String
        get() = id

    /**
     * 插件详情页 assets 路径
     *
     * 默认 `debug/plugins/{id}/session.html`，各 Debug 模块在自身 assets 下放置独立 HTML；
     * 若路径不同可 override
     * */
    val sessionPageAssetPath: String
        get() = PluginPagePaths.defaultSessionPageAsset(id)

    /**
     * 构建插件详情页 Web 访问路径
     *
     * @param sessionId 调试会话 ID
     * @return 如 `/plugin/apihttp/session/abc123`
     * */
    fun buildSessionPageUrl(sessionId: String): String {
        return PluginPagePaths.buildSessionPageUrl(id, sessionId)
    }

    /**
     * 将业务 payload 转为可展示的 JSON 字符串
     *
     * 供 `/api/session/{id}/detail` 接口返回，插件 HTML 页面解析 [display] 字段渲染 UI
     *
     * @param session 当前调试会话
     * @return 格式化后的 JSON 字符串
     * */
    fun toDisplayJson(session: DebugSession): String

    /**
     * 应用人工决策（可选）
     *
     * 浏览器提交决策后、会话 resolve 之前回调，用于插件侧附加处理
     *
     * @param session 当前调试会话
     * @param decision 人工决策结果
     * @return 业务侧需要的附加结果，默认 null
     * */
    fun applyDecision(session: DebugSession, decision: Decision): Any? = null
}
