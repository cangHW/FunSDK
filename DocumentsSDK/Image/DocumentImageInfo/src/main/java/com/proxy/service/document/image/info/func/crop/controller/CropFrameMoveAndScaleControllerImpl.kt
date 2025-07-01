package com.proxy.service.document.image.info.func.crop.controller

import android.graphics.Matrix
import android.graphics.RectF
import android.view.MotionEvent
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.image.base.callback.base.OnTouchEventCallback
import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.loader.base.IOption
import com.proxy.service.document.image.base.mode.CropMode
import com.proxy.service.document.image.info.func.crop.CropInfo
import com.proxy.service.document.image.info.utils.RectUtils

/**
 * @author: cangHX
 * @data: 2025/6/26 10:29
 * @desc:
 */
class CropFrameMoveAndScaleControllerImpl(
    private val option: IOption,
    info: CropInfo,
    private val mode: CropMode.CropFrameMoveAndScaleMode
) : BaseController(option, info), OnTouchEventCallback {

    companion object {
        private const val TAG = "${ImageConstants.LOG_TAG_IMAGE_START}CFMAS"

        private const val TYPE_LT = "lt"
        private const val TYPE_RT = "rt"
        private const val TYPE_RB = "rb"
        private const val TYPE_LB = "lb"
    }

    private val limitRectF = RectF()
    private var touchOffset = ImageConstants.DEFAULT_CROP_TOUCH_OFFSET_WIDTH * 1f
    private var aspectRatio = 1f

    override fun init() {
        super.init()
        option.setScale(1f, 1f)
        option.setTouchEventCallback(this)

        val minWH = Math.min(mode.getCropFrameMinWidth(), mode.getCropFrameMinHeight())
        touchOffset = Math.min(minWH / 3, touchOffset)
        CsLogger.tag(TAG).d("touchOffset = $touchOffset")
    }

    override fun onBoundChanged(
        bitmapRect: RectF,
        matrix: Matrix,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ): Boolean {
        super.onBoundChanged(bitmapRect, matrix, left, top, right, bottom)
        aspectRatio = cropRect.width() / cropRect.height()
        RectUtils.fitRect(bitmapRect, limitRectF, matrix, offset, left, top, right, bottom)
        if (limitRectF.contains(cropRect)) {
            CsLogger.tag(TAG).i("limitRectF=$limitRectF")
        } else {
            CsLogger.tag(TAG)
                .e("Special pictures cannot be cropped. BitmapRect=$limitRectF, CropRect=$cropRect")
        }
        return true
    }

    private var isCenterMove = false

    private var isLtCornerMove = false
    private var isRtCornerMove = false
    private var isRbCornerMove = false
    private var isLbCornerMove = false

    private var isLeftEdgeMove = false
    private var isTopEdgeMove = false
    private var isRightEdgeMove = false
    private var isBottomEdgeMove = false

    private var downX = 0f
    private var downY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (limitRectF.contains(cropRect)) {
                    downX = event.x
                    downY = event.y
                    checkTouchPoint(downX, downY)
                } else {
                    CsLogger.tag(TAG)
                        .e("Special pictures cannot be cropped. BitmapRect=$limitRectF, CropRect=$cropRect")
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (downX >= 0f && downY >= 0f) {
                    if (isCenterMove) {
                        centerMove(event.x - downX, event.y - downY)
                    } else if (isLtCornerMove) {
                        ltCornerMove(event.x - downX, event.y - downY)
                    } else if (isRtCornerMove) {
                        rtCornerMove(event.x - downX, event.y - downY)
                    } else if (isRbCornerMove) {
                        rbCornerMove(event.x - downX, event.y - downY)
                    } else if (isLbCornerMove) {
                        lbCornerMove(event.x - downX, event.y - downY)
                    } else if (isLeftEdgeMove) {
                        leftEdgeMove(event.x - downX)
                    } else if (isTopEdgeMove) {
                        topEdgeMove(event.y - downY)
                    } else if (isRightEdgeMove) {
                        rightEdgeMove(event.x - downX)
                    } else if (isBottomEdgeMove) {
                        bottomEdgeMove(event.y - downY)
                    }
                }
                downX = event.x
                downY = event.y
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isCenterMove = false

                isLtCornerMove = false
                isRtCornerMove = false
                isRbCornerMove = false
                isLbCornerMove = false

                isLeftEdgeMove = false
                isTopEdgeMove = false
                isRightEdgeMove = false
                isBottomEdgeMove = false
            }
        }
        return true
    }

    private val temp = RectF()
    private fun checkTouchPoint(eventX: Float, eventY: Float) {
        if (mode.isCropFrameCenterDraggable()) {
            temp.set(cropRect)
            temp.inset(touchOffset, touchOffset)
            isCenterMove = temp.contains(eventX, eventY)
        }

        if (mode.isCropFrameEdgeDraggable()) {
            // 左上
            temp.set(cropRect.left, cropRect.top, cropRect.left, cropRect.top)
            temp.inset(-touchOffset, -touchOffset)
            isLtCornerMove = temp.contains(eventX, eventY)
            // 右上
            temp.set(cropRect.right, cropRect.top, cropRect.right, cropRect.top)
            temp.inset(-touchOffset, -touchOffset)
            isRtCornerMove = temp.contains(eventX, eventY)
            // 右下
            temp.set(cropRect.right, cropRect.bottom, cropRect.right, cropRect.bottom)
            temp.inset(-touchOffset, -touchOffset)
            isRbCornerMove = temp.contains(eventX, eventY)
            // 左下
            temp.set(cropRect.left, cropRect.bottom, cropRect.left, cropRect.bottom)
            temp.inset(-touchOffset, -touchOffset)
            isLbCornerMove = temp.contains(eventX, eventY)
        }

        if (mode.isCropFrameEdgeDraggable()) {
            // 左
            temp.set(
                cropRect.left - touchOffset,
                cropRect.top + touchOffset,
                cropRect.left + touchOffset,
                cropRect.bottom - touchOffset
            )
            isLeftEdgeMove = temp.contains(eventX, eventY)
            // 上
            temp.set(
                cropRect.left + touchOffset,
                cropRect.top - touchOffset,
                cropRect.right - touchOffset,
                cropRect.top + touchOffset
            )
            isTopEdgeMove = temp.contains(eventX, eventY)
            // 右
            temp.set(
                cropRect.right - touchOffset,
                cropRect.top + touchOffset,
                cropRect.right + touchOffset,
                cropRect.bottom - touchOffset
            )
            isRightEdgeMove = temp.contains(eventX, eventY)
            // 下
            temp.set(
                cropRect.left + touchOffset,
                cropRect.bottom - touchOffset,
                cropRect.right - touchOffset,
                cropRect.bottom + touchOffset
            )
            isBottomEdgeMove = temp.contains(eventX, eventY)
        }
    }

    private fun centerMove(offsetX: Float, offsetY: Float) {
        cropRect.offset(offsetX, offsetY)
        val diffX = when {
            cropRect.left < limitRectF.left -> limitRectF.left - cropRect.left
            cropRect.right > limitRectF.right -> limitRectF.right - cropRect.right
            else -> 0f
        }
        val diffY = when {
            cropRect.top < limitRectF.top -> limitRectF.top - cropRect.top
            cropRect.bottom > limitRectF.bottom -> limitRectF.bottom - cropRect.bottom
            else -> 0f
        }
        cropRect.offset(diffX, diffY)

        CsLogger.tag(TAG)
            .d("centerMove offsetX=$offsetX, offsetY=$offsetY, moveX=${offsetX + diffX}, moveY=${offsetY + diffY}")

        refreshCropPath()
        previewController?.invalidate()
    }

    private fun ltCornerMove(offsetX: Float, offsetY: Float) {
        val offsets = adjustOffsets(offsetX, offsetY, TYPE_LT)
        val targetX = cropRect.left + offsets.first
        val targetY = cropRect.top + offsets.second

        CsLogger.tag(TAG)
            .d("ltCornerMove offsetX=$offsetX, offsetY=$offsetY, moveX=${offsets.first}, moveY=${offsets.second}")

        if (cropRect.left != targetX || cropRect.top != targetY) {
            cropRect.set(targetX, targetY, cropRect.right, cropRect.bottom)
            refreshCropPath()
            previewController?.invalidate()
        }
    }

    private fun rtCornerMove(offsetX: Float, offsetY: Float) {
        val offsets = adjustOffsets(offsetX, offsetY, TYPE_RT)
        val targetX = cropRect.right + offsets.first
        val targetY = cropRect.top + offsets.second

        CsLogger.tag(TAG)
            .d("rtCornerMove offsetX=$offsetX, offsetY=$offsetY, moveX=${offsets.first}, moveY=${offsets.second}")

        if (cropRect.right != targetX || cropRect.top != targetY) {
            cropRect.set(cropRect.left, targetY, targetX, cropRect.bottom)
            refreshCropPath()
            previewController?.invalidate()
        }
    }

    private fun rbCornerMove(offsetX: Float, offsetY: Float) {
        val offsets = adjustOffsets(offsetX, offsetY, TYPE_RB)
        val targetX = cropRect.right + offsets.first
        val targetY = cropRect.bottom + offsets.second

        CsLogger.tag(TAG)
            .d("rbCornerMove offsetX=$offsetX, offsetY=$offsetY, moveX=${offsets.first}, moveY=${offsets.second}")

        if (cropRect.right != targetX || cropRect.bottom != targetY) {
            cropRect.set(cropRect.left, cropRect.top, targetX, targetY)
            refreshCropPath()
            previewController?.invalidate()
        }
    }

    private fun lbCornerMove(offsetX: Float, offsetY: Float) {
        val offsets = adjustOffsets(offsetX, offsetY, TYPE_LB)
        val targetX = cropRect.left + offsets.first
        val targetY = cropRect.bottom + offsets.second

        CsLogger.tag(TAG)
            .d("lbCornerMove offsetX=$offsetX, offsetY=$offsetY, moveX=${offsets.first}, moveY=${offsets.second}")

        if (cropRect.left != targetX || cropRect.bottom != targetY) {
            cropRect.set(targetX, cropRect.top, cropRect.right, targetY)
            refreshCropPath()
            previewController?.invalidate()
        }
    }

    private fun leftEdgeMove(offsetX: Float) {
        val offsets = adjustOffsets(offsetX, 0f, TYPE_LB)
        val targetX = cropRect.left + offsets.first
        val targetY = cropRect.bottom + offsets.second

        CsLogger.tag(TAG)
            .d("leftEdgeMove offsetX=$offsetX, moveX=${offsets.first}, moveY=${offsets.second}")

        if (cropRect.left != targetX || cropRect.bottom != targetY) {
            cropRect.set(targetX, cropRect.top, cropRect.right, targetY)
            refreshCropPath()
            previewController?.invalidate()
        }
    }

    private fun topEdgeMove(offsetY: Float) {
        val offsets = adjustOffsets(0f, offsetY, TYPE_RT)
        val targetX = cropRect.right + offsets.first
        val targetY = cropRect.top + offsets.second

        CsLogger.tag(TAG)
            .d("topEdgeMove offsetY=$offsetY, moveX=${offsets.first}, moveY=${offsets.second}")

        if (cropRect.right != targetX || cropRect.top != targetY) {
            cropRect.set(cropRect.left, targetY, targetX, cropRect.bottom)
            refreshCropPath()
            previewController?.invalidate()
        }
    }

    private fun rightEdgeMove(offsetX: Float) {
        val offsets = adjustOffsets(offsetX, 0f, TYPE_RB)
        val targetX = cropRect.right + offsets.first
        val targetY = cropRect.bottom + offsets.second

        CsLogger.tag(TAG)
            .d("rightEdgeMove offsetX=$offsetX, moveX=${offsets.first}, moveY=${offsets.second}")

        if (cropRect.right != targetX || cropRect.bottom != targetY) {
            cropRect.set(cropRect.left, cropRect.top, targetX, targetY)
            refreshCropPath()
            previewController?.invalidate()
        }
    }

    private fun bottomEdgeMove(offsetY: Float) {
        val offsets = adjustOffsets(0f, offsetY, TYPE_RB)
        val targetX = cropRect.right + offsets.first
        val targetY = cropRect.bottom + offsets.second

        CsLogger.tag(TAG)
            .d("bottomEdgeMove offsetY=$offsetY, moveX=${offsets.first}, moveY=${offsets.second}")

        if (cropRect.right != targetX || cropRect.bottom != targetY) {
            cropRect.set(cropRect.left, cropRect.top, targetX, targetY)
            refreshCropPath()
            previewController?.invalidate()
        }
    }

    /**
     * 调整移动范围
     * */
    private fun adjustOffsets(
        offsetX: Float,
        offsetY: Float,
        type: String
    ): Pair<Float, Float> {
        return if (mode.isCropFrameAspectRatioLock()) {
            adjustOffsetsForAspectRatio(offsetX, offsetY, type)
        } else {
            adjustOffsetsForNormal(offsetX, offsetY, type)
        }
    }

    /**
     * 根据角类型按宽高比例限制移动范围
     * */
    private fun adjustOffsetsForAspectRatio(
        offsetX: Float,
        offsetY: Float,
        type: String
    ): Pair<Float, Float> {
        // 初始化移动值
        val init = adjustInit(offsetX, offsetY, type)
        var moveX = init.first
        var moveY = init.second

        if (moveX == 0f || moveY == 0f) {
            return Pair(0f, 0f)
        }

        // 根据角类型调整移动方向
        val direction = adjustDirection(offsetX, offsetY, type)
        moveX = Math.abs(moveX) * direction.first
        moveY = Math.abs(moveY) * direction.second

        // 限制移动范围
        val offsets = adjustOffsetsForNormal(moveX, moveY, type)
        moveX = offsets.first
        moveY = offsets.second

        if (moveX == 0f || moveY == 0f) {
            return Pair(0f, 0f)
        }

        // 宽高比例调整
        val tempX = Math.abs(moveY) * aspectRatio
        if (tempX > Math.abs(moveX)) {
            moveY = Math.abs(moveX) / aspectRatio
        } else {
            moveX = tempX
        }

        // 重新调整方向
        moveX = Math.abs(moveX) * direction.first
        moveY = Math.abs(moveY) * direction.second

        return Pair(moveX, moveY)
    }

    /**
     * 根据角类型限制移动范围
     * */
    private fun adjustOffsetsForNormal(
        offsetX: Float,
        offsetY: Float,
        type: String
    ): Pair<Float, Float> {
        var moveX = offsetX
        var moveY = offsetY

        if (type == TYPE_LT) {
            val moveXMax = cropRect.right - mode.getCropFrameMinWidth() - cropRect.left
            val moveXMin = limitRectF.left - cropRect.left
            if (moveX > moveXMax) {
                moveX = moveXMax
            } else if (moveX < moveXMin) {
                moveX = moveXMin
            }

            val moveYMax = cropRect.bottom - mode.getCropFrameMinHeight() - cropRect.top
            val moveYMin = limitRectF.top - cropRect.top
            if (moveY > moveYMax) {
                moveY = moveYMax
            } else if (moveY < moveYMin) {
                moveY = moveYMin
            }
        } else if (type == TYPE_RB) {
            val moveXMax = limitRectF.right - cropRect.right
            val moveXMin = cropRect.left + mode.getCropFrameMinWidth() - cropRect.right
            if (moveX > moveXMax) {
                moveX = moveXMax
            } else if (moveX < moveXMin) {
                moveX = moveXMin
            }

            val moveYMax = limitRectF.bottom - cropRect.bottom
            val moveYMin = cropRect.top + mode.getCropFrameMinHeight() - cropRect.bottom
            if (moveY > moveYMax) {
                moveY = moveYMax
            } else if (moveY < moveYMin) {
                moveY = moveYMin
            }
        } else if (type == TYPE_RT) {
            val moveXMax = limitRectF.right - cropRect.right
            val moveXMin = cropRect.left + mode.getCropFrameMinWidth() - cropRect.right
            if (moveX > moveXMax) {
                moveX = moveXMax
            } else if (moveX < moveXMin) {
                moveX = moveXMin
            }

            val moveYMax = cropRect.bottom - mode.getCropFrameMinHeight() - cropRect.top
            val moveYMin = limitRectF.top - cropRect.top
            if (moveY > moveYMax) {
                moveY = moveYMax
            } else if (moveY < moveYMin) {
                moveY = moveYMin
            }
        } else if (type == TYPE_LB) {
            val moveXMax = cropRect.right - mode.getCropFrameMinWidth() - cropRect.left
            val moveXMin = limitRectF.left - cropRect.left
            if (moveX > moveXMax) {
                moveX = moveXMax
            } else if (moveX < moveXMin) {
                moveX = moveXMin
            }

            val moveYMax = limitRectF.bottom - cropRect.bottom
            val moveYMin = cropRect.top + mode.getCropFrameMinHeight() - cropRect.bottom
            if (moveY > moveYMax) {
                moveY = moveYMax
            } else if (moveY < moveYMin) {
                moveY = moveYMin
            }
        }
        return Pair(moveX, moveY)
    }

    /**
     * 根据角类型初始化移动值
     * */
    private fun adjustInit(offsetX: Float, offsetY: Float, type: String): Pair<Float, Float> {
        val moveX = if (offsetX == 0f) {
            if (type == TYPE_LT || type == TYPE_RB) {
                offsetY
            } else {
                -offsetY
            }
        } else {
            offsetX
        }
        val moveY = if (offsetY == 0f) {
            if (type == TYPE_LT || type == TYPE_RB) {
                offsetX
            } else {
                -offsetX
            }
        } else {
            offsetY
        }
        return Pair(moveX, moveY)
    }

    /**
     * 根据角类型计算移动方向
     * */
    private fun adjustDirection(offsetX: Float, offsetY: Float, type: String): Pair<Int, Int> {
        var directionX = 1
        var directionY = 1
        if (type == TYPE_LT || type == TYPE_RB) {
            if (offsetX + offsetY <= 0) {
                directionX = -1
                directionY = -1
            } else {
                directionX = 1
                directionY = 1
            }
        } else if (type == TYPE_RT || type == TYPE_LB) {
            if (offsetX >= offsetY) {
                directionX = 1
                directionY = -1
            } else {
                directionX = -1
                directionY = 1
            }
        }
        return Pair(directionX, directionY)
    }
}