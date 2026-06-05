package com.proxy.service.apm.info.monitor.performance.leak.reporter

import com.proxy.service.apm.info.config.controller.performance.leak.IMemoryLeakConfigGet
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.leak.report.MemoryLeakReport
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import java.io.File

/**
 * @author: cangHX
 * @date: 2026/6/4 18:05
 * @desc:
 */
class MemoryLeakFileReporter(
    private val dir: String,
    private val config: IMemoryLeakConfigGet
) : IReporter<MemoryLeakReport> {

    companion object {
        private const val TAG = "${Constants.TAG}MemoryLeakFileReporter"
    }

    override fun publish(time: Long, report: MemoryLeakReport) {
        try {
            val timeString = CsTimeManager.createFactory(time).get("yyyyMMdd_HHmmss_SSS")
            val file = File(dir, "memory_leak_$timeString.txt")
            val content = format(report)
            CsFileWriteUtils.setSourceString(content).writeAsync(file)

            val summary = buildString {
                append("检测到内存泄漏(FULL): groups=")
                append(report.leakGroups.size)
                append(", file=")
                append(file.absolutePath)
            }
            MemoryLeakToastHelper.notify(config, summary)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
    }

    private fun format(report: MemoryLeakReport): String {
        val builder = StringBuilder()
        builder.append("内存泄漏报告（FULL 模式）").append("\n\n")
        builder.append(Constants.DIVIDER).append("\n")
        builder.append("摘要").append("\n\n")
        builder.append("distinctLeakGroups: ${report.leakGroups.size}").append("\n")
        builder.append("hprofAnalyzed: ${report.hprofAnalyzed}").append("\n\n")

        report.leakGroups.forEachIndexed { index, group ->
            builder.append(Constants.DIVIDER).append("\n")
            builder.append("泄漏组 ${index + 1}/${report.leakGroups.size}")
                .append("  signature=${group.signature}").append("\n\n")
            builder.append("leakedClass: ${group.leakedClassName}").append("\n")
            builder.append("description: ${group.description}").append("\n")
            builder.append("retainedCount: ${group.retainedCount}").append("\n")
            group.leakTrace?.let {
                builder.append("\n").append(it).append("\n")
            }
            builder.append("\n")
        }

        val size = report.samplerData.size
        report.samplerData.forEachIndexed { index, samplerData ->
            samplerData.formatTitle(builder, index, size)
            samplerData.formatContent(builder)
        }
        return builder.toString()
    }
}