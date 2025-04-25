package com.proxy.service.apm.info

import com.proxy.service.apm.info.config.ApmConfig

/**
 * ApmMonitor 稳定性监控框架入口
 *
 * @author: cangHX
 * @data: 2025/4/22 17:38
 * @desc:
 */
object CsApmMonitor {

    private var config: ApmConfig = ApmConfig.builder().build()

    /**
     * 设置配置
     * */
    fun setConfig(config: ApmConfig) {
        this.config = config
    }

    fun getConfig(): ApmConfig {
        return config
    }

}