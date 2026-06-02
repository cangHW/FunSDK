package com.proxy.service.apm.info.monitor.anr.reporter

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.anr.report.AnrReport
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import java.io.File

class AnrFileReporter(
    private val dir: String
) : IReporter<AnrReport> {

    companion object {
        private const val TAG = "${Constants.TAG}AnrFileReporter"
    }

    override fun publish(time: Long, report: AnrReport) {
        try {
            val timeString = CsTimeManager.createFactory(time).get("yyyyMMdd_HHmmss_SSS")
            val file = File(dir, "anr_$timeString.txt")
            CsFileWriteUtils.setSourceString(format(report)).writeSync(file)
            CsLogger.tag(TAG).d("ANR report saved: ${file.absolutePath}")
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }

    private fun format(report: AnrReport): String {
        val builder = StringBuilder()
        builder.append("ANR 报告").append("\n\n")

        val size = report.samplerData.size
        report.samplerData.forEachIndexed { index, samplerData ->
            samplerData.formatTitle(builder, index, size)
            samplerData.formatContent(builder)
        }

        return builder.toString()
    }
}
