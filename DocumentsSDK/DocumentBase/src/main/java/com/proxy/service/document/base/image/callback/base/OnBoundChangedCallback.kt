package com.proxy.service.document.base.image.callback.base

import android.graphics.Matrix
import android.graphics.RectF

/**
 * @author: cangHX
 * @data: 2025/6/2 18:37
 * @desc:
 */
interface OnBoundChangedCallback {

    /**
     * 显示区域发生变化回调
     *
     * @param bitmapRect    bitmap 原始信息
     * @param matrix        矩阵
     * @param left          新区域左侧坐标消息
     * @param top           新区域顶部坐标消息
     * @param right         新区域右侧坐标消息
     * @param bottom        新区域底部坐标消息
     *
     * @return 为 true 表示已经自行处理这次变化, false 仍然会执行原定逻辑
     * */
    fun onBoundChanged(
        bitmapRect: RectF,
        matrix: Matrix,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
    ): Boolean {
        return false
    }

    /**
     * 显示区域变化结束
     *
     * @param bitmapRect    bitmap 原始信息
     * @param matrix        矩阵
     * */
    fun onBoundChangedEnd(
        bitmapRect: RectF,
        matrix: Matrix
    ) {
    }
}