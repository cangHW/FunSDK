package com.proxy.service.camera.base.mode.loader.bean

import android.hardware.camera2.params.MeteringRectangle
import androidx.annotation.IntRange

/**
 * @author: cangHX
 * @data: 2026/2/7 16:05
 * @desc: 测光区域
 */
class MeteringAreaInfo private constructor(
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
         * @param x         传感器 active array 坐标系下，区域左上角 x
         * @param y         传感器 active array 坐标系下，区域左上角 y
         * @param width     传感器 active array 坐标系下，区域宽度
         * @param height    传感器 active array 坐标系下，区域高度
         * @param weight    区域的权重
         * */
        fun create(
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            @IntRange(from = WEIGHT_MIN.toLong(), to = WEIGHT_MAX.toLong()) weight: Int
        ): MeteringAreaInfo {
            return MeteringAreaInfo(x, y, width, height, weight)
        }
    }

    override fun toString(): String {
        return "MeteringAreaInfo(x=$x, y=$y, width=$width, height=$height, weight=$weight)"
    }
}