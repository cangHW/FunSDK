package com.proxy.service.apm.info.monitor.performance.lag.mainthread.reporter

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.report.MainThreadLagReport
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.data.time.enums.TimeFormat
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.widget.info.toast.CsToast
import com.proxy.service.widget.info.toast.enums.ToastDuration
import java.io.File

/**
 * 将 [MainThreadLagReport] 格式化写入 `performance/main_thread_lag/` 目录。
 *
 * @author: cangHX
 * @date: 2026/5/25 09:45
 */
class MainThreadLagFileReporter(
    private val dir: String
) : IReporter<MainThreadLagReport> {

    companion object {
        private const val TAG = "${Constants.TAG}MainThreadLag"
    }

    override fun publish(time: Long, report: MainThreadLagReport) {
        try {
            val timeString = CsTimeManager.createFactory(time).get("yyyyMMdd_HHmmss_SSS")
            val file = File(dir, "main_thread_lag_$timeString.txt")
            CsFileWriteUtils.setSourceString(format(report)).writeAsync(file)

            if (CoreConfig.isDebug) {
                CsLogger.tag(TAG).w("检测到主线程卡顿: ${file.absolutePath}")
                CsToast.show("主线程卡顿", ToastDuration.LENGTH_LONG)
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
    }

    private fun format(report: MainThreadLagReport): String {
        val builder = StringBuilder()
        builder.append("主线程慢分发报告").append("\n").append("\n")

        builder.append(Constants.DIVIDER).append("\n")
        builder.append("摘要").append("\n").append("\n")
        builder.append("wallDurationMs: ${report.wallDurationMs}ms").append("\n")
        builder.append("cpuDurationMs: ${report.cpuDurationMs}ms").append("\n")
        val startTimeMsStr = CsTimeManager.createFactory(report.startTimeMs)
            .get(TimeFormat.TYPE_Y_M_D_H_M_S_MS)
        builder.append("startTimeMs: $startTimeMsStr").append("\n")
        val endTimeMsStr = CsTimeManager.createFactory(report.endTimeMs)
            .get(TimeFormat.TYPE_Y_M_D_H_M_S_MS)
        builder.append("endTimeMs: $endTimeMsStr").append("\n")
        builder.append("messageHint: ${report.messageHint ?: ""}")
            .append("\n").append("\n").append("\n")

        val size = report.samplerData.size
        report.samplerData.forEachIndexed { index, samplerData ->
            samplerData.formatTitle(builder, index, size)
            samplerData.formatContent(builder)
        }

        return builder.toString()
    }
}