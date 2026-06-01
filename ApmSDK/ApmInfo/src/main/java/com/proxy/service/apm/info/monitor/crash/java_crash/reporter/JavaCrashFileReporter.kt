package com.proxy.service.apm.info.monitor.crash.java_crash.reporter

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.crash.java_crash.report.ExceptionReport
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.apm.info.utils.CrashSignatureUtil
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author: cangHX
 * @date: 2026/5/25 16:29
 * @desc:
 */
class JavaCrashFileReporter(
    private val dir: String
) : IReporter<ExceptionReport> {

    companion object {
        private const val TAG = "${Constants.TAG}JavaCrashFileReporter"
    }

    override fun publish(time: Long, report: ExceptionReport) {
        try {
            val timeString = CsTimeManager.createFactory(time).get("yyyyMMdd_HHmmss_SSS")
            val file = File(dir, "$timeString.txt")
            CsFileWriteUtils.setSourceString(format(report)).writeSync(file)
            CsLogger.tag(TAG).d("Java Crash，日志记录成功: ${file.absolutePath}")
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }

    private fun format(report: ExceptionReport): String {
        val builder = StringBuilder()
        builder.append("Java Crash 报告").append("\n").append("\n")
        builder.append("signature: ").append(CrashSignatureUtil.buildSignature(report.throwable))
        builder.append("\n").append("\n")

        builder.append(Constants.DIVIDER).append("\n")
        builder.append("摘要").append("\n").append("\n")
        builder.append("thread name: ${report.thread.name}").append("\n")
        builder.append("thread id: ${report.thread.id}").append("\n")
        builder.append("thread state: ${report.thread.state}").append('\n')
        builder.append(getStackTraceString(report.throwable))
            .append("\n").append("\n").append("\n")

        val size = report.samplerData.size
        report.samplerData.forEachIndexed { index, samplerData ->
            samplerData.formatTitle(builder, index, size)
            samplerData.formatContent(builder)
        }

        return builder.toString()
    }

    private fun getStackTraceString(throwable: Throwable): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        throwable.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}