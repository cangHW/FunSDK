package com.proxy.service.apm.info.sampler.impl.cpu

import com.proxy.service.apm.info.sampler.base.BaseSamplerData
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.data.time.enums.TimeFormat

class CpuSamplerData(timestampMs: Long) : BaseSamplerData(timestampMs) {

    var processUserJiffies: Long = 0
    var processSystemJiffies: Long = 0

    // 两次采样间的 CPU 使用率（百分比）
    var processCpuPercent: Double = -1.0
    var processUserPercent: Double = -1.0
    var processSystemPercent: Double = -1.0

    // schedstat：运行时间 vs 等待时间
    var schedRunTimeNs: Long = 0
    var schedWaitTimeNs: Long = 0
    var schedWaitPercent: Double = -1.0

    // 各核心频率
    var coreFrequencies: List<CpuCoreInfo> = emptyList()

    // CPU 占用最高的线程
    var topThreads: List<ThreadCpuInfo> = emptyList()

    override fun formatTitle(builder: StringBuilder, total: Int) {
        if (total > 1) {
            builder.append("CPU 信息采样 ($total)").append("\n").append("\n")
        } else {
            builder.append("CPU 信息采样").append("\n").append("\n")
        }
    }

    override fun formatContent(builder: StringBuilder, index: Int, total: Int) {
        if (total > 1) {
            val timeStr = CsTimeManager.createFactory(timestampMs)
                .get(TimeFormat.TYPE_Y_M_D_H_M_S_MS)
            builder.append("--- cpu #${index + 1} @ $timeStr ($timestampMs) ---").append("\n")
        }

        // 进程 CPU 使用率
        if (processCpuPercent >= 0) {
            builder.append("进程 CPU 使用率: ")
                .append(String.format("%.1f%%", processCpuPercent))
                .append(" (user: ")
                .append(String.format("%.1f%%", processUserPercent))
                .append(", system: ")
                .append(String.format("%.1f%%", processSystemPercent))
                .append(")")
                .append("\n")
        } else {
            builder.append("进程 CPU jiffies: user=$processUserJiffies, system=$processSystemJiffies")
                .append(" (首次采样，无差值)")
                .append("\n")
        }

        // 调度等待
        if (schedWaitPercent >= 0) {
            builder.append("调度等待占比: ")
                .append(String.format("%.1f%%", schedWaitPercent))
                .append(" (等待调度的时间越高说明 CPU 越繁忙)")
                .append("\n")
        }

        // CPU 核心频率
        if (coreFrequencies.isNotEmpty()) {
            builder.append("CPU 核心频率:\n")
            for (core in coreFrequencies) {
                val freqMhz = core.currentKhz / 1000
                val info = if (core.maxKhz != null && core.maxKhz > 0) {
                    val maxMhz = core.maxKhz / 1000
                    val percent = (core.currentKhz * 100.0 / core.maxKhz).toInt()
                    "  core${core.index}: ${freqMhz}MHz / ${maxMhz}MHz ($percent%)"
                } else {
                    "  core${core.index}: ${freqMhz}MHz"
                }
                builder.append(info).append("\n")
            }
        }

        // CPU 占用最高的线程
        if (topThreads.isNotEmpty()) {
            builder.append("CPU 占用 Top 线程:\n")
            for (thread in topThreads) {
                val total = thread.userJiffies + thread.systemJiffies
                builder.append("  tid=${thread.tid}")
                    .append(" (${thread.name})")
                    .append(" state=${thread.state}")
                    .append(" jiffies=$total")
                    .append(" (user=${thread.userJiffies}, sys=${thread.systemJiffies})")
                    .append("\n")
            }
        }

        builder.append("\n")
    }
}

data class CpuCoreInfo(
    val index: Int,
    val currentKhz: Long,
    val maxKhz: Long?
)

data class ThreadCpuInfo(
    val tid: Int,
    val name: String,
    val userJiffies: Long,
    val systemJiffies: Long,
    val state: String
)
