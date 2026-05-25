package com.proxy.service.apm.info.monitor.performance.lag.ui.reporter

import com.proxy.service.apm.info.config.controller.performance.uilag.IUiLagConfigGet
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.lag.ui.bean.UiLagReport
import com.proxy.service.apm.info.monitor.performance.lag.ui.frame.JankSeverity
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.widget.info.toast.CsToast
import com.proxy.service.widget.info.toast.enums.ToastDuration

/**
 * UI 渲染卡顿通知：MODERATE/SEVERE 打日志；仅 SEVERE 在 Debug 下 Toast（限流）。
 */
class UiLagNotifyReporter(
    private val config: IUiLagConfigGet,
) : IReporter<UiLagReport> {

    companion object {
        private const val TAG = "${Constants.TAG}UiLag"

        @Volatile
        private var lastToastWallMs: Long = 0L

        @Volatile
        private var lastModerateLogWallMs: Long = 0L
    }

    override fun publish(time: Long, report: UiLagReport) {
        when (report.severity) {
            JankSeverity.NONE, JankSeverity.MILD -> return
            JankSeverity.MODERATE -> logModerate(report)
            JankSeverity.SEVERE -> logSevere(report)
        }
        if (report.severity == JankSeverity.SEVERE) {
            maybeShowToast(report)
        }
    }

    private fun logModerate(event: UiLagReport) {
        val now = System.currentTimeMillis()
        if (now - lastModerateLogWallMs < Constants.MONITOR_UI_LAG_NOTIFY_THROTTLE_MS) {
            return
        }
        lastModerateLogWallMs = now
        CsLogger.tag(TAG).w(formatLogLine(event))
    }

    private fun logSevere(event: UiLagReport) {
        CsLogger.tag(TAG).e(formatLogLine(event))
    }

    private fun formatLogLine(event: UiLagReport): String {
        return buildString {
            append(event.activitySimpleName)
            append(' ')
            append(event.severity.name)
            append(" bottleneck=")
            append(event.primaryBottleneck)
            append(" total=")
            append(event.totalDurationMs)
            append("ms overrun=")
            append(event.frameOverrunMs)
            append("ms budget=")
            append(event.frameBudgetMs)
            append("ms(")
            append(event.budgetSource.name)
            append(')')
        }
    }

    private fun maybeShowToast(event: UiLagReport) {
        if (!CoreConfig.isDebug || !config.getEnableDebugToast()) {
            return
        }
        val now = System.currentTimeMillis()
        if (now - lastToastWallMs < Constants.MONITOR_UI_LAG_NOTIFY_THROTTLE_MS) {
            return
        }
        lastToastWallMs = now

        CsToast.show(
            "UI严重卡顿 · 整帧${event.totalDurationMs}ms (瓶颈 ${event.primaryBottleneck})",
            ToastDuration.LENGTH_LONG
        )
    }
}
