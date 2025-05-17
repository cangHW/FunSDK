package com.proxy.service.document.pdf.view.touch

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector

/**
 * @author: cangHX
 * @data: 2025/5/14 14:18
 * @desc:
 */
class TouchManager private constructor(
    context: Context,
    private val callback: Callback
) {

    companion object {
        fun create(context: Context, callback: Callback): TouchManager {
            return TouchManager(context, callback)
        }
    }

    interface Callback {
        /**
         * 单击
         * */
        fun onSingleClick(x: Float, y: Float)

        /**
         * 双击
         * */
        fun onDoubleClick(x: Float, y: Float)

        /**
         * 长按
         * */
        fun onLongClick(x: Float, y: Float)

        /**
         * 缩放
         * */
        fun onScale(scale: Float)
    }

    private var scaleGestureDetector: ScaleGestureDetector? = null

    private val scaleGestureListener = object : SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            callback.onScale(detector.scaleFactor)
            return true
        }
    }

    private var gestureDetector: GestureDetector? = null

    private val gestureDetectorListener = object : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            callback.onSingleClick(e.x, e.y)
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            callback.onDoubleClick(e.x, e.y)
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            callback.onLongClick(e.x, e.y)
        }

    }

    init {
        gestureDetector = GestureDetector(context, gestureDetectorListener)
        scaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener)
    }


    fun onTouch(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        if (gestureDetector?.onTouchEvent(event) == true) {
            return true
        }
        return scaleGestureDetector?.onTouchEvent(event) == true
    }
}