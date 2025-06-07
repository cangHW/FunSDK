package com.proxy.service.document.base.image.callback.base

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF

/**
 * @author: cangHX
 * @data: 2025/6/2 18:55
 * @desc:
 */
interface OnDrawCallback {

    /**
     * 绘制回调
     *
     * @param bitmapRect    bitmap 原始信息
     * @param matrix        矩阵
     * @param canvas        画布
     * @param paint         画笔
     * @param width         视图宽度
     * @param height        视图高度
     * */
    fun onDraw(
        bitmapRect: RectF,
        matrix: Matrix,
        canvas: Canvas,
        paint: Paint,
        width: Int,
        height: Int
    )

}