package com.proxy.service.camera.base.mode.af

import android.hardware.camera2.params.MeteringRectangle
import androidx.annotation.IntRange

/**
 * @author: cangHX
 * @data: 2026/2/7 16:05
 * @desc:
 */
class FocusAreaInfo private constructor(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val weight: Int
) {

    companion object {
        private const val WEIGHT_MIN = MeteringRectangle.METERING_WEIGHT_MIN
        const val WEIGHT_MAX = MeteringRectangle.METERING_WEIGHT_MAX

        /**
         * @param x         对焦区域的左上角
         * @param y         对焦区域的左上角
         * @param width     对焦区域的宽度
         * @param height    对焦区域的高度
         * @param weight    对焦区域的权重
         * */
        fun create(
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            @IntRange(from = WEIGHT_MIN.toLong(), to = WEIGHT_MAX.toLong()) weight: Int
        ): FocusAreaInfo {
            return FocusAreaInfo(x, y, width, height, weight)
        }
    }

    override fun toString(): String {
        return "FocusAreaInfo(x=$x, y=$y, width=$width, height=$height, weight=$weight)"
    }
}