package com.proxy.service.document.image.base.callback.crop

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF

/**
 * @author: cangHX
 * @data: 2025/6/7 14:01
 * @desc:
 */
interface OnDrawCropCallback {

    /**
     * 裁剪框绘制回调
     *
     * @param bitmapRect    bitmap 原始信息
     * @param matrix        矩阵
     * @param canvas        画布
     * @param paint         画笔
     * @param width         视图宽度
     * @param height        视图高度
     * @param maskColor     遮罩颜色
     * @param cropRect      裁剪区域
     * @param cropLineWidth 裁剪框线宽度
     * @param cropLineColor 裁剪框线颜色
     *
     * @return 为 true 表示已经自行处理这次变化, false 仍然会执行原定逻辑
     * */
    fun onDrawCrop(
        bitmapRect: RectF,
        matrix: Matrix,
        canvas: Canvas,
        paint: Paint,
        width: Int,
        height: Int,
        maskColor: Int,
        cropRect: RectF,
        cropLineWidth: Float,
        cropLineColor: Int
    ): Boolean

}