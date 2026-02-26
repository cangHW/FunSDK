package com.proxy.service.camera.info.view.touch

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import com.proxy.service.camera.base.callback.ITouchDispatch
import com.proxy.service.camera.info.view.touch.mode.AfModeDispatch
import com.proxy.service.core.service.task.CsTask

/**
 * @author: cangHX
 * @data: 2026/2/8 15:59
 * @desc:
 */
class CameraTouchView : BaseTouchView {

    private val handler = CsTask.launchTaskGroup("Media-Camera-TouchView")

    private var touchDispatch: ITouchDispatch? = null

    private val afModeIntercept = AfModeDispatch(this, handler)


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun setCustomTouchDispatch(touchDispatch: ITouchDispatch?) {
        this.touchDispatch = touchDispatch
    }

    fun setOnCameraAfIntercept(intercept: AfModeDispatch.OnCameraAfIntercept) {
        afModeIntercept.setOnCameraAfIntercept(intercept)
    }


    override fun onViewDown(e: MotionEvent) {
        touchDispatch?.onViewDown(e)
    }

    override fun onViewScale(scale: Float, focusX: Float, focusY: Float) {
        touchDispatch?.onViewScale(scale, focusX, focusY)
    }

    override fun onViewScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ) {
        touchDispatch?.onViewScroll(e1, e2, distanceX, distanceY)
    }

    override fun onViewSingleTap(e: MotionEvent) {
        touchDispatch?.onViewSingleTap(e)
        afModeIntercept.onViewSingleTap(e)
    }

    override fun onViewDoubleTap(e: MotionEvent) {
        touchDispatch?.onViewDoubleTap(e)
    }

    override fun onViewLongPress(e: MotionEvent) {
        touchDispatch?.onViewLongPress(e)
        afModeIntercept.onViewLongPress(e)
    }

    override fun onViewDraw(canvas: Canvas) {
        touchDispatch?.onViewDraw(canvas)
        afModeIntercept.onViewDraw(canvas)
    }

}