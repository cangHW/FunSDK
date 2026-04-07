package com.proxy.service.camera.base.callback.view

import android.graphics.Canvas
import android.view.MotionEvent

/**
 * @author: cangHX
 * @data: 2026/2/11 11:10
 * @desc:
 */
interface ITouchDispatch {

    /**
     * 按下
     * */
    fun onViewDown(e: MotionEvent)

    /**
     * 缩放
     * */
    fun onViewScale(scale: Float, focusX: Float, focusY: Float)

    /**
     * 滑动
     * */
    fun onViewScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    )

    /**
     * 单击
     * */
    fun onViewSingleTap(e: MotionEvent)

    /**
     * 双击
     * */
    fun onViewDoubleTap(e: MotionEvent)

    /**
     * 长按
     * */
    fun onViewLongPress(e: MotionEvent)

    /**
     * 绘制
     * */
    fun onViewDraw(canvas: Canvas)

}