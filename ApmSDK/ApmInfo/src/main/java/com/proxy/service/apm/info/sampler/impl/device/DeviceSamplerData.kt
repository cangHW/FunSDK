package com.proxy.service.apm.info.sampler.impl.device

import com.proxy.service.apm.info.sampler.base.BaseSamplerData
import com.proxy.service.core.framework.convert.CsStorageUnit
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.data.time.enums.TimeFormat
import com.proxy.service.core.framework.system.device.CsDeviceUtils

/**
 * @author: cangHX
 * @date: 2026/5/26 16:37
 * @desc:
 */
class DeviceSamplerData(timestampMs: Long) : BaseSamplerData(timestampMs) {

    override fun formatTitle(builder: StringBuilder, total: Int) {
        if (total > 1) {
            builder.append("设备信息采样 ($total)").append("\n").append("\n")
        } else {
            builder.append("设备信息采样").append("\n").append("\n")
        }
    }

    override fun formatContent(builder: StringBuilder, index: Int, total: Int) {
        if (total > 1) {
            val timeStr = CsTimeManager.createFactory(timestampMs)
                .get(TimeFormat.TYPE_Y_M_D_H_M_S_MS)
            builder.append("--- 设备信息 #${index + 1} @ $timeStr ($timestampMs) ---").append("\n")
        }

        builder.append("device brand: ${CsDeviceUtils.getDeviceBrand()}").append("\n")
        builder.append("device model: ${CsDeviceUtils.getDeviceModel()}").append("\n")
        builder.append("device type: ${CsDeviceUtils.getDeviceType().name}").append("\n")
        builder.append("rom type: ${CsDeviceUtils.getRomType().name}").append("\n")
        builder.append("android level: ${CsDeviceUtils.getDeviceSdk()}").append("\n")
        val totalStorage = CsStorageUnit.B_UNIT_1000.toMbLong(CsDeviceUtils.getDeviceTotalStorage())
        builder.append("total storage: $totalStorage MB").append("\n")
        val availStorage = CsStorageUnit.B_UNIT_1000.toMbLong(CsDeviceUtils.getDeviceAvailStorage())
        builder.append("avail storage: $availStorage MB").append("\n")
        builder.append("isRoot: ${CsDeviceUtils.isRoot()}").append("\n")
        builder.append("\n").append("\n")
    }
}