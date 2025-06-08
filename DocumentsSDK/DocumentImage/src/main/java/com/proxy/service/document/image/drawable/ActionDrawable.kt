package com.proxy.service.document.image.drawable

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.view.MotionEvent
import com.proxy.service.document.image.utils.AnimatorManager

/**
 * @author: cangHX
 * @data: 2025/6/2 18:24
 * @desc:
 */
class ActionDrawable(
    context: Context,
    bitmap: Bitmap,
    private val config: ConfigInfo
) : CallbackDrawable(bitmap, config) {

    companion object {
        private const val DISTANCE_X = "distance_type_x"
        private const val DISTANCE_Y = "distance_type_y"
    }

    private val touchCallback = object : TouchManager.OnTouchCallback {
        override fun onTouchDown(event: MotionEvent) {
            animator.cancelAnim()
        }

        override fun onTouchUp(event: MotionEvent) {
            if (!config.overScrollBounceEnabled) {
                return
            }

            getDistanceInfoByLockRect()?.let { info ->
                animator.startAnim { ratio ->
                    mMatrix.postTranslate(-info.distanceX * ratio, -info.distanceY * ratio)
                    invalidateSelf()
                }
            }
        }

        override fun onScale(scale: Float, focusX: Float, focusY: Float) {
            try {
                if (callScale(mCurrentScale, scale, focusX, focusY)) {
                    return
                }
                var realScale = scale
                val newScale = mCurrentScale * realScale

                if (newScale < config.minScale) {
                    realScale = config.minScale / mCurrentScale
                    mCurrentScale = config.minScale
                } else if (newScale > config.maxScale) {
                    realScale = config.maxScale / mCurrentScale
                    mCurrentScale = config.maxScale
                } else {
                    mCurrentScale = newScale
                }
                mMatrix.postScale(realScale, realScale, focusX, focusY)

                if (config.overScrollBounceEnabled) {
                    return
                }
                getDistanceInfoByLockRect()?.let {
                    mMatrix.postTranslate(-it.distanceX, -it.distanceY)
                }
            } finally {
                callScaleEnd()
                invalidateSelf()
            }
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ) {
            try {
                if (callDrag(e1, e2, distanceX, distanceY)) {
                    return
                }
                mMatrix.postTranslate(-distanceX, -distanceY)

                if (config.overScrollBounceEnabled) {
                    return
                }
                getDistanceInfoByLockRect()?.let {
                    mMatrix.postTranslate(-it.distanceX, -it.distanceY)
                }
            } finally {
                callDragEnd()
                invalidateSelf()
            }
        }

        override fun onSingleClick(event: MotionEvent) {
            callSingleClick(event)
        }

        override fun onDoubleClick(event: MotionEvent) {
            callDoubleClick(event)
        }

        override fun onLongPress(event: MotionEvent) {
            callLongPress(event)
        }
    }

    private val animator = AnimatorManager.create()
    private val destRectF = RectF(0f, 0f, 0f, 0f)
    private var touchManager: TouchManager? = null

    init {
        touchManager = TouchManager(context, touchCallback)
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        mMatrix.reset()
        mCurrentScale = 1f

        try {
            if (callBoundChanged(left, top, right, bottom)) {
                return
            }
            val viewWidth = right - left
            val viewHeight = bottom - top

            var bitmapWidth = mSrcRectF.width()
            var bitmapHeight = mSrcRectF.height()

            if (bitmapWidth > viewWidth || bitmapHeight > viewHeight) {
                val scaleX = viewWidth / bitmapWidth
                val scaleY = viewHeight / bitmapHeight

                val scale = minOf(scaleX, scaleY)
                mMatrix.postScale(scale, scale)

                bitmapWidth *= scale
                bitmapHeight *= scale
            }

            val dx = (viewWidth - bitmapWidth) / 2f
            val dy = (viewHeight - bitmapHeight) / 2f
            mMatrix.postTranslate(dx, dy)

            getDistanceInfoByLockRect()?.let {
                mMatrix.postTranslate(-it.distanceX, -it.distanceY)
            }
        } finally {
            callBoundChangedEnd()
        }
    }

    fun onTouchEvent(event: MotionEvent) {
        if (config.touchEventCallback?.onTouchEvent(event) != true) {
            touchManager?.onTouchEvent(event)
        }
    }

    fun setScale(scale: Float) {
        val focusX = bounds.left + bounds.width() / 2f
        val focusY = bounds.top + bounds.height() / 2f
        setScale(scale, focusX, focusY)
    }

    fun setScale(scale: Float, focusX: Float, focusY: Float) {
        val offset = scale / mCurrentScale
        touchCallback.onScale(offset, focusX, focusY)
    }

    private data class DistanceInfo(val distanceX: Float, val distanceY: Float) {
        override fun toString(): String {
            return "DistanceInfo(distanceX=$distanceX, distanceY=$distanceY)"
        }
    }

    /**
     * 检查锁定区域并获取间距
     * */
    private fun getDistanceInfoByLockRect(): DistanceInfo? {
        val lockRect = config.lockRect ?: return null

        if (!mMatrix.mapRect(destRectF, mSrcRectF)) {
            return null
        }

        val moveInfo = if (config.canDragInLockRect) {
            getMoveInfoByLockRectWithMovable(lockRect, destRectF)
        } else {
            getMoveInfoByLockRectWithImmovable(lockRect, destRectF)
        } ?: return null

        val distanceX = getDistanceByMoveInfo(
            moveInfo,
            lockRect,
            destRectF,
            DISTANCE_X
        )
        val distanceY = getDistanceByMoveInfo(
            moveInfo,
            lockRect,
            destRectF,
            DISTANCE_Y
        )

        return DistanceInfo(distanceX, distanceY)
    }

    private data class MoveInfo(val offsetX: Float, val offsetY: Float) {
        override fun toString(): String {
            return "DragInfo(offsetX=$offsetX, offsetY=$offsetY)"
        }
    }

    /**
     * 通过移动信息获取距离
     * */
    private fun getDistanceByMoveInfo(
        dragInfo: MoveInfo,
        lockRect: RectF,
        currentRectF: RectF,
        distanceType: String
    ): Float {
        if (distanceType == DISTANCE_X) {
            return if (config.canDragInLockRect) {
                if (currentRectF.left < lockRect.left) {
                    -1 * dragInfo.offsetX
                } else {
                    dragInfo.offsetX
                }
            } else {
                if (currentRectF.centerX() < lockRect.centerX()) {
                    -1 * dragInfo.offsetX
                } else {
                    dragInfo.offsetX
                }
            }
        }

        if (distanceType == DISTANCE_Y) {
            return if (config.canDragInLockRect) {
                if (currentRectF.top < lockRect.top) {
                    -1 * dragInfo.offsetY
                } else {
                    dragInfo.offsetY
                }
            } else {
                if (currentRectF.centerY() < lockRect.centerY()) {
                    -1 * dragInfo.offsetY
                } else {
                    dragInfo.offsetY
                }
            }
        }

        return 0f
    }

    /**
     * 通过锁定区域获取移动信息, 锁定区域内可移动
     * */
    private fun getMoveInfoByLockRectWithMovable(lockRect: RectF, currentRectF: RectF): MoveInfo? {
        if (lockRect.contains(currentRectF)) {
            return null
        }

        var offsetLeft = 0f
        var offsetTop = 0f
        var offsetRight = 0f
        var offsetBottom = 0f

        if (currentRectF.width() < lockRect.width()) {
            if (currentRectF.left < lockRect.left) {
                offsetLeft = currentRectF.left - lockRect.left
            }
            if (currentRectF.right > lockRect.right) {
                offsetRight = currentRectF.right - lockRect.right
            }
        } else {
            if (currentRectF.left > lockRect.left) {
                offsetLeft = currentRectF.left - lockRect.left
            }
            if (currentRectF.right < lockRect.right) {
                offsetRight = currentRectF.right - lockRect.right
            }
        }

        if (currentRectF.height() < lockRect.height()) {
            if (currentRectF.top < lockRect.top) {
                offsetTop = currentRectF.top - lockRect.top
            }
            if (currentRectF.bottom > lockRect.bottom) {
                offsetBottom = currentRectF.bottom - lockRect.bottom
            }
        } else {
            if (currentRectF.top > lockRect.top) {
                offsetTop = currentRectF.top - lockRect.top
            }
            if (currentRectF.bottom < lockRect.bottom) {
                offsetBottom = currentRectF.bottom - lockRect.bottom
            }
        }

        val offsetX = if (offsetLeft != 0f && offsetRight != 0f) {
            Math.min(Math.abs(offsetLeft), Math.abs(offsetRight))
        } else if (offsetLeft != 0f) {
            Math.abs(offsetLeft)
        } else {
            Math.abs(offsetRight)
        }

        val offsetY = if (offsetTop != 0f && offsetBottom != 0f) {
            Math.min(Math.abs(offsetTop), Math.abs(offsetBottom))
        } else if (offsetTop != 0f) {
            Math.abs(offsetTop)
        } else {
            Math.abs(offsetBottom)
        }

        return MoveInfo(offsetX, offsetY)
    }

    /**
     * 通过锁定区域获取移动信息, 锁定区域内不可移动
     * */
    private fun getMoveInfoByLockRectWithImmovable(lockRect: RectF, currentRectF: RectF): MoveInfo {
        var offsetLeft = 0f
        var offsetTop = 0f
        var offsetRight = 0f
        var offsetBottom = 0f

        if (currentRectF.width() < lockRect.width()) {
            offsetLeft = currentRectF.centerX() - lockRect.centerX()
        } else {
            if (currentRectF.left > lockRect.left) {
                offsetLeft = currentRectF.left - lockRect.left
            }
            if (currentRectF.right < lockRect.right) {
                offsetRight = currentRectF.right - lockRect.right
            }
        }

        if (currentRectF.height() < lockRect.height()) {
            offsetTop = currentRectF.centerY() - lockRect.centerY()
        } else {
            if (currentRectF.top > lockRect.top) {
                offsetTop = currentRectF.top - lockRect.top
            }
            if (currentRectF.bottom < lockRect.bottom) {
                offsetBottom = currentRectF.bottom - lockRect.bottom
            }
        }

        val offsetX = if (offsetLeft != 0f && offsetRight != 0f) {
            Math.min(Math.abs(offsetLeft), Math.abs(offsetRight))
        } else if (offsetLeft != 0f) {
            Math.abs(offsetLeft)
        } else {
            Math.abs(offsetRight)
        }

        val offsetY = if (offsetTop != 0f && offsetBottom != 0f) {
            Math.min(Math.abs(offsetTop), Math.abs(offsetBottom))
        } else if (offsetTop != 0f) {
            Math.abs(offsetTop)
        } else {
            Math.abs(offsetBottom)
        }

        return MoveInfo(offsetX, offsetY)
    }
}