package com.proxy.service.debugbridge.base.config.builder

import com.proxy.service.debugbridge.base.config.DebugBridgeConfig
import com.proxy.service.debugbridge.base.constants.DebugBridgeConstants

/**
 * DebugBridge 配置只读访问接口
 *
 * 由 [DebugBridgeConfig] 实现，供 Info 模块读取已构建的配置项
 *
 * @author: cangHX
 * @date: 2026/6/15
 * @desc:
 */
interface IDebugBridgeConfigBuilderGet {

    /**
     * 获取 Web 服务监听端口
     *
     * @return 默认 [DebugBridgeConstants.DEFAULT_PORT]
     * */
    fun getPort(): Int

    /**
     * 获取断点会话等待超时时间（毫秒）
     *
     * @return 默认 [DebugBridgeConstants.DEFAULT_SESSION_TIMEOUT_MS]
     * */
    fun getSessionTimeoutMs(): Long

    /**
     * init 时是否自动启动 Web 服务
     * */
    fun isAutoStart(): Boolean

    /**
     * 获取 Web 页面访问鉴权 token
     *
     * @return null 表示不校验
     * */
    fun getAccessToken(): String?
}
