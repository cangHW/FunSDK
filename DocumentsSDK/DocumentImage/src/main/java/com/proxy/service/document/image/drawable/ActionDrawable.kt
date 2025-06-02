package com.proxy.service.document.image.drawable

import android.content.Context
import android.graphics.Bitmap
import android.view.MotionEvent

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

    private var currentScale = 1f

    private val touchCallback = object : TouchManager.OnTouchCallback {

        override fun onScale(scale: Float, focusX: Float, focusY: Float) {
            try {
                if (callScale(currentScale, scale, focusX, focusY)) {
                    return
                }
                var realScale = scale
                val newScale = currentScale * realScale

                if (newScale < config.minScale) {
                    realScale = config.minScale / currentScale
                    currentScale = config.minScale
                } else if (newScale > config.maxScale) {
                    realScale = config.maxScale / currentScale
                    currentScale = config.maxScale
                } else {
                    currentScale = newScale
                }
                matrix.postScale(realScale, realScale, focusX, focusY)
            } finally {
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
                matrix.postTranslate(-distanceX, -distanceY)
            } finally {
                invalidateSelf()
            }
        }
    }

    private var touchManager: TouchManager? = null

    init {
        touchManager = TouchManager(context, touchCallback)
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        matrix.reset()
        currentScale = 1f

        if (callBoundChanged(left, top, right, bottom)) {
            return
        }
        val viewWidth = right - left
        val viewHeight = bottom - top

        var bitmapWidth = srcRectF.width().toFloat()
        var bitmapHeight = srcRectF.height().toFloat()

        if (bitmapWidth > viewWidth || bitmapHeight > viewHeight) {
            val scaleX = viewWidth / bitmapWidth
            val scaleY = viewHeight / bitmapHeight

            val scale = minOf(scaleX, scaleY)
            matrix.postScale(scale, scale)

            bitmapWidth *= scale
            bitmapHeight *= scale
        }

        val dx = (viewWidth - bitmapWidth) / 2f
        val dy = (viewHeight - bitmapHeight) / 2f
        matrix.postTranslate(dx, dy)
    }

    fun onTouchEvent(event: MotionEvent) {
        touchManager?.onTouchEvent(event)
    }

}