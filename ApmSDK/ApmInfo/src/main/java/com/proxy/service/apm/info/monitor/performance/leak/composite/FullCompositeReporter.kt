package com.proxy.service.apm.info.monitor.performance.leak.composite

import com.proxy.service.apm.info.config.controller.performance.leak.MemoryLeakConfig
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.leak.heap.HeapDumpResult
import com.proxy.service.apm.info.monitor.performance.leak.heap.HeapDumper
import com.proxy.service.apm.info.monitor.performance.leak.mode.MemoryLeakMode
import com.proxy.service.apm.info.monitor.performance.leak.report.MemoryLeakGroup
import com.proxy.service.apm.info.monitor.performance.leak.report.MemoryLeakReport
import com.proxy.service.apm.info.monitor.performance.leak.watcher.check.RetainedObject
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.apm.info.sampler.composite.CompositeSampler
import com.proxy.service.apm.info.sampler.impl.app.AppSampler
import com.proxy.service.apm.info.sampler.impl.device.DeviceSampler
import com.proxy.service.apm.info.sampler.impl.mem.MemSampler
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @date: 2026/6/4 18:00
 * @desc:
 */
class FullCompositeReporter(
    private val tempDir: String,
    private val config: MemoryLeakConfig,
    private vararg val reporters: IReporter<MemoryLeakReport>
) : IReporter<RetainedObject>, AppBackgroundCallback {

    companion object {
        private const val TAG = "${Constants.TAG}FullCompositeReporter"
    }

    private val heapDumper = HeapDumper()
    private val pendingRetained = ArrayList<RetainedObject>()

    @Volatile
    private var heapAnalysisInProgress = false

    @Volatile
    private var backgroundDumpScheduled = false

    override fun publish(time: Long, report: RetainedObject) {
        synchronized(pendingRetained) {
            pendingRetained.add(report)
            tryDump()
        }
    }

    override fun whenAppBackground() {
        tryDump()
    }

    private fun tryDump() {
        if (CsContextManager.isInBackground()) {
            if (backgroundDumpScheduled) {
                return
            }
            backgroundDumpScheduled = true
            CsTask.ioThread()?.delay(
                Constants.MONITOR_MEMORY_LEAK_BACKGROUND_DUMP_DELAY_MS,
                TimeUnit.MILLISECONDS
            )?.doOnNext(object : IConsumer<Long> {
                override fun accept(value: Long) {
                    backgroundDumpScheduled = false

                    synchronized(pendingRetained) {
                        maybeDump(CsContextManager.isInBackground())
                    }
                }
            })?.start()
        } else {
            maybeDump(false)
        }
    }

    private fun maybeDump(isInBackground: Boolean) {
        val retainedCount = pendingRetained.size
        CsLogger.tag(TAG).d("retainedCount=$retainedCount")

        if (heapAnalysisInProgress) {
            CsLogger.tag(TAG).d("分析进行中，跳过 dump")
            return
        }

        val threshold = if (isInBackground) {
            config.getRetainedBackgroundThreshold()
        } else {
            config.getRetainedVisibleThreshold()
        }

        if (retainedCount < threshold) {
            CsLogger.tag(TAG).d("retained=$retainedCount < threshold=$threshold，等待更多 retained")
            return
        }

        heapAnalysisInProgress = true

        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                try {
                    when (val result = heapDumper.dump(tempDir)) {
                        is HeapDumpResult.Success -> {
                            if (result.groups.isEmpty()) {
                                publishFallbackReport(
                                    hprofAnalyzed = true,
                                    description = "Heap analysis completed but no leak traces found in hprof"
                                )
                            } else {
                                publishFullReport(result.groups)
                            }
                        }

                        HeapDumpResult.DumpFailed -> {
                            publishFallbackReport(
                                description = "Heap dump failed"
                            )
                        }

                        HeapDumpResult.AnalysisFailed -> {
                            publishFallbackReport(
                                description = "Heap dump succeeded but Shark analysis failed"
                            )
                        }
                    }
                } catch (throwable: Throwable) {
                    CsLogger.tag(TAG).e(throwable)
                    publishFallbackReport(
                        description = "Unexpected error during heap dump or analysis"
                    )
                } finally {
                    synchronized(pendingRetained) {
                        pendingRetained.forEach {
                            it.reference.clearReference()
                        }
                        pendingRetained.clear()
                    }
                    heapAnalysisInProgress = false
                }
                return ""
            }
        })?.start()
    }

    private fun publishFullReport(groups: List<MemoryLeakGroup>) {
        val sampler = CompositeSampler(
            AppSampler.create(),
            DeviceSampler.create(),
            MemSampler.create()
        )
        val samplerData = sampler.sampleNow()
        val report = MemoryLeakReport(
            mode = MemoryLeakMode.FULL,
            description = "",
            hprofAnalyzed = true,
            leakGroups = groups,
            samplerData = samplerData
        )
        publish(System.currentTimeMillis(), report)
    }

    private fun publishFallbackReport(hprofAnalyzed: Boolean = false, description: String) {
        val retained = synchronized(pendingRetained) {
            pendingRetained.toList()
        }
        val report = MemoryLeakReport(
            mode = MemoryLeakMode.FULL,
            description = description,
            hprofAnalyzed = hprofAnalyzed,
            leakGroups = retained.map {
                MemoryLeakGroup(
                    signature = it.key,
                    leakedClassName = it.className,
                    description = it.description,
                    leakTrace = null
                )
            }
        )
        publish(System.currentTimeMillis(), report)
    }

    private fun publish(time: Long, report: MemoryLeakReport) {
        reporters.forEach {
            try {
                it.publish(time, report)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }
    }

}