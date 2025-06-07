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

    private val touchCallback = object : TouchManager.OnTouchCallback {
        override fun onTouchDown(event: MotionEvent) {
            animator.cancelAnim()
        }

        override fun onTouchUp(event: MotionEvent) {
            val lockRect = config.lockRect ?: return

            if (!mMatrix.mapRect(destRectF, mSrcRectF)) {
                return
            }

            val dragInfo = if (config.canDragInLockRect) {
                checkLockRectFByCanDrag(lockRect, destRectF)
            } else {
                checkLockRectFByNotDrag(lockRect, destRectF)
            } ?: return

            animator.startAnim {
                val distanceX = if (destRectF.left < lockRect.left) {
                    dragInfo.offsetX * it
                } else {
                    -1 * dragInfo.offsetX * it
                }
                val distanceY = if (destRectF.top < lockRect.top) {
                    dragInfo.offsetY * it
                } else {
                    -1 * dragInfo.offsetY * it
                }
                mMatrix.postTranslate(distanceX, distanceY)
                invalidateSelf()
            }
        }

        override fun onScale(scale: Float, focusX: Float, focusY: Float) {
            if (!callScale(mCurrentScale, scale, focusX, focusY)) {
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
            }
            callScaleEnd()
            invalidateSelf()
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ) {
            if (!callDrag(e1, e2, distanceX, distanceY)) {
                mMatrix.postTranslate(-distanceX, -distanceY)
            }
            callDragEnd()
            invalidateSelf()
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

        if (!callBoundChanged(left, top, right, bottom)) {
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

            config.lockRect?.let {
                if (mMatrix.mapRect(destRectF, mSrcRectF)) {
                    val dragInfo = if (config.canDragInLockRect) {
                        checkLockRectFByCanDrag(it, destRectF)
                    } else {
                        checkLockRectFByNotDrag(it, destRectF)
                    }
                    if (dragInfo != null) {
                        val distanceX = if (destRectF.left < it.left) {
                            dragInfo.offsetX
                        } else {
                            -1 * dragInfo.offsetX
                        }
                        val distanceY = if (destRectF.top < it.top) {
                            dragInfo.offsetY
                        } else {
                            -1 * dragInfo.offsetY
                        }
                        mMatrix.postTranslate(distanceX, distanceY)
                    }
                }
            }
        }
        callBoundChangedEnd()
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


    private data class DragInfo(val offsetX: Float, val offsetY: Float)

    private fun checkLockRectFByCanDrag(lockRect: RectF, currentRectF: RectF): DragInfo? {
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

        return DragInfo(offsetX, offsetY)
    }

    private fun checkLockRectFByNotDrag(lockRect: RectF, currentRectF: RectF): DragInfo {
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

        return DragInfo(offsetX, offsetY)
    }
}