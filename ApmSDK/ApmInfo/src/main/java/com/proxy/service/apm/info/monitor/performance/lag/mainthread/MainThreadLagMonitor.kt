package com.proxy.service.apm.info.monitor.performance.lag.mainthread

import android.app.Application
import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.config.controller.performance.mainthread.MainThreadLagConfig
import com.proxy.service.apm.info.monitor.base.AbstractMonitor
import com.proxy.service.apm.info.report.composite.CompositeReporter
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.engine.DispatchWatchEngine
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.hook.MainLooperHookInstaller
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.reporter.MainThreadLagFileReporter
import com.proxy.service.apm.info.report.impl.CallbackReporter
import com.proxy.service.apm.info.sampler.composite.CompositeSampler
import com.proxy.service.apm.info.sampler.impl.app.AppSampler
import com.proxy.service.apm.info.sampler.impl.cpu.CpuSampler
import com.proxy.service.apm.info.sampler.impl.device.DeviceSampler
import com.proxy.service.apm.info.sampler.impl.stack.MainThreadStackSampler
import com.proxy.service.apm.info.utils.FileUtils

/**
 * 主线程慢分发（Looper Message 分发超时）监测器。
 *
 * 通过 [MainLooperHookInstaller] 挂钩主 Looper，由 [DispatchWatchEngine] 判定单次 dispatch 是否超过阈值，
 * 并采集堆栈与 CPU 采样；报告落盘至 `performance/main_thread_lag/`。
 *
 * @author: cangHX
 * @date: 2026/5/22 14:12
 */
class MainThreadLagMonitor private constructor() : AbstractMonitor<MainThreadLagConfig>() {

    companion object {
        private val mInstance by lazy { MainThreadLagMonitor() }

        fun getInstance(): MainThreadLagMonitor = mInstance
    }

    private var hookInstaller: MainLooperHookInstaller? = null

    override fun start(
        application: Application,
        apmConfig: ApmConfig,
        config: MainThreadLagConfig
    ) {
        val blockThresholdTime = config.getBlockThresholdTime()
        val intervalMs = blockThresholdTime / 2
        val sampler = CompositeSampler(
            AppSampler.create(),
            DeviceSampler.create(),
            MainThreadStackSampler.create(intervalMs),
            CpuSampler.create(intervalMs)
        )
        val reporter = CompositeReporter(
            MainThreadLagFileReporter(getLogFileDir(application)),
            CallbackReporter(apmConfig.getMainThreadLagReporter())
        )
        val engine = DispatchWatchEngine(blockThresholdTime, sampler, reporter)

        hookInstaller = MainLooperHookInstaller(engine)
        hookInstaller?.install()
    }

    override fun stop() {
        super.stop()

        hookInstaller?.uninstall()
        hookInstaller = null
    }

    override fun getLogFileDir(application: Application): String {
        return FileUtils.getDefaultDir(application, "performance/lag/main_thread/")
    }

}