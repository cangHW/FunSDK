package com.proxy.service.apm.info.monitor.performance.lag.mainthread.engine

import android.os.Debug
import android.os.SystemClock
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.report.MainThreadLagReport
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.hook.DispatchListener
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.apm.info.sampler.ICompositeSampler
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 主线程 Looper 单次 Message 分发耗时监测引擎。
 *
 * @author: cangHX
 * @date: 2026/5/22 18:37
 */
class DispatchWatchEngine(
    private val blockThresholdTime: Long,
    private val sampler: ICompositeSampler,
    private val reporter: IReporter<MainThreadLagReport>,
) : DispatchListener {

    companion object {
        private const val TAG = "${Constants.TAG}DispatchWatchEngine"
    }

    @Volatile
    private var dispatching = AtomicBoolean(false)

    private var wallStart = 0L
    private var cpuStart = 0L
    private var lastHint: String? = null

    override fun onDispatchStart(hint: String?) {
        if (Debug.isDebuggerConnected()) {
            return
        }
        if (!dispatching.compareAndSet(false, true)) {
            CsLogger.tag(TAG).w("Unpaired start, force end previous")
            onDispatchEnd(null)
        }
        lastHint = hint
        wallStart = System.currentTimeMillis()
        cpuStart = SystemClock.currentThreadTimeMillis()
        sampler.start()
    }

    override fun onDispatchEnd(hint: String?) {
        if (!dispatching.compareAndSet(true, false)) {
            CsLogger.tag(TAG).w("Unpaired end, ignored")
            return
        }
        if (hint != null) {
            lastHint = hint
        }
        sampler.stop()

        val wallEnd = System.currentTimeMillis()
        val wallDurationMs = wallEnd - wallStart
        if (wallDurationMs < blockThresholdTime) {
            return
        }
        val cpuEnd = SystemClock.currentThreadTimeMillis()
        val report = buildReport(wallStart, wallEnd, wallDurationMs, cpuEnd - cpuStart)
        publishAsync(wallEnd, report)
    }

    private fun buildReport(
        wallStart: Long,
        wallEnd: Long,
        wallDurationMs: Long,
        cpuDurationMs: Long
    ): MainThreadLagReport {
        return MainThreadLagReport(
            wallDurationMs = wallDurationMs,
            cpuDurationMs = cpuDurationMs,
            startTimeMs = wallStart,
            endTimeMs = wallEnd,
            samplerData = sampler.snapshotsInWindow(wallStart, wallEnd),
            messageHint = lastHint
        )
    }

    /**
     * 报告构建与 IO 均在后台线程，避免阻塞 onDispatchEnd
     * */
    private fun publishAsync(time: Long, report: MainThreadLagReport) {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                reporter.publish(time, report)
                return ""
            }
        })?.start()
    }

}