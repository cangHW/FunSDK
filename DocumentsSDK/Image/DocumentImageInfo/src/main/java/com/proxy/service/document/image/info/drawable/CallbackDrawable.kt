package com.proxy.service.document.image.info.drawable

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
            val count = canvas.saveLayer(
                0f,
                0f,
                bounds.width() * 1f,
                bounds.height() * 1f,
                null
            )
            it.onDraw(mSrcRectF, mMatrix, canvas, mPaint, bounds.width(), bounds.height())
            canvas.restoreToCount(count)
        }
    }

    protected fun callBoundChanged(left: Int, top: Int, right: Int, bottom: Int): Boolean {
        return config.boundChangedCallback?.onBoundChanged(
            mSrcRectF,
            mMatrix,
            left,
            top,
            right,
            bottom
        ) == true
    }

    protected fun callBoundChangedEnd() {
        config.boundChangedCallback?.onBoundChangedEnd(mSrcRectF, mMatrix)
    }

    protected fun callScale(
        currentScale: Float,
        scale: Float,
        focusX: Float,
        focusY: Float
    ): Boolean {
        return config.scaleCallback?.onScale(
            mSrcRectF,
            mMatrix,
            currentScale,
            scale,
            focusX,
            focusY
        ) == true
    }

    protected fun callScaleEnd() {
        config.scaleCallback?.onScaleEnd(mSrcRectF, mMatrix)
    }

    protected fun callDrag(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return config.dragCallback?.onDragged(
            mSrcRectF,
            mMatrix,
            e1,
            e2,
            distanceX,
            distanceY
        ) == true
    }

    protected fun callDragEnd() {
        config.dragCallback?.onDraggedEnd(mSrcRectF, mMatrix)
    }

    protected fun callSingleClick(event: MotionEvent) {
        config.singleClickCallback?.onSingleClick(event)
    }

    protected fun callDoubleClick(event: MotionEvent) {
        config.doubleClickCallback?.onDoubleClick(event)
    }

    protected fun callLongPress(event: MotionEvent) {
        config.longPressCallback?.onLongPress(event)
    }

}