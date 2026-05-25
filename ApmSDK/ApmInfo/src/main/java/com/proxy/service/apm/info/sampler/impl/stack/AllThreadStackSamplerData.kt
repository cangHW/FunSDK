package com.proxy.service.apm.info.sampler.impl.stack

import com.proxy.service.apm.info.sampler.base.BaseSamplerData
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.data.time.enums.TimeFormat

/**
 * @author: cangHX
 * @date: 2026/5/25 18:45
 * @desc: 某一时刻的全进程线程栈快照（一次 sampleNow 对应一条）。
 */
class AllThreadStackSamplerData(timestampMs: Long) : BaseSamplerData(timestampMs) {
    /**
     * 已格式化的全线程栈文本，直接写入报告
     * */
    var allThreadStacksText: String = ""
    var threadCount: Int = 0
    var truncated: Boolean = false

    override fun formatTitle(builder: StringBuilder, total: Int) {
        if (total > 1) {
            builder.append("全线程栈信息采样 ($total)").append("\n").append("\n")
        } else {
            builder.append("全线程栈信息采样").append("\n").append("\n")
        }
    }

    override fun formatContent(builder: StringBuilder, index: Int, total: Int) {
        if (total > 1) {
            val timeStr = CsTimeManager.createFactory(timestampMs)
                .get(TimeFormat.TYPE_Y_M_D_H_M_S_MS)
            builder.append("--- all threads #${index + 1} @ $timeStr ($timestampMs) ---")
                .append("\n")
        }

        builder.append("thread count: $threadCount, truncated: $truncated").append("\n\n")
        builder.append(allThreadStacksText).append("\n")
    }
}