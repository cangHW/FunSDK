package com.proxy.service.apm.info.sampler.impl.cpu

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.sampler.base.BaseSampler
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.BufferedReader
import java.io.FileReader

/**
 * 基于 /proc/self/stat（必选）与 /proc/stat（可选降级）的 CPU 采样。
 */
class CpuSampler private constructor(
    intervalMs: Long,
) : BaseSampler<CpuSamplerData>(
    intervalMs,
) {

    companion object {
        private const val TAG = "${Constants.TAG}CpuSampler"
        private const val PROC_SELF_STAT = "/proc/self/stat"
        private const val PROC_STAT = "/proc/stat"

        fun create(intervalMs: Long): CpuSampler {
            return CpuSampler(intervalMs)
        }
    }

    @Volatile
    private var skipProcStat = false

    @Volatile
    private var procStatDeniedLogged = false

    @Volatile
    private var procSelfReadFailedLogged = false

    override fun getTag(): String {
        return TAG
    }

    override fun capture() {
        val snapshot = readCpuSnapshot() ?: return
        addData(snapshot)
    }

    private fun readCpuSnapshot(): CpuSamplerData? {
        val processLine = readFirstLineQuiet(PROC_SELF_STAT) ?: run {
            logProcSelfReadFailedOnce()
            return null
        }
        val processParts = processLine.split("\\s+".toRegex())
        if (processParts.size < 17) {
            return null
        }

        val data = CpuSamplerData(System.currentTimeMillis())
        data.processUserJiffies = processParts[13].toLongOrNull() ?: 0L
        data.processSystemJiffies = processParts[14].toLongOrNull() ?: 0L

        if (skipProcStat) {
            data.systemStatsAvailable = false
            return data
        }

        val systemLine = readFirstLineQuiet(PROC_STAT)
        if (systemLine == null) {
            skipProcStat = true
            data.systemStatsAvailable = false
            logProcStatDeniedOnce()
            return data
        }

        val systemParts = systemLine.split("\\s+".toRegex())
        if (systemParts.size < 6) {
            data.systemStatsAvailable = false
            return data
        }

        data.systemStatsAvailable = true
        data.systemUserJiffies = systemParts[1].toLongOrNull() ?: 0L
        data.systemSystemJiffies = systemParts[2].toLongOrNull() ?: 0L
        data.systemIoWaitJiffies = systemParts[5].toLongOrNull() ?: 0L
        return data
    }

    private fun readFirstLineQuiet(path: String): String? {
        return try {
            BufferedReader(FileReader(path)).use { reader ->
                reader.readLine()
            }
        } catch (_: Throwable) {
            null
        }
    }

    private fun logProcStatDeniedOnce() {
        if (procStatDeniedLogged) {
            return
        }
        procStatDeniedLogged = true
        CsLogger.tag(TAG).w(
            "$PROC_STAT unavailable (e.g. EACCES on API 29+); system cpu fields degraded"
        )
    }

    private fun logProcSelfReadFailedOnce() {
        if (procSelfReadFailedLogged) {
            return
        }
        procSelfReadFailedLogged = true
        CsLogger.tag(TAG).w("$PROC_SELF_STAT unavailable; skip cpu sampling")
    }
}
