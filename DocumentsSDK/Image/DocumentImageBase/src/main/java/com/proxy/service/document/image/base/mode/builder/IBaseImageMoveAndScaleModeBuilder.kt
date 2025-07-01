package com.proxy.service.document.image.base.mode.builder

import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.mode.CropMode

/**
 * @author: cangHX
 * @data: 2025/6/26 09:48
 * @desc:
 */
interface IBaseImageMoveAndScaleModeBuilder {

    /**
     * 设置缩放倍数
     *
     * @param maxScale  最小缩放倍数, 默认: [ImageConstants.DEFAULT_MAX_SCALE]
     * */
    fun setMaxScale(maxScale: Float): IBaseImageMoveAndScaleModeBuilder

    /**
     * 创建模式
     * */
    fun build(): CropMode

    interface IBaseImageMoveModeBuilderGet {
        fun getMaxScale(): Float
    }
}