package com.proxy.service.apm.info.sampler.impl.cpu

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.sampler.base.BaseSampler
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class CpuSampler private constructor(
    intervalMs: Long,
) : BaseSampler<CpuSamplerData>(intervalMs) {

    companion object {
        private const val TAG = "${Constants.TAG}CpuSampler"
        private const val PROC_SELF_STAT = "/proc/self/stat"
        private const val PROC_SELF_SCHEDSTAT = "/proc/self/schedstat"
        private const val PROC_SELF_TASK = "/proc/self/task"
        private const val CPU_FREQ_PATH = "/sys/devices/system/cpu"
        private const val TOP_THREAD_COUNT = 5

        fun create(intervalMs: Long): CpuSampler {
            return CpuSampler(intervalMs)
        }
    }

    @Volatile
    private var lastProcessUserJiffies = 0L
    @Volatile
    private var lastProcessSystemJiffies = 0L
    @Volatile
    private var lastTimestampMs = 0L
    @Volatile
    private var lastSchedRuntime = 0L
    @Volatile
    private var lastSchedWait = 0L

    override fun getTag(): String = TAG

    override fun capture() {
        val snapshot = readCpuSnapshot() ?: return
        addData(snapshot)
    }

    private fun readCpuSnapshot(): CpuSamplerData {
        val now = System.currentTimeMillis()
        val data = CpuSamplerData(now)

        readProcessStat(data, now)
        readSchedStat(data)
        readCpuCoreFrequencies(data)
        readTopThreads(data)

        return data
    }

    private fun readProcessStat(data: CpuSamplerData, now: Long) {
        val line = readFirstLine(PROC_SELF_STAT) ?: return
        val parts = line.split("\\s+".toRegex())
        if (parts.size < 17) {
            return
        }

        val userJiffies = parts[13].toLongOrNull() ?: 0L
        val systemJiffies = parts[14].toLongOrNull() ?: 0L

        data.processUserJiffies = userJiffies
        data.processSystemJiffies = systemJiffies

        if (lastTimestampMs > 0) {
            val elapsedMs = now - lastTimestampMs
            if (elapsedMs > 0) {
                val userDelta = userJiffies - lastProcessUserJiffies
                val systemDelta = systemJiffies - lastProcessSystemJiffies
                val totalDelta = userDelta + systemDelta
                // jiffies 通常 10ms/tick (HZ=100)，转换为百分比
                val cpuTimeMs = totalDelta * 10
                data.processCpuPercent = (cpuTimeMs * 100.0 / elapsedMs).coerceIn(0.0, 100.0 * Runtime.getRuntime().availableProcessors())
                data.processUserPercent = (userDelta * 10 * 100.0 / elapsedMs).coerceIn(0.0, 100.0 * Runtime.getRuntime().availableProcessors())
                data.processSystemPercent = (systemDelta * 10 * 100.0 / elapsedMs).coerceIn(0.0, 100.0 * Runtime.getRuntime().availableProcessors())
            }
        }

        lastProcessUserJiffies = userJiffies
        lastProcessSystemJiffies = systemJiffies
        lastTimestampMs = now
    }

    private fun readSchedStat(data: CpuSamplerData) {
        val line = readFirstLine(PROC_SELF_SCHEDSTAT) ?: return
        val parts = line.split("\\s+".toRegex())
        if (parts.size < 3) {
            return
        }

        val runTimeNs = parts[0].toLongOrNull() ?: 0L
        val waitTimeNs = parts[1].toLongOrNull() ?: 0L

        data.schedRunTimeNs = runTimeNs
        data.schedWaitTimeNs = waitTimeNs

        if (lastSchedRuntime > 0) {
            val runDelta = runTimeNs - lastSchedRuntime
            val waitDelta = waitTimeNs - lastSchedWait
            val total = runDelta + waitDelta
            if (total > 0) {
                data.schedWaitPercent = (waitDelta * 100.0 / total)
            }
        }

        lastSchedRuntime = runTimeNs
        lastSchedWait = waitTimeNs
    }

    private fun readCpuCoreFrequencies(data: CpuSamplerData) {
        val cpuDir = File(CPU_FREQ_PATH)
        if (!cpuDir.exists()) {
            return
        }

        val freqs = mutableListOf<CpuCoreInfo>()
        var index = 0
        while (true) {
            val freqFile = File(cpuDir, "cpu$index/cpufreq/scaling_cur_freq")
            if (!freqFile.exists()) {
                break
            }

            val freqKhz = readFirstLine(freqFile.absolutePath)?.trim()?.toLongOrNull()
            val maxFile = File(cpuDir, "cpu$index/cpufreq/cpuinfo_max_freq")
            val maxKhz = readFirstLine(maxFile.absolutePath)?.trim()?.toLongOrNull()

            if (freqKhz != null) {
                freqs.add(CpuCoreInfo(index, freqKhz, maxKhz))
            }
            index++
            if (index > 16) {
                break
            }
        }

        data.coreFrequencies = freqs
    }

    private fun readTopThreads(data: CpuSamplerData) {
        val taskDir = File(PROC_SELF_TASK)
        if (!taskDir.exists()) {
            return
        }

        val threads = mutableListOf<ThreadCpuInfo>()
        val tids = taskDir.list() ?: return

        for (tidStr in tids) {
            if (tidStr[0] == '.') {
                continue
            }

            val statLine = readFirstLine("$PROC_SELF_TASK/$tidStr/stat") ?: continue
            val parts = statLine.split("\\s+".toRegex())
            if (parts.size < 17) {
                continue
            }

            val tid = tidStr.toIntOrNull() ?: continue
            // 进程名在 parts[1]，格式 (name)
            val name = parts[1].removeSurrounding("(", ")")
            val userJiffies = parts[13].toLongOrNull() ?: 0L
            val systemJiffies = parts[14].toLongOrNull() ?: 0L
            val state = parts[2]

            threads.add(ThreadCpuInfo(tid, name, userJiffies, systemJiffies, state))
        }

        threads.sortByDescending {
            it.userJiffies + it.systemJiffies
        }
        data.topThreads = threads.take(TOP_THREAD_COUNT)
    }

    private fun readFirstLine(path: String): String? {
        return try {
            BufferedReader(FileReader(path)).use { it.readLine() }
        } catch (_: Throwable) {
            null
        }
    }
}
