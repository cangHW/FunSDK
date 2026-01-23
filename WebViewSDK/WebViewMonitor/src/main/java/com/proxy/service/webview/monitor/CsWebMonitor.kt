package com.proxy.service.webview.monitor

import com.proxy.service.webview.monitor.config.MonitorConfig

/**
 * @author: cangHX
 * @data: 2026/1/23 13:37
 * @desc: web 容器监控框架入口
 */
object CsWebMonitor {

    private var config: MonitorConfig = MonitorConfig.builder().build()

    /**
     * 设置配置
     * */
    fun setConfig(config: MonitorConfig) {
        this.config = config
    }

    fun getMonitorConfig(): MonitorConfig {
        return config
    }

}