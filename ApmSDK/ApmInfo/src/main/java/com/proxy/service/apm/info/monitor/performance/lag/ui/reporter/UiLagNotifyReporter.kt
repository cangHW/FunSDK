package com.proxy.service.apm.info.monitor.performance.lag.ui.reporter

import com.proxy.service.apm.info.config.controller.performance.uilag.IUiLagConfigGet
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.lag.ui.report.UiLagReport
import com.proxy.service.apm.info.monitor.performance.lag.ui.frame.JankSeverity
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.widget.info.toast.CsToast
import com.proxy.service.widget.info.toast.enums.ToastDuration
import java.lang.StringBuilder

/**
 * UI 渲染卡顿通知：MODERATE/SEVERE 打日志；仅 SEVERE 在 Debug 下 Toast（限流）。
 */
class UiLagNotifyReporter(
    private val config: IUiLagConfigGet,
) : IReporter<UiLagReport> {

    companion object {
        private const val TAG = "${Constants.TAG}UiLag"
    }

    @Volatile
    private var lastModerateLogWallMs: Long = 0L


    override fun publish(time: Long, report: UiLagReport) {
        when (report.severity) {
            JankSeverity.NONE, JankSeverity.MILD -> {
                return
            }

            JankSeverity.MODERATE -> {
                logModerate(report)
            }

            JankSeverity.SEVERE -> {
                logSevere(report)
            }
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


    @Volatile
    private var lastActivitySimpleName: String? = ""

    @Volatile
    private var lastToastWallMs: Long = 0L

    @Volatile
    private var count = 0

    private fun maybeShowToast(event: UiLagReport) {
        if (!CoreConfig.isDebug || !config.getEnableDebugToast()) {
            return
        }
        val name = CsContextManager.getTopActivity()?.javaClass?.simpleName
        if (lastActivitySimpleName != name) {
            lastToastWallMs = 0L
            count = 0
            lastActivitySimpleName = name
        }
        count++
        val now = System.currentTimeMillis()
        if (now - lastToastWallMs < Constants.MONITOR_UI_LAG_NOTIFY_THROTTLE_MS) {
            return
        }
        lastToastWallMs = now

        val builder = StringBuilder()
        if (count > 1) {
            builder.append("(${count}次)")
        }
        builder.append(" UI严重卡顿 ·")
        builder.append(" 整帧${event.totalDurationMs}ms")
        builder.append(" (瓶颈 ${event.primaryBottleneck})")

        CsToast.show(builder.toString(), ToastDuration.LENGTH_LONG)

        count = 0
    }
}
