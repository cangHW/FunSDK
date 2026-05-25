package com.proxy.service.apm.info.sampler.impl.cpu

import com.proxy.service.apm.info.sampler.base.BaseSamplerData
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.data.time.enums.TimeFormat

/**
 * @author: cangHX
 * @date: 2026/5/22 17:16
 * @desc: 带时间戳的 CPU 统计快照（/proc/self/stat 必选；/proc/stat 不可用时系统字段降级）。
 */
class CpuSamplerData(timestampMs: Long) : BaseSamplerData(timestampMs) {

    var processUserJiffies: Long = 0
    var processSystemJiffies: Long = 0
    var systemStatsAvailable: Boolean = true
    var systemUserJiffies: Long = 0
    var systemSystemJiffies: Long = 0
    var systemIoWaitJiffies: Long = 0

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
        builder.append("process user jiffies: $processUserJiffies").append("\t")
        builder.append("process system jiffies: $processSystemJiffies").append("\n")
        if (systemStatsAvailable) {
            builder.append("system user jiffies: $systemUserJiffies").append("\t")
            builder.append("system system jiffies: $systemSystemJiffies").append("\t")
            builder.append("system iowait jiffies: $systemIoWaitJiffies").append("\n")
        } else {
            builder.append("system cpu: N/A (/proc/stat unavailable)").append("\n")
        }
        builder.append("\n")
    }

}