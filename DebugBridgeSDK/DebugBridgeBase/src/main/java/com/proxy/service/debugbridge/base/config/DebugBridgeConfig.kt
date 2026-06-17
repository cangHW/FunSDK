package com.proxy.service.debugbridge.base.config

import com.proxy.service.debugbridge.base.config.builder.IDebugBridgeConfigBuilder
import com.proxy.service.debugbridge.base.config.builder.IDebugBridgeConfigBuilderGet
import com.proxy.service.debugbridge.base.constants.DebugBridgeConstants

/**
 * DebugBridge 服务配置
 *
 * 通过 [builder] 创建，构建后不可变；实现 [IDebugBridgeConfigBuilderGet] 供运行时读取
 *
 * @author: cangHX
 * @date: 2026/6/15
 * @desc:
 */
sealed interface DebugBridgeConfig : IDebugBridgeConfigBuilderGet {

    companion object {

        /**
         * 创建配置构建器
         * */
        fun builder(): IDebugBridgeConfigBuilder {
            return Builder()
        }
    }

    private class Builder : IDebugBridgeConfigBuilder, DebugBridgeConfig {

        private var port: Int = DebugBridgeConstants.DEFAULT_PORT
        private var sessionTimeoutMs: Long = DebugBridgeConstants.DEFAULT_SESSION_TIMEOUT_MS
        private var autoStart: Boolean = false
        private var accessToken: String? = null

        override fun setPort(port: Int): Builder {
            if (port in 1024..65535) {
                this.port = port
            }
            return this
        }

        override fun setSessionTimeoutMs(timeoutMs: Long): Builder {
            if (timeoutMs > 0) {
                this.sessionTimeoutMs = timeoutMs
            }
            return this
        }

        override fun setAutoStart(autoStart: Boolean): Builder {
            this.autoStart = autoStart
            return this
        }

        override fun setAccessToken(token: String): Builder {
            this.accessToken = token
            return this
        }

        override fun build(): DebugBridgeConfig {
            return this
        }

        override fun getPort(): Int = port

        override fun getSessionTimeoutMs(): Long = sessionTimeoutMs

        override fun isAutoStart(): Boolean = autoStart

        override fun getAccessToken(): String? = accessToken
    }
}
