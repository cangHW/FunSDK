package com.proxy.service.apm.info

import android.annotation.SuppressLint
import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.apm.info.cache.CheckManager
import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.monitor.anr.AnrMonitor
import com.proxy.service.apm.info.monitor.crash.java_crash.JavaCrashMonitor
import com.proxy.service.apm.info.monitor.crash.native_crash.NativeCrashMonitor
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.MainThreadLagMonitor
import com.proxy.service.apm.info.monitor.performance.lag.ui.UiLagMonitor
import com.proxy.service.apm.info.monitor.performance.leak.MemoryLeakMonitor
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.constants.CoreConfig

/**
 * APM 模块 Cloud 自动配置入口。
 *
 * @author: cangHX
 * @date: 2025/4/12 16:31
 */
@CloudApiService(serviceTag = "${CoreConfig.SERVICE_TAG_APP_PREFIX}/apm")
class ApmMonitorApplication : CsBaseApplication() {

    override fun moduleType(): ModuleType {
        return ModuleType.SDK
    }

    @SuppressLint("Range")
    override fun priority(): Int {
        return CoreConfig.PRIORITY_APP_APM
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        val config = CsApmMonitor.getConfig()

        CheckManager.checkMonitorFileCache(application)

        initCrashMonitor(application, config)
        initAnrMonitor(application, config)

        initPerformanceMonitor(application, config)
    }

    private fun initCrashMonitor(application: Application, config: ApmConfig) {
        val cConfig = config.getJavaCrashMonitorConfig()
        JavaCrashMonitor.getInstance().init(application, config, cConfig)

        val ncConfig = config.getNativeCrashMonitorConfig()
        NativeCrashMonitor.getInstance().init(application, config, ncConfig)
    }

    private fun initPerformanceMonitor(application: Application, config: ApmConfig) {
        val mtlConfig = config.getMainThreadLagMonitorConfig()
        MainThreadLagMonitor.getInstance().init(application, config, mtlConfig)

        val uilConfig = config.getUiLagMonitorConfig()
        UiLagMonitor.getInstance().init(application, config, uilConfig)

        val mlConfig = config.getMemoryLeakMonitorConfig()
        MemoryLeakMonitor.getInstance().init(application, config, mlConfig)
    }

    private fun initAnrMonitor(application: Application, config: ApmConfig) {
        val anrConfig = config.getAnrMonitorConfig()
        AnrMonitor.getInstance().init(application, config, anrConfig)
    }
}