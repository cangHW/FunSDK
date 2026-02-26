package com.proxy.service.camera.info.utils

import android.util.Size
import com.proxy.service.camera.base.mode.SensorOrientationMode
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import kotlin.math.abs


/**
 * @author: cangHX
 * @data: 2026/2/5 18:27
 * @desc:
 */
object CameraUtils {

    /**
     * 获取拍照时的旋转角度
     * */
    fun calculateRotation(
        som: SensorOrientationMode,
        isFrontCamera: Boolean
    ): Int {
        val rotationDegrees = CsScreenUtils.getScreenRotation().degree * 90
        val sign = if (isFrontCamera) 1 else -1
        return (som.degree + rotationDegrees * sign + 360) % 360
    }

    /**
     * 获取预览时的旋转角度
     * */
    fun calculatePreviewRotation(): Int {
        val rotationDegrees = CsScreenUtils.getScreenRotation().degree * 90
        return (360 - rotationDegrees) % 360
    }

    /**
     * 获取最佳预览尺寸
     * */
     fun calculatePreviewSize(
        supportedSizes: List<Size>,
        viewWidth: Int,
        viewHeight: Int
    ): Size? {
        val aspectRatio = checkRatio(viewWidth, viewHeight)

        var offset = Float.MAX_VALUE
        var finalWidth = -1
        var finalHeight = -1

        supportedSizes.forEach {
            val cameraRatio = checkRatio(it.width, it.height)
            val temp = abs(cameraRatio - aspectRatio)
            if (temp <= offset) {
                offset = temp
                if (it.width > finalWidth && it.height > finalHeight) {
                    finalWidth = it.width
                    finalHeight = it.height
                }
            }
        }

        if (finalWidth == -1 && finalHeight == -1) {
            return null
        }

        return Size(finalWidth, finalHeight)
    }

    private fun checkRatio(width: Int, height: Int): Float {
        return if (width > height) {
            width.toFloat() / height
        } else {
            height.toFloat() / width
        }
    }

}