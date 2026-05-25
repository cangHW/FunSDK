package com.proxy.service.apm.info.sampler.impl.logcat

import com.proxy.service.apm.info.sampler.base.BaseSamplerData
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.data.time.enums.TimeFormat

/**
 * 某一时刻的 logcat 缓冲区快照（一次 [com.proxy.service.apm.info.sampler.ISampler.sampleNow] 对应一条）。
 *
 * @author: cangHX
 * @date: 2026/5/26
 */
class LogcatSamplerData(timestampMs: Long) : BaseSamplerData(timestampMs) {

    var logcatText: String = ""
    var lineCount: Int = 0
    var truncated: Boolean = false
    var errorHint: String? = null

    override fun formatTitle(builder: StringBuilder, total: Int) {
        if (total > 1) {
            builder.append("logcat 信息采样 ($total)").append("\n").append("\n")
        } else {
            builder.append("logcat 信息采样").append("\n").append("\n")
        }
    }

    override fun formatContent(builder: StringBuilder, index: Int, total: Int) {
        if (total > 1) {
            val timeStr = CsTimeManager.createFactory(timestampMs)
                .get(TimeFormat.TYPE_Y_M_D_H_M_S_MS)
            builder.append("--- logcat #${index + 1} @ $timeStr ($timestampMs) ---").append("\n")
        }

        builder.append("lines: $lineCount, truncated: $truncated").append("\n")
        errorHint?.let { builder.append("hint: $it").append("\n") }
        builder.append("\n")
        builder.append(logcatText).append("\n")
    }
}
