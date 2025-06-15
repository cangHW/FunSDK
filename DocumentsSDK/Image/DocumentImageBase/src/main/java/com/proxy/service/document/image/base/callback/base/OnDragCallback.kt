package com.proxy.service.document.image.base.callback.base

import android.graphics.Matrix
import android.graphics.RectF
import android.view.MotionEvent

/**
 * @author: cangHX
 * @data: 2025/6/2 18:56
 * @desc:
 */
interface OnDragCallback {

    /**
     * 拖动回调
     *
     * @param bitmapRect    bitmap 原始信息
     * @param matrix        矩阵
     * @param oldEvent      变化之前的 MotionEvent
     * @param newEvent      变化之后的 MotionEvent
     * @param distanceX     拖动前后 X 轴变化距离
     * @param distanceY     拖动前后 Y 轴变化距离
     *
     * @return 为 true 表示已经自行处理这次变化, false 仍然会执行原定逻辑
     * */
    fun onDragged(
        bitmapRect: RectF,
        matrix: Matrix,
        oldEvent: MotionEvent?,
        newEvent: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    /**
     * 拖动结束, 刷新之前回调
     *
     * @param bitmapRect    bitmap 原始信息
     * @param matrix        矩阵
     * */
    fun onDraggedEnd(
        bitmapRect: RectF,
        matrix: Matrix
    ) {
    }

}