package com.proxy.service.document.image.base.mode.builder

import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.mode.CropMode

/**
 * @author: cangHX
 * @date: 2025/6/26 09:48
 * @desc:
 */
interface IImageMoveAndScaleModeBuilder {

    /**
     * 设置缩放倍数
     *
     * @param maxScale  最大缩放倍数, 默认: [ImageConstants.DEFAULT_MAX_SCALE], 不能小于 1.
     * */
    fun setMaxScale(maxScale: Float): IImageMoveAndScaleModeBuilder

    /**
     * 创建模式
     * */
    fun build(): CropMode

    interface IImageMoveModeBuilderGet {
        fun getMaxScale(): Float
    }
}