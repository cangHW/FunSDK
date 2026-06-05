package com.proxy.service.apm.info.monitor.performance.leak.reporter

import com.proxy.service.apm.info.config.controller.performance.leak.IMemoryLeakConfigGet
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.leak.report.MemoryLeakReport
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @date: 2026/6/4 18:06
 * @desc:
 */
class MemoryLeakNotifyReporter(
    private val config: IMemoryLeakConfigGet
) : IReporter<MemoryLeakReport> {

    companion object {
        private const val TAG = "${Constants.TAG}MemoryLeakNotifyReporter"
    }

    override fun publish(time: Long, report: MemoryLeakReport) {
        try {
            val group = report.leakGroups.firstOrNull() ?: return

            val message = buildString {
                append("检测到内存泄漏(LITE): ")
                append(group.leakedClassName)
                append(", ")
                append(report.description)
            }

            MemoryLeakToastHelper.notify(config, message)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
    }
}