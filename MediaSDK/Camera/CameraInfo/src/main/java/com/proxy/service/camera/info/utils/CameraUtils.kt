package com.proxy.service.camera.info.utils

import com.proxy.service.camera.base.mode.SensorOrientationMode
import com.proxy.service.core.framework.system.screen.enums.RotationEnum

/**
 * @author: cangHX
 * @data: 2026/2/5 18:27
 * @desc:
 */
object CameraUtils {

    fun calculateRotation(
        som: SensorOrientationMode,
        rotation: RotationEnum,
        isFrontCamera: Boolean
    ): Int {
        val rotationDegrees = when (rotation) {
            RotationEnum.ROTATION_0 -> 0
            RotationEnum.ROTATION_90 -> 90
            RotationEnum.ROTATION_180 -> 180
            RotationEnum.ROTATION_270 -> 270
            else -> 0
        }

        return if (isFrontCamera) {
            // 前置摄像头需要额外处理镜像
            (som.degree + rotationDegrees) % 360
        } else {
            // 后置摄像头
            (som.degree - rotationDegrees + 360) % 360
        }
    }

}