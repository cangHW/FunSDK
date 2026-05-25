package com.proxy.service.apm.info.sampler.impl.stack

import com.proxy.service.apm.info.sampler.base.BaseSamplerData
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.data.time.enums.TimeFormat

/**
 * @author: cangHX
 * @date: 2026/5/22 17:06
 * @desc: 带时间戳的主线程堆栈快照
 */
class MainThreadStackSamplerData(timestampMs: Long) : BaseSamplerData(timestampMs) {

    var stackTrace: String = ""

    override fun formatTitle(builder: StringBuilder, total: Int) {
        if (total > 1) {
            builder.append("主堆栈信息采样 ($total)").append("\n").append("\n")
        } else {
            builder.append("主堆栈信息采样").append("\n").append("\n")
        }
    }

    override fun formatContent(builder: StringBuilder, index: Int, total: Int) {
        if (total > 1) {
            val timeStr = CsTimeManager.createFactory(timestampMs)
                .get(TimeFormat.TYPE_Y_M_D_H_M_S_MS)
            builder.append("--- stack #${index + 1} @ $timeStr ($timestampMs) ---").append("\n")
        }

        builder.append(stackTrace).append("\n").append("\n")
    }

}