package com.proxy.service.camera.info.view.touch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.proxy.service.camera.base.callback.ITouchDispatch

/**
 * @author: cangHX
 * @data: 2026/2/11 10:10
 * @desc:
 */
abstract class BaseTouchView : View, ITouchDispatch {

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var gestureDetector: GestureDetector? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        scaleGestureDetector = ScaleGestureDetector(context, scaleGesture)
        gestureDetector = GestureDetector(context, gesture)
    }

    private val scaleGesture = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            onViewScale(detector.scaleFactor, detector.focusX, detector.focusY)
            return true
        }
    }

    private val gesture = object : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            onViewDown(e)
            return super.onDown(e)
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            onViewScroll(e1, e2, distanceX, distanceY)
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            onViewSingleTap(e)
            return super.onSingleTapConfirmed(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            onViewDoubleTap(e)
            return super.onDoubleTap(e)
        }

        override fun onLongPress(e: MotionEvent) {
            onViewLongPress(e)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            scaleGestureDetector?.onTouchEvent(it)
            gestureDetector?.onTouchEvent(it)
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        onViewDraw(canvas)
    }
}