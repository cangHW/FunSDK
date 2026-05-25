package com.proxy.service.apm.info.sampler.impl.app

import com.proxy.service.apm.info.sampler.base.BaseSamplerData
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.data.time.enums.TimeFormat

/**
 * @author: cangHX
 * @date: 2026/5/26 16:37
 * @desc:
 */
class AppSamplerData(timestampMs: Long) : BaseSamplerData(timestampMs) {

    override fun formatTitle(builder: StringBuilder, total: Int) {
        if (total > 1) {
            builder.append("应用信息采样 ($total)").append("\n").append("\n")
        } else {
            builder.append("应用信息采样").append("\n").append("\n")
        }
    }

    override fun formatContent(builder: StringBuilder, index: Int, total: Int) {
        if (total > 1) {
            val timeStr = CsTimeManager.createFactory(timestampMs)
                .get(TimeFormat.TYPE_Y_M_D_H_M_S_MS)
            builder.append("--- 应用信息 #${index + 1} @ $timeStr ($timestampMs) ---").append("\n")
        }

        builder.append("app pid: ${CsAppUtils.getPid()}").append("\n")
        builder.append("app package name: ${CsAppUtils.getPackageName()}").append("\n")
        builder.append("app process name: ${CsAppUtils.getProcessName()}").append("\n")
        builder.append("app version name: ${CsAppUtils.getVersionName()}").append("\n")
        builder.append("app version code: ${CsAppUtils.getVersionCode()}").append("\n")
        builder.append("\n").append("\n")
    }
}