package com.proxy.service.document.image.base.mode

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.mode.builder.IImageMoveAndScaleModeBuilder
import com.proxy.service.document.image.base.mode.builder.ICropFrameMoveAndScaleModeBuilder
import com.proxy.service.document.image.base.mode.builder.ICropFrameMoveModeBuilder

/**
 * 图片裁剪交互模式.
 *
 * 1. [ImageMoveAndScaleMode]: 裁剪框固定, 用户移动和缩放图片.
 * 2. [CropFrameMoveMode]: 图片固定, 用户移动裁剪框.
 * 3. [CropFrameMoveAndScaleMode]: 图片固定, 用户移动和缩放裁剪框.
 *
 * @author: cangHX
 * @date: 2025/6/25 20:50
 * @desc:
 */
open class CropMode private constructor() {

    companion object {
        private const val TAG = "${ImageConstants.LOG_TAG_IMAGE_START}CropMode"

        /**
         * 创建图片移动与缩放模式.
         *
         * 该模式下裁剪框固定, 用户通过移动和缩放图片调整裁剪内容.
         * */
        fun builderImageMoveAndScaleMode(): IImageMoveAndScaleModeBuilder {
            return ImageMoveAndScaleMode()
        }

        /**
         * 创建裁剪框移动模式
         *
         * 该模式下图片固定, 用户通过移动裁剪框调整裁剪内容.
         * */
        fun builderCropFrameMoveMode(): ICropFrameMoveModeBuilder {
            return CropFrameMoveMode()
        }

        /**
         * 创建裁剪框移动与缩放模式
         *
         * 该模式下图片固定, 用户通过移动和缩放裁剪框调整裁剪内容.
         * */
        fun builderCropFrameMoveAndScaleMode(): ICropFrameMoveAndScaleModeBuilder {
            return CropFrameMoveAndScaleMode()
        }
    }

    /**
     * 图片移动与缩放模式.
     *
     * 裁剪框固定, 用户通过移动和缩放图片调整裁剪内容.
     * */
    class ImageMoveAndScaleMode : CropMode(), IImageMoveAndScaleModeBuilder,
        IImageMoveAndScaleModeBuilder.IImageMoveModeBuilderGet {

        private var maxScale = ImageConstants.DEFAULT_MAX_SCALE

        override fun setMaxScale(maxScale: Float): IImageMoveAndScaleModeBuilder {
            if (maxScale < 1) {
                CsLogger.tag(TAG).e("The maxScale cannot be less than 1. maxScale = $maxScale")
                return this
            }
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

    /**
     * 裁剪框移动模式.
     *
     * 图片固定, 用户只能移动裁剪框.
     * */
    class CropFrameMoveMode : CropMode(), ICropFrameMoveModeBuilder {
        override fun build(): CropMode {
            return this
        }
    }

    /**
     * 裁剪框移动与缩放模式.
     *
     * 图片固定, 用户可以移动和缩放裁剪框.
     * */
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
            if (minWidthPx <= 0) {
                CsLogger.tag(TAG)
                    .e("The crop frame min width cannot be less than or equal to 0. minWidthPx = $minWidthPx")
                return this
            }
            if (minHeightPx <= 0) {
                CsLogger.tag(TAG)
                    .e("The crop frame min height cannot be less than or equal to 0. minHeightPx = $minHeightPx")
                return this
            }
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