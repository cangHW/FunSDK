package com.proxy.service.document.image.base.callback.base

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF

/**
 * 图片预览绘制回调.
 *
 * @author: cangHX
 * @data: 2025/6/2 18:55
 * @desc:
 */
interface OnDrawCallback {

    /**
     * 绘制回调
     *
     * @param bitmapRect    bitmap 原始区域
     * @param matrix        当前显示矩阵
     * @param canvas        画布
     * @param paint         画笔
     * @param width         视图宽度, View 坐标系
     * @param height        视图高度, View 坐标系
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