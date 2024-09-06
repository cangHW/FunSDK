package com.proxy.service.imageloader.base.option.glide

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.proxy.service.imageloader.base.loader.glide.IGlideLoader

/**
 * 配置管理器
 *
 * @author: cangHX
 * @data: 2024/5/16 09:47
 * @desc:
 */
interface IGlideOption<R> : IGlideLoader<R> {

    /**
     * 修改资源宽高 [0 - ]
     * */
    fun size(width: Int, height: Int): IGlideOption<R>

    /**
     * 占位图
     */
    fun placeholder(@DrawableRes placeholderId: Int): IGlideOption<R>

    /**
     * 加载错误占位图
     */
    fun error(@DrawableRes errorId: Int): IGlideOption<R>

    /**
     * 裁剪模式
     */
    fun centerCrop(): IGlideOption<R>

    /**
     * 裁剪模式
     */
    fun centerInside(): IGlideOption<R>

    /**
     * 缩放模式，维持宽高比
     */
    fun fitCenter(): IGlideOption<R>

    /**
     * 缩放模式，破坏宽高比
     */
    fun fitXY(): IGlideOption<R>

    /**
     * 圆形
     */
    fun circleCrop(): IGlideOption<R>

    /**
     * 圆角 [0 - ]
     */
    fun roundedCorners(roundingRadiusPx: Int): IGlideOption<R>

    /**
     * 圆角 [0 - ]
     */
    fun roundedCorners(
        tfRoundingRadiusPx: Float,
        trRoundingRadiusPx: Float,
        brRoundingRadiusPx: Float,
        blRoundingRadiusPx: Float
    ): IGlideOption<R>

    /**
     * 高斯模糊
     * @param radius    模糊的程度，值越大模糊效果越明显
     * @param scaling   缩放比例，值越大图越小，性能越好，但模糊效果可能会减轻
     */
    fun blur(radius: Int = 25, scaling: Int = 1): IGlideOption<R>

    /**
     * 颜色过滤
     * @param color 待过滤的颜色
     * */
    fun colorFilter(@ColorInt color: Int): IGlideOption<R>

    /**
     * 灰色模式
     * */
    fun grayscale(): IGlideOption<R>

    /**
     * 添加蒙层
     * */
    fun mask(@DrawableRes drawableId: Int): IGlideOption<R>

    /**
     * 透明度 [0 - 255]
     * */
    fun alpha(alpha: Int): IGlideOption<R>

    /**
     * 添加自定义转换器
     * */
    fun addTransform(bitmapTransformation: BitmapTransformation): IGlideOption<R>
}