package com.proxy.service.apm.info.monitor.performance.lag.ui

import android.app.Application
import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.config.controller.performance.uilag.UiLagConfig
import com.proxy.service.apm.info.monitor.base.AbstractMonitor
import com.proxy.service.apm.info.monitor.performance.lag.ui.engine.FrameWatchEngine
import com.proxy.service.apm.info.monitor.performance.lag.ui.hook.FrameMetricsHookInstaller
import com.proxy.service.apm.info.monitor.performance.lag.ui.reporter.UiLagNotifyReporter
import com.proxy.service.apm.info.report.composite.CompositeReporter
import com.proxy.service.apm.info.report.impl.CallbackReporter
import com.proxy.service.apm.info.utils.FileUtils

/**
 * UI 渲染卡顿监控（FrameMetrics 单帧回调 + Log / Debug Toast 通知）。
 */
class UiLagMonitor private constructor() : AbstractMonitor<UiLagConfig>() {

    companion object {
        private val mInstance by lazy { UiLagMonitor() }

        fun getInstance(): UiLagMonitor = mInstance
    }

    private var installer: FrameMetricsHookInstaller? = null

    override fun start(application: Application, apmConfig: ApmConfig, config: UiLagConfig) {
        val reporter = CompositeReporter(
            UiLagNotifyReporter(config),
            CallbackReporter(apmConfig.getUiLagReporter())
        )
        val engine = FrameWatchEngine(reporter)
        installer = FrameMetricsHookInstaller(engine)
        installer?.install()
    }

    override fun stop() {
        super.stop()
        installer?.uninstall()
        installer = null
    }

    override fun getLogFileDir(application: Application): String? {
        return null
    }
}
