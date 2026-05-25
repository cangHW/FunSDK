package com.proxy.service.apm.info

import com.proxy.service.apm.info.config.ApmConfig

/**
 * Apm 稳定性监控框架全局入口。
 *
 * 须在 [com.proxy.service.core.CsCore.init] 之前调用 [setConfig]，
 * 以便 [ApmMonitorConfig] 自动初始化时能读到业务配置（含主线程慢分发 [MainThreadLagConfig]）。
 *
 * @author: cangHX
 * @date: 2025/4/22 17:38
 */
object CsApmMonitor {

    private var config: ApmConfig = ApmConfig.builder().build()

    /**
     * 注入 Apm 配置（Crash、主线程慢分发、UI 卡顿等）。
     */
    fun setConfig(config: ApmConfig) {
        this.config = config
    }

    fun getConfig(): ApmConfig {
        return config
    }

}