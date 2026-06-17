package com.proxy.service.debugbridge.base.config.builder

import com.proxy.service.debugbridge.base.config.DebugBridgeConfig

/**
 * DebugBridge 配置构建器
 *
 * 通过 [DebugBridgeConfig.builder] 获取实例，链式设置后 [build] 生成不可变配置
 *
 * @author: cangHX
 * @date: 2026/6/15
 * @desc:
 */
interface IDebugBridgeConfigBuilder {

    /**
     * 设置 Web 服务监听端口
     *
     * @param port 有效范围 1024~65535，无效值将被忽略
     * @return 当前构建器，支持链式调用
     * */
    fun setPort(port: Int): IDebugBridgeConfigBuilder

    /**
     * 设置断点会话等待超时时间
     *
     * 超时后 [com.proxy.service.debugbridge.base.session.Decision.TimeoutPassThrough] 自动放行
     *
     * @param timeoutMs 超时毫秒数，需大于 0
     * @return 当前构建器，支持链式调用
     * */
    fun setSessionTimeoutMs(timeoutMs: Long): IDebugBridgeConfigBuilder

    /**
     * 是否在 [com.proxy.service.debugbridge.base.DebugBridgeService.init] 时自动启动 Web 服务
     *
     * @param autoStart true 则 init 后立即 start
     * @return 当前构建器，支持链式调用
     * */
    fun setAutoStart(autoStart: Boolean): IDebugBridgeConfigBuilder

    /**
     * 设置 Web 页面访问鉴权 token
     *
     * 设置后浏览器需在 URL 参数 `?token=xxx` 或 Header `X-Debug-Token` 中携带；
     * 不设置则不校验
     *
     * @param token 访问令牌
     * @return 当前构建器，支持链式调用
     * */
    fun setAccessToken(token: String): IDebugBridgeConfigBuilder

    /**
     * 构建配置对象
     *
     * @return 不可变的 [DebugBridgeConfig] 实例
     * */
    fun build(): DebugBridgeConfig
}
