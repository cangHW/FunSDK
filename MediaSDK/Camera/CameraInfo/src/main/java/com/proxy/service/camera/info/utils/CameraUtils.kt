package com.proxy.service.camera.info.utils

import android.util.Size
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import kotlin.math.abs


/**
 * @author: cangHX
 * @data: 2026/2/5 18:27
 * @desc:
 */
object CameraUtils {

    private const val TAG = "${CameraConstants.TAG}Utils"

    /**
     * 获取预览时的旋转角度
     * */
    fun calculatePreviewRotation(): Int {
        val rotationDegrees = CsScreenUtils.getScreenRotation().degree * 90
        return (360 - rotationDegrees) % 360
    }

    /**
     * 获取最佳尺寸
     * */
    fun calculateSize(
        supportedSizes: List<Size>,
        viewWidth: Int,
        viewHeight: Int
    ): Size? {
        val aspectRatio = viewWidth.toFloat() / viewHeight
        CsLogger.tag(TAG)
            .d("calculateSize. viewWidth=$viewWidth, viewHeight=$viewHeight, aspectRatio=$aspectRatio")

        var offset = Float.MAX_VALUE
        var finalWidth = -1
        var finalHeight = -1

        supportedSizes.forEach {
            val cameraRatio = it.width.toFloat() / it.height
            val temp = abs(cameraRatio - aspectRatio)
            if (temp <= offset) {
                if (offset != temp) {
                    finalWidth = it.width
                    finalHeight = it.height
                } else {
                    if (it.width > finalWidth && it.height > finalHeight) {
                        finalWidth = it.width
                        finalHeight = it.height
                    }
                }

                offset = temp
            }
        }

        if (finalWidth == -1 && finalHeight == -1) {
            return null
        }

        return Size(finalWidth, finalHeight)
    }

}