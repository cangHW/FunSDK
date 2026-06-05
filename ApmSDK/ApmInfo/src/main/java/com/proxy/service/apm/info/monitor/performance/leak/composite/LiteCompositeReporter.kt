package com.proxy.service.apm.info.monitor.performance.leak.composite

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.leak.mode.MemoryLeakMode
import com.proxy.service.apm.info.monitor.performance.leak.report.MemoryLeakGroup
import com.proxy.service.apm.info.monitor.performance.leak.report.MemoryLeakReport
import com.proxy.service.apm.info.monitor.performance.leak.watcher.check.RetainedObject
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @date: 2026/6/4 18:00
 * @desc:
 */
class LiteCompositeReporter(
    private vararg val reporters: IReporter<MemoryLeakReport>
) : IReporter<RetainedObject> {

    companion object {
        private const val TAG = "${Constants.TAG}LiteCompositeReporter"
    }

    override fun publish(time: Long, report: RetainedObject) {
        val finalReport = MemoryLeakReport(
            mode = MemoryLeakMode.FULL,
            description = "",
            hprofAnalyzed = false,
            leakGroups = listOf(
                MemoryLeakGroup(
                    signature = report.key,
                    leakedClassName = report.className,
                    description = report.description,
                    leakTrace = null
                )
            )
        )

        report.reference.clearReference()
        reporters.forEach {
            try {
                it.publish(time, finalReport)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }
    }
}