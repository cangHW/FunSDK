package com.proxy.service.document.base.image.loader.base

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * @author: cangHX
 * @data: 2025/5/30 10:23
 * @desc:
 */
interface IController {

    /**
     * 获取原图
     * */
    fun getBitmap(): Bitmap?

    /**
     * 获取矩阵
     * */
    fun getMatrix(): Matrix?

    /**
     * 获取当前缩放比例
     * */
    fun getCurrentScale(): Float

    /**
     * 设置缩放倍数, 缩放位置为视图中心
     *
     * @param scale     目标缩放倍数
     * */
    fun setScale(scale: Float)

    /**
     * 设置缩放倍数
     *
     * @param scale     目标缩放倍数
     * @param focusX    缩放 X 轴位置
     * @param focusY    缩放 Y 轴位置
     * */
    fun setScale(scale: Float, focusX: Float, focusY: Float)

}