package com.proxy.service.camera.base.callback

import android.graphics.Canvas
import android.view.MotionEvent

/**
 * @author: cangHX
 * @data: 2026/2/11 11:10
 * @desc:
 */
abstract class CustomTouchDispatch : ITouchDispatch {

    /**
     * 按下
     * */
    override fun onViewDown(e: MotionEvent) {

    }

    /**
     * 缩放
     * */
    override fun onViewScale(scale: Float, focusX: Float, focusY: Float) {}

    /**
     * 滑动
     * */
    override fun onViewScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ) {
    }

    /**
     * 单击
     * */
    override fun onViewSingleTap(e: MotionEvent) {}

    /**
     * 双击
     * */
    override fun onViewDoubleTap(e: MotionEvent) {}

    /**
     * 长按
     * */
    override fun onViewLongPress(e: MotionEvent) {}

    /**
     * 绘制
     * */
    override fun onViewDraw(canvas: Canvas) {}

}