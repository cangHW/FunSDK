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
import com.proxy.service.apm.info.sampler.impl.mem.MemSampler
import com.proxy.service.apm.info.sampler.impl.stack.AllThreadStackSampler
import com.proxy.service.apm.info.sampler.impl.stack.MainThreadStackSampler
import com.proxy.service.apm.info.utils.FileUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import java.io.File

class AnrMonitor private constructor() : AbstractMonitor<CommonConfig>() {

    companion object {
        private const val TAG: String = "${Constants.TAG}Anr"
        private const val MARKER_FILE_NAME = "anr_marker.tmp"
        private const val CHECK_INTERVAL_MS = 500L

        private val mInstance by lazy { AnrMonitor() }

        fun getInstance(): AnrMonitor {
            return mInstance
        }
    }

    private var watchdog: AnrWatchdog? = null
    private var sampler: CompositeSampler? = null
    private var reporter: CompositeReporter<AnrReport>? = null

    override fun start(application: Application, apmConfig: ApmConfig, config: CommonConfig) {
        val dir = getLogFileDir(application)
        val tempDir = getTempDir(application)

        checkPendingAnr(tempDir)

        sampler = CompositeSampler(
            MainThreadStackSampler.create(Long.MAX_VALUE),
            AppSampler.create(),
            DeviceSampler.create(),
            CpuSampler.create(Long.MAX_VALUE),
            MemSampler.create(),
            AllThreadStackSampler.create(),
        )

        reporter = CompositeReporter(
            AnrFileReporter(dir),
            CallbackReporter(apmConfig.getAnrReporter())
        )

        val ret = AnrBridge.nativeInit(tempDir)
        if (ret != 0) {
            CsLogger.tag(TAG).e("ANR init failed, code=$ret")
            return
        }

        watchdog = AnrWatchdog(CHECK_INTERVAL_MS) {
            handleAnrDetected()
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

    private fun getTempDir(application: Application): String {
        return FileUtils.getDefaultDir(application, "temp/anr/")
    }

    private fun handleAnrDetected() {
        try {
            val time = System.currentTimeMillis()
            val samplerData = sampler?.sampleNow() ?: emptyList()

            val report = AnrReport(
                time = time,
                samplerData = samplerData
            )

            reporter?.publish(time, report)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }

    private fun checkPendingAnr(tempDir: String) {
        val marker = File(tempDir, MARKER_FILE_NAME)
        if (!marker.exists() || marker.length() == 0L) {
            return
        }

        // 有 marker 说明上次发生了 ANR 但未被实时处理（进程被杀）
        // 此处仅清理 marker，因为实时检测已经处理了大部分情况
        CsFileUtils.delete(marker.absolutePath)
    }
}
