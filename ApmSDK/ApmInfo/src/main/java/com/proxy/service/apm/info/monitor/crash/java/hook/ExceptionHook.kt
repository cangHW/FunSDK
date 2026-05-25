package com.proxy.service.apm.info.monitor.crash.java.hook

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.crash.bean.ExceptionReport
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.apm.info.sampler.ICompositeSampler
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @date: 2026/5/25 16:19
 * @desc:
 */
class ExceptionHook(
    private val primaryHandler: Thread.UncaughtExceptionHandler?,
    private val systemHandler: Thread.UncaughtExceptionHandler?,
    private val sampler: ICompositeSampler,
    private val reporter: IReporter<ExceptionReport>,
) : Thread.UncaughtExceptionHandler {

    companion object {
        private const val TAG: String = "${Constants.TAG}ExceptionHook"

        private val CRASH_LOCK = Any()
        private val handling = ThreadLocal<Boolean>()
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        synchronized(CRASH_LOCK) {
            if (handling.get() == true) {
                CsLogger.tag(TAG).w("Reentrant uncaughtException, skip report")
                UncaughtHandlerChain.forward(
                    thread,
                    throwable,
                    primaryHandler,
                    systemHandler,
                    skipPrimary = true,
                )
                return
            }
            handling.set(true)
            try {
                handleCrashOnce(thread, throwable)
            } finally {
                handling.remove()
            }
        }
    }

    private fun handleCrashOnce(thread: Thread, throwable: Throwable) {
        try {
            val time = System.currentTimeMillis()
            val report = ExceptionReport(
                time,
                thread,
                throwable,
                sampler.sampleNow(),
            )
            reporter.publish(report.time, report)
        } catch (t: Throwable) {
            CsLogger.tag(TAG).e(t)
        }
        UncaughtHandlerChain.forward(
            thread,
            throwable,
            primaryHandler,
            systemHandler,
            skipPrimary = false,
        )
    }
}
