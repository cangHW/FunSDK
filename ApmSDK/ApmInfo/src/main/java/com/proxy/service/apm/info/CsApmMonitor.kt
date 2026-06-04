package com.proxy.service.apm.info

import com.proxy.service.apm.info.cache.CheckManager
import com.proxy.service.apm.info.cache.ExceptionHandler
import com.proxy.service.apm.info.config.ApmConfig

/**
 * Apm 稳定性监控框架全局入口。
 *
 * @author: cangHX
 * @date: 2025/4/22 17:38
 */
object CsApmMonitor {

    private var config: ApmConfig = ApmConfig.builder().build()

    private var exceptionHandler: ExceptionHandler? = null

    /**
     * 注入 Apm 配置（Crash、主线程慢分发、UI 卡顿等）。
     */
    fun setConfig(config: ApmConfig) {
        this.config = config
    }

    fun getConfig(): ApmConfig {
        return config
    }

    /**
     * 设置异常文件整体回调
     * */
    fun setExceptionHandler(handler: ExceptionHandler) {
        this.exceptionHandler = handler

        CheckManager.checkMonitorFileCache(null)
    }

    fun getExceptionHandler(): ExceptionHandler? {
        val handler = exceptionHandler
        exceptionHandler = null
        return handler
    }

}