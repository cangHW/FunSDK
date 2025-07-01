package com.proxy.service.document.image.base.mode.builder

import com.proxy.service.document.image.base.mode.CropMode
import com.proxy.service.document.image.base.constants.ImageConstants

/**
 * @author: cangHX
 * @data: 2025/6/26 09:49
 * @desc:
 */
interface ICropFrameMoveAndScaleModeBuilder {

    /**
     * 设置裁剪区域最小宽高, 默认: [ImageConstants.DEFAULT_CROP_FRAME_MIN_SIZE]
     * */
    fun setCropFrameMinSize(minWidthPx: Float, minHeightPx: Float): ICropFrameMoveAndScaleModeBuilder

    /**
     * 设置裁剪框中心是否允许拖动, 默认: [ImageConstants.DEFAULT_CROP_FRAME_CENTER_DRAGGABLE]
     * */
    fun setCropFrameCenterDraggable(isDraggable: Boolean): ICropFrameMoveAndScaleModeBuilder

    /**
     * 设置裁剪框四个角是否允许拖动, 默认: [ImageConstants.DEFAULT_CROP_FRAME_CORNER_DRAGGABLE]
     * */
    fun setCropFrameCornerDraggable(isDraggable: Boolean): ICropFrameMoveAndScaleModeBuilder

    /**
     * 设置裁剪框四个边是否允许拖动, 默认: [ImageConstants.DEFAULT_CROP_FRAME_EDGE_DRAGGABLE]
     * */
    fun setCropFrameEdgeDraggable(isDraggable: Boolean): ICropFrameMoveAndScaleModeBuilder

    /**
     * 设置裁剪框是否锁定宽高比, 默认: [ImageConstants.DEFAULT_CROP_FRAME_ASPECT_RATIO_LOCK]
     * */
    fun setCropFrameAspectRatioLock(isLocked: Boolean): ICropFrameMoveAndScaleModeBuilder

    /**
     * 创建模式
     * */
    fun build(): CropMode


    interface ICropFrameMoveAndScaleModeBuilderGet {
        fun getCropFrameMinWidth(): Float
        fun getCropFrameMinHeight(): Float

        fun isCropFrameCenterDraggable(): Boolean
        fun isCropFrameCornerDraggable(): Boolean
        fun isCropFrameEdgeDraggable(): Boolean

        fun isCropFrameAspectRatioLock(): Boolean
    }

}