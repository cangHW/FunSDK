package com.proxy.service.camera.info.utils

import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.info.SupportSize
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils


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
        sizeList: List<SupportSize>,
        viewWidth: Int,
        viewHeight: Int
    ): SupportSize? {
        val aspectRatio = getAspectRatio(viewWidth, viewHeight)
        val type = getSupportSizeType(aspectRatio)

        CsLogger.tag(TAG)
            .d("calculateSize. viewWidth=$viewWidth, viewHeight=$viewHeight, aspectRatio=$aspectRatio")

        var offset = Float.MIN_VALUE
        var supportSize: SupportSize? = null

        for (size in sizeList) {
            if (size.type == type) {
                supportSize = size
                break
            }

            if (size.type < type && size.ratio > offset && size.ratio <= aspectRatio) {
                supportSize = size
                offset = size.ratio
            }
        }

        return supportSize
    }

    /**
     * 获取支持的尺寸类型 [SupportSize]
     * */
    fun getSupportSizeType(width: Int, height: Int): Int {
        val ratio = getAspectRatio(width, height)
        return getSupportSizeType(ratio)
    }

    private fun getAspectRatio(width: Int, height: Int): Float {
        return if (width > height) {
            width.toFloat() / height
        } else {
            height.toFloat() / width
        }
    }

    private fun getSupportSizeType(ratio: Float): Int {
        return if (ratio > SupportSize.RATIO_16_9) {
            SupportSize.TYPE_SIZE_16_9
        } else if (ratio > SupportSize.RATIO_4_3) {
            SupportSize.TYPE_SIZE_4_3
        } else if (ratio > SupportSize.RATIO_1_1) {
            SupportSize.TYPE_SIZE_1_1
        } else {
            SupportSize.TYPE_SIZE_UNKNOWN
        }
    }
}