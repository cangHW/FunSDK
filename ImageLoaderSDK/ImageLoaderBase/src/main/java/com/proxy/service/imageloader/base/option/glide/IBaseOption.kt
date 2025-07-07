package com.proxy.service.imageloader.base.option.glide

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.proxy.service.imageloader.base.loader.glide.IGlideLoader
import com.proxy.service.imageloader.base.option.glide.format.BitmapTransformation
import com.proxy.service.imageloader.base.option.glide.format.GlideDecodeFormat

/**
 * 配置管理器
 *
 * @author: cangHX
 * @data: 2024/5/16 09:47
 * @desc:
 */
interface IBaseOption<L : IBaseOption<L, R>, R> : IGlideLoader<R> {

    /**
     * 修改资源宽高 [0 - ]
     * */
    fun size(width: Int, height: Int): L

    /**
     * 占位图
     */
    fun placeholder(@DrawableRes placeholderId: Int): L

    /**
     * 加载错误占位图
     */
    fun error(@DrawableRes errorId: Int): L

    /**
     * 设置解码格式, 用于调整清晰度
     * */
    fun format(format: GlideDecodeFormat): L

    /**
     * 裁剪模式
     */
    fun centerCrop(): L

    /**
     * 裁剪模式
     */
    fun centerInside(): L

    /**
     * 缩放模式，维持宽高比
     */
    fun fitCenter(): L

    /**
     * 缩放模式，破坏宽高比
     */
    fun fitXY(): L

    /**
     * 圆形
     */
    fun circleCrop(): L

    /**
     * 圆角 [0 - ]
     */
    fun roundedCorners(roundingRadiusPx: Int): L

    /**
     * 圆角 [0 - ]
     */
    fun roundedCorners(
        tfRoundingRadiusPx: Float,
        trRoundingRadiusPx: Float,
        brRoundingRadiusPx: Float,
        blRoundingRadiusPx: Float
    ): L

    /**
     * 高斯模糊
     * @param radius    模糊的程度，值越大模糊效果越明显
     * @param scaling   缩放比例，值越大图越小，性能越好，但模糊效果可能会减轻
     */
    fun blur(radius: Int = 25, scaling: Int = 1): L

    /**
     * 颜色过滤
     * @param color 待过滤的颜色
     * */
    fun colorFilter(@ColorInt color: Int): L

    /**
     * 色彩饱和度, 0 为黑白, 1 为原始颜色, 大于 1 颜色变为鲜艳, 过大会失真, 建议值: [0 - 3]
     * */
    fun saturation(sat: Float): L

    /**
     * 添加蒙层
     * */
    fun mask(@DrawableRes drawableId: Int): L

    /**
     * 透明度 [0 - 255]
     * */
    fun alpha(alpha: Int): L

    /**
     * 添加自定义转换器
     * */
    fun addTransform(bitmapTransformation: BitmapTransformation): L
}