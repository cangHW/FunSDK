package com.proxy.service.apm.info.monitor.crash.java

import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.crash.java.hook.ExceptionHook
import com.proxy.service.apm.info.monitor.crash.java.hook.UncaughtHandlerChain
import com.proxy.service.apm.info.monitor.crash.java.reporter.JavaCrashFileReporter
import com.proxy.service.apm.info.report.composite.CompositeReporter
import com.proxy.service.apm.info.report.impl.CallbackReporter
import com.proxy.service.apm.info.sampler.composite.CompositeSampler
import com.proxy.service.apm.info.sampler.impl.app.AppSampler
import com.proxy.service.apm.info.sampler.impl.cpu.CpuSampler
import com.proxy.service.apm.info.sampler.impl.device.DeviceSampler
import com.proxy.service.apm.info.sampler.impl.logcat.LogcatSampler
import com.proxy.service.apm.info.sampler.impl.mem.MemSampler
import com.proxy.service.apm.info.sampler.impl.stack.AllThreadStackSampler
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import java.io.File

/**
 * @author: cangHX
 * @date: 2025/4/22 17:14
 * @desc:
 */
class JavaCrashHookInstaller private constructor() {

    companion object {
        private const val TAG: String = "${Constants.TAG}JavaCrash"

        private val mInstance by lazy { JavaCrashHookInstaller() }

        fun getInstance(): JavaCrashHookInstaller {
            return mInstance
        }
    }

    private var exceptionHook: ExceptionHook? = null
    private var primaryForUninstall: Thread.UncaughtExceptionHandler? = null

    fun install(apmConfig: ApmConfig, dir: String) {
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
        val file = File(dir, "java/")
        CsFileUtils.createDir(file)
        val reporters = CompositeReporter(
            JavaCrashFileReporter(file.absolutePath),
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

    fun uninstall() {
        Thread.setDefaultUncaughtExceptionHandler(primaryForUninstall)
        exceptionHook = null
        primaryForUninstall = null
    }
}
