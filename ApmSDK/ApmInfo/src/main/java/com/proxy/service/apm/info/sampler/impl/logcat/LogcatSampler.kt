package com.proxy.service.apm.info.sampler.impl.logcat

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.sampler.SamplerData
import com.proxy.service.apm.info.sampler.base.BaseSampler

/**
 * logcat 同步采样器。仅用于崩溃等场景调用 [sampleNow]，不要 [start] 周期任务。
 *
 * @author: cangHX
 * @date: 2026/5/26
 */
class LogcatSampler private constructor(
    private val types: List<Int> = listOf(),
    private val maxLines: Int = Constants.MONITOR_CRASH_LOGCAT_MAX_LINES,
    private val timeoutMs: Long = Constants.MONITOR_CRASH_LOGCAT_TIMEOUT_MS,
    private val maxBytes: Long = Constants.MONITOR_CRASH_LOGCAT_MAX_BYTES
) : BaseSampler<LogcatSamplerData>(
    Long.MAX_VALUE
) {

    companion object {
        const val TYPE_LOG_MAIN: Int = 1
        const val TYPE_LOG_SYSTEM: Int = 2
        const val TYPE_LOG_CRASH: Int = 3

        private const val TAG = "${Constants.TAG}LogcatSampler"

        fun create(
            types: List<Int> = listOf(),
            maxLines: Int = Constants.MONITOR_CRASH_LOGCAT_MAX_LINES,
            timeoutMs: Long = Constants.MONITOR_CRASH_LOGCAT_TIMEOUT_MS,
            maxBytes: Long = Constants.MONITOR_CRASH_LOGCAT_MAX_BYTES
        ): LogcatSampler {
            return LogcatSampler(
                types = types,
                maxLines = maxLines,
                timeoutMs = timeoutMs,
                maxBytes = maxBytes,
            )
        }
    }

    override fun start() {
//        super.start()
    }

    override fun stop() {
//        super.stop()
    }

    override fun snapshotsInWindow(startMs: Long, endMs: Long): List<SamplerData> {
        return sampleNow()
    }

    override fun getTag(): String = TAG

    override fun capture() {
        val timestampMs = System.currentTimeMillis()
        val data = LogcatSamplerData(timestampMs)
        val result = LogcatDumper.dumpSync(
            types = types,
            maxLines = maxLines,
            timeoutMs = timeoutMs,
            maxBytes = maxBytes,
        )
        data.logcatText = result.text
        data.lineCount = result.lineCount
        data.truncated = result.truncated
        data.errorHint = result.errorHint
        addData(data)
    }
}
