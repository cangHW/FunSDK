package com.proxy.service.document.image.drawable

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.MotionEvent

/**
 * @author: cangHX
 * @data: 2025/6/2 18:29
 * @desc:
 */
open class CallbackDrawable(
    bitmap: Bitmap,
    private val config: ConfigInfo
) : ImageDrawable(bitmap) {

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        config.drawCallback?.let {
            val saveCount = canvas.saveLayer(
                0f,
                0f,
                bounds.width() * 1f,
                bounds.height() * 1f,
                null
            )
            it.onDraw(srcRectF, matrix, canvas, paint)
            canvas.restoreToCount(saveCount)
        }
    }

    protected fun callBoundChanged(left: Int, top: Int, right: Int, bottom: Int): Boolean {
        return config.boundChangedCallback?.onBoundChanged(
            srcRectF,
            matrix,
            left,
            top,
            right,
            bottom
        ) == true
    }

    protected fun callScale(
        currentScale: Float,
        scale: Float,
        focusX: Float,
        focusY: Float
    ): Boolean {
        return config.scaleCallback?.onScale(
            srcRectF,
            matrix,
            currentScale,
            scale,
            focusX,
            focusY
        ) == true
    }

    protected fun callDrag(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return config.dragCallback?.onDragged(
            srcRectF,
            matrix,
            e1,
            e2,
            distanceX,
            distanceY
        ) == true
    }
}