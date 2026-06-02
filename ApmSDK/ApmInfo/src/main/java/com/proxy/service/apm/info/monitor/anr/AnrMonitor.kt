package com.proxy.service.apm.info.monitor.anr

import android.app.Application
import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.config.controller.common.CommonConfig
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.anr.jni.AnrBridge
import com.proxy.service.apm.info.monitor.anr.report.AnrReport
import com.proxy.service.apm.info.monitor.anr.reporter.AnrFileReporter
import com.proxy.service.apm.info.monitor.anr.watchdog.AnrWatchdog
import com.proxy.service.apm.info.monitor.base.AbstractMonitor
import com.proxy.service.apm.info.report.composite.CompositeReporter
import com.proxy.service.apm.info.report.impl.CallbackReporter
import com.proxy.service.apm.info.sampler.composite.CompositeSampler
import com.proxy.service.apm.info.sampler.impl.app.AppSampler
import com.proxy.service.apm.info.sampler.impl.cpu.CpuSampler
import com.proxy.service.apm.info.sampler.impl.device.DeviceSampler
import com.proxy.service.apm.info.sampler.impl.logcat.LogcatSampler
import com.proxy.service.apm.info.sampler.impl.mem.MemSampler
import com.proxy.service.apm.info.sampler.impl.stack.AllThreadStackSampler
import com.proxy.service.apm.info.sampler.impl.stack.MainThreadStackSampler
import com.proxy.service.apm.info.utils.FileUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.concurrent.atomic.AtomicBoolean

class AnrMonitor private constructor() : AbstractMonitor<CommonConfig>() {

    companion object {
        private const val TAG: String = "${Constants.TAG}Anr"
        private const val CHECK_INTERVAL_MS = 500L

        private val mInstance by lazy { AnrMonitor() }

        fun getInstance(): AnrMonitor {
            return mInstance
        }
    }

    private var watchdog: AnrWatchdog? = null
    private var sampler: CompositeSampler? = null
    private var reporter: CompositeReporter<AnrReport>? = null

    @Volatile
    private var lastReportWallMs = 0L

    private val handling = AtomicBoolean(false)

    override fun start(application: Application, apmConfig: ApmConfig, config: CommonConfig) {
        val dir = getLogFileDir(application)

        sampler = CompositeSampler(
            MainThreadStackSampler.create(Long.MAX_VALUE),
            AppSampler.create(),
            DeviceSampler.create(),
            CpuSampler.create(Long.MAX_VALUE),
            MemSampler.create(),
            AllThreadStackSampler.create(),
            LogcatSampler.create()
        )

        reporter = CompositeReporter(
            AnrFileReporter(dir),
            CallbackReporter(apmConfig.getAnrReporter())
        )

        val ret = AnrBridge.nativeInit()
        if (ret != 0) {
            CsLogger.tag(TAG).e("ANR init failed, code=$ret")
            return
        }

        watchdog = AnrWatchdog(CHECK_INTERVAL_MS) { signalCount ->
            handleAnrDetected(signalCount)
        }
        watchdog?.isDaemon = true
        watchdog?.start()
    }

    override fun stop() {
        super.stop()
        watchdog?.stopWatching()
        watchdog = null
        AnrBridge.nativeDeinit()
    }

    override fun getLogFileDir(application: Application): String {
        return FileUtils.getDefaultDir(application, "anr/")
    }

    private fun handleAnrDetected(signalCount: Int) {
        if (signalCount <= 0) {
            return
        }

        val now = System.currentTimeMillis()
        if (now - lastReportWallMs < Constants.MONITOR_ANR_REPORT_COOLDOWN_MS) {
            CsLogger.tag(TAG).d("ANR suppressed (cooldown), signalCount=$signalCount")
            return
        }

        if (!handling.compareAndSet(false, true)) {
            CsLogger.tag(TAG).d("ANR suppressed (handling), signalCount=$signalCount")
            return
        }

        lastReportWallMs = now
        publishAsync(signalCount)
    }

    private fun publishAsync(signalCount: Int) {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                try {
                    val time = System.currentTimeMillis()
                    val samplerData = sampler?.sampleNow() ?: emptyList()
                    val report = AnrReport(samplerData = samplerData)
                    reporter?.publish(time, report)
                    if (signalCount > 1) {
                        CsLogger.tag(TAG).w(
                            "ANR report published, coalesced ${signalCount - 1} extra SIGQUIT(s)"
                        )
                    }
                } catch (throwable: Throwable) {
                    CsLogger.tag(TAG).e(throwable)
                } finally {
                    handling.set(false)
                }
                return ""
            }
        })?.start()
    }
}
