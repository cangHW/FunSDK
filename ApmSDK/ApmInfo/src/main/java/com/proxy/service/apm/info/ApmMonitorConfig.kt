package com.proxy.service.apm.info

import android.annotation.SuppressLint
import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.apm.info.monitor.crash.CrashMonitor
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.MainThreadLagMonitor
import com.proxy.service.apm.info.monitor.performance.lag.ui.UiLagMonitor
import com.proxy.service.core.application.base.CsBaseConfig

/**
 * APM 模块 Cloud 自动配置入口。
 *
 * 在 Application 初始化时拉起各监控器，优先级 -999 尽早执行。
 *
 * @author: cangHX
 * @date: 2025/4/12 16:31
 */
@CloudApiService(serviceTag = "cs_config/apm")
class ApmMonitorConfig : CsBaseConfig() {

    override fun moduleType(): ModuleType {
        return ModuleType.SDK
    }

    @SuppressLint("Range")
    override fun priority(): Int {
        return -985
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        val config = CsApmMonitor.getConfig()

        val mtlConfig = config.getMainThreadLagMonitorConfig()
        MainThreadLagMonitor.getInstance().init(application, config, mtlConfig)

        val uilConfig = config.getUiLagMonitorConfig()
        UiLagMonitor.getInstance().init(application, config, uilConfig)

        val cConfig = config.getJavaCrashMonitorConfig()
        CrashMonitor.getInstance().init(application, config, cConfig)
    }
}