package com.proxy.service.apm.info.sampler.impl.mem

import com.proxy.service.apm.info.sampler.base.BaseSamplerData
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.data.time.enums.TimeFormat

/**
 * 某一时刻的内存快照（一次 [com.proxy.service.apm.info.sampler.ISampler.sampleNow] 对应一条）。
 */
class MemSamplerData(timestampMs: Long) : BaseSamplerData(timestampMs) {

    var appSectionText: String = ""
    var systemSectionText: String = ""
    var errorHint: String? = null

    override fun formatTitle(builder: StringBuilder, total: Int) {
        if (total > 1) {
            builder.append("内存信息采样 ($total)").append("\n").append("\n")
        }else{
            builder.append("内存信息采样").append("\n").append("\n")
        }
    }

    override fun formatContent(builder: StringBuilder, index: Int, total: Int) {
        if (total > 1) {
            val timeStr = CsTimeManager.createFactory(timestampMs)
                .get(TimeFormat.TYPE_Y_M_D_H_M_S_MS)
            builder.append("--- memory #${index + 1} @ $timeStr ($timestampMs) ---").append("\n")
        }

        errorHint?.let {
            builder.append("hint: $it").append("\n").append("\n")
        }
        builder.append(appSectionText).append("\n")
        builder.append(systemSectionText).append("\n").append("\n")
    }
}
