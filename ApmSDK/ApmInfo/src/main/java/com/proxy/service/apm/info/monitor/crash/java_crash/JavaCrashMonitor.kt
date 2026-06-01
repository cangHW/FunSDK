package com.proxy.service.apm.info.monitor.crash.java_crash

import android.app.Application
import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.config.controller.common.CommonConfig
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.base.AbstractMonitor
import com.proxy.service.apm.info.monitor.crash.java_crash.hook.ExceptionHook
import com.proxy.service.apm.info.monitor.crash.java_crash.hook.UncaughtHandlerChain
import com.proxy.service.apm.info.monitor.crash.java_crash.reporter.JavaCrashFileReporter
import com.proxy.service.apm.info.report.composite.CompositeReporter
import com.proxy.service.apm.info.report.impl.CallbackReporter
import com.proxy.service.apm.info.sampler.composite.CompositeSampler
import com.proxy.service.apm.info.sampler.impl.app.AppSampler
import com.proxy.service.apm.info.sampler.impl.cpu.CpuSampler
import com.proxy.service.apm.info.sampler.impl.device.DeviceSampler
import com.proxy.service.apm.info.sampler.impl.logcat.LogcatSampler
import com.proxy.service.apm.info.sampler.impl.mem.MemSampler
import com.proxy.service.apm.info.sampler.impl.stack.AllThreadStackSampler
import com.proxy.service.apm.info.utils.FileUtils
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @date: 2025/4/22 17:14
 * @desc:
 */
class JavaCrashMonitor private constructor() : AbstractMonitor<CommonConfig>() {

    companion object {
        private const val TAG: String = "${Constants.TAG}JavaCrash"

        private val mInstance by lazy { JavaCrashMonitor() }

        fun getInstance(): JavaCrashMonitor {
            return mInstance
        }
    }

    private var exceptionHook: ExceptionHook? = null
    private var primaryForUninstall: Thread.UncaughtExceptionHandler? = null

    override fun start(application: Application, apmConfig: ApmConfig, config: CommonConfig) {
        val prev = Thread.getDefaultUncaughtExceptionHandler()
        if (prev is ExceptionHook) {
            CsLogger.tag(TAG).w("Java crash hook already installed")
            return
        }
        val backups = UncaughtHandlerChain.resolveBackups(prev)
        val samplers = CompositeSampler(
            AppSampler.create(),
            DeviceSampler.create(),
            CpuSampler.create(Long.MAX_VALUE),
            MemSampler.create(),
            AllThreadStackSampler.create(),
            LogcatSampler.create()
        )
        val reporters = CompositeReporter(
            JavaCrashFileReporter(getLogFileDir(application)),
            CallbackReporter(apmConfig.getJavaCrashReporter())
        )

        exceptionHook = ExceptionHook(
            backups.primary,
            backups.system,
            samplers,
            reporters,
        )
        primaryForUninstall = backups.primary
        Thread.setDefaultUncaughtExceptionHandler(exceptionHook)
    }

    override fun stop() {
        super.stop()
        Thread.setDefaultUncaughtExceptionHandler(primaryForUninstall)
        exceptionHook = null
        primaryForUninstall = null
    }


    override fun getLogFileDir(application: Application): String {
        return FileUtils.getDefaultDir(application, "crash/java/")
    }
}
