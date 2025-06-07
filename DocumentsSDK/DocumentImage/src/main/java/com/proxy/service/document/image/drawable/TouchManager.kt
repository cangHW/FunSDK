package com.proxy.service.document.image.drawable

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector

/**
 * @author: cangHX
 * @data: 2025/5/30 18:33
 * @desc:
 */
class TouchManager(
    context: Context,
    private val callback: OnTouchCallback
) {

    interface OnTouchCallback {
        fun onTouchDown(event: MotionEvent)
        fun onTouchUp(event: MotionEvent)
        fun onScale(scale: Float, focusX: Float, focusY: Float)
        fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float)
        fun onSingleClick(event: MotionEvent)
        fun onDoubleClick(event: MotionEvent)
        fun onLongPress(event: MotionEvent)
    }

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var gestureDetector: GestureDetector? = null

    private val scaleGestureListener =
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                callback.onScale(detector.scaleFactor, detector.focusX, detector.focusY)
                return true
            }
        }

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            callback.onScroll(e1, e2, distanceX, distanceY)
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            callback.onSingleClick(e)
            return super.onSingleTapConfirmed(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            callback.onDoubleClick(e)
            return super.onDoubleTap(e)
        }

        override fun onLongPress(e: MotionEvent) {
            super.onLongPress(e)
            callback.onLongPress(e)
        }
    }

    init {
        scaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener)
        gestureDetector = GestureDetector(context, gestureListener)
    }

    fun onTouchEvent(event: MotionEvent) {
        scaleGestureDetector?.onTouchEvent(event)
        gestureDetector?.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                callback.onTouchDown(event)
            }

            MotionEvent.ACTION_UP -> {
                callback.onTouchUp(event)
            }
        }
    }

}