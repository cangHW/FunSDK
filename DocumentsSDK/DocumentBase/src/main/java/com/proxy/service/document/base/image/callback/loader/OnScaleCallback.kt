package com.proxy.service.document.base.image.callback.loader

import android.graphics.Matrix
import android.graphics.Rect

/**
 * @author: cangHX
 * @data: 2025/6/2 18:55
 * @desc:
 */
interface OnScaleCallback {

    /**
     * 缩放回调
     *
     * @param bitmapRect    bitmap 原始信息
     * @param matrix        矩阵
     * @param currentScale  当前的缩放比例
     * @param scale         变化的缩放比例
     * @param centerX       缩放时的 X 轴中心点
     * @param centerY       缩放时的 Y 轴中心点
     *
     * @return 为 true 表示已经自行处理这次变化, false 仍然会执行原定逻辑
     * */
    fun onScale(
        bitmapRect: Rect,
        matrix: Matrix,
        currentScale: Float,
        scale: Float,
        centerX: Float,
        centerY: Float
    ): Boolean

}