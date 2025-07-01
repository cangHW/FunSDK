package com.proxy.service.document.image.base.mode

import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.mode.builder.IBaseImageMoveAndScaleModeBuilder
import com.proxy.service.document.image.base.mode.builder.ICropFrameMoveAndScaleModeBuilder
import com.proxy.service.document.image.base.mode.builder.ICropFrameMoveModeBuilder

/**
 * @author: cangHX
 * @data: 2025/6/25 20:50
 * @desc:
 */
open class CropMode private constructor() {

    companion object {
        /**
         * 创建底图移动与缩放模式
         * */
        fun builderBaseImageMoveAndScaleMode(): IBaseImageMoveAndScaleModeBuilder {
            return BaseImageMoveAndScaleMode()
        }

        /**
         * 创建裁剪框移动模式
         * */
        fun builderCropFrameMoveMode(): ICropFrameMoveModeBuilder {
            return CropFrameMoveMode()
        }

        /**
         * 创建裁剪框移动与缩放模式
         * */
        fun builderCropFrameMoveAndScaleMode(): ICropFrameMoveAndScaleModeBuilder {
            return CropFrameMoveAndScaleMode()
        }
    }

    class BaseImageMoveAndScaleMode : CropMode(), IBaseImageMoveAndScaleModeBuilder,
        IBaseImageMoveAndScaleModeBuilder.IBaseImageMoveModeBuilderGet {

        private var maxScale = ImageConstants.DEFAULT_MAX_SCALE

        override fun setMaxScale(maxScale: Float): IBaseImageMoveAndScaleModeBuilder {
            this.maxScale = maxScale
            return this
        }

        override fun build(): CropMode {
            return this
        }

        override fun getMaxScale(): Float {
            return maxScale
        }
    }

    class CropFrameMoveMode : CropMode(), ICropFrameMoveModeBuilder {
        override fun build(): CropMode {
            return this
        }
    }

    class CropFrameMoveAndScaleMode : CropMode(), ICropFrameMoveAndScaleModeBuilder,
        ICropFrameMoveAndScaleModeBuilder.ICropFrameMoveAndScaleModeBuilderGet {

        private var cropFrameMinWidth = ImageConstants.DEFAULT_CROP_FRAME_MIN_SIZE
        private var cropFrameMinHeight = ImageConstants.DEFAULT_CROP_FRAME_MIN_SIZE

        private var isCropFrameCenterDraggable = ImageConstants.DEFAULT_CROP_FRAME_CENTER_DRAGGABLE
        private var isCropFrameCornerDraggable = ImageConstants.DEFAULT_CROP_FRAME_CORNER_DRAGGABLE
        private var isCropFrameEdgeDraggable = ImageConstants.DEFAULT_CROP_FRAME_EDGE_DRAGGABLE

        private var isCropFrameAspectRatioLock = ImageConstants.DEFAULT_CROP_FRAME_ASPECT_RATIO_LOCK

        override fun setCropFrameMinSize(
            minWidthPx: Float,
            minHeightPx: Float
        ): ICropFrameMoveAndScaleModeBuilder {
            this.cropFrameMinWidth = minWidthPx
            this.cropFrameMinHeight = minHeightPx
            return this
        }

        override fun setCropFrameCenterDraggable(isDraggable: Boolean): ICropFrameMoveAndScaleModeBuilder {
            this.isCropFrameCenterDraggable = isDraggable
            return this
        }

        override fun setCropFrameCornerDraggable(isDraggable: Boolean): ICropFrameMoveAndScaleModeBuilder {
            this.isCropFrameCornerDraggable = isDraggable
            return this
        }

        override fun setCropFrameEdgeDraggable(isDraggable: Boolean): ICropFrameMoveAndScaleModeBuilder {
            this.isCropFrameEdgeDraggable = isDraggable
            return this
        }

        override fun setCropFrameAspectRatioLock(isLocked: Boolean): ICropFrameMoveAndScaleModeBuilder {
            this.isCropFrameAspectRatioLock = isLocked
            return this
        }

        override fun build(): CropMode {
            return this
        }

        override fun getCropFrameMinWidth(): Float {
            return cropFrameMinWidth
        }

        override fun getCropFrameMinHeight(): Float {
            return cropFrameMinHeight
        }

        override fun isCropFrameCenterDraggable(): Boolean {
            return isCropFrameCenterDraggable
        }

        override fun isCropFrameCornerDraggable(): Boolean {
            return isCropFrameCornerDraggable
        }

        override fun isCropFrameEdgeDraggable(): Boolean {
            return isCropFrameEdgeDraggable
        }

        override fun isCropFrameAspectRatioLock(): Boolean {
            return isCropFrameAspectRatioLock
        }
    }

}