package com.proxy.service.core.framework.data.span.builder

import android.graphics.Shader
import android.graphics.Typeface
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.proxy.service.core.framework.data.span.builder.base.IAppendBuilder
import com.proxy.service.core.framework.data.span.enums.BlurUnit
import com.proxy.service.core.framework.data.span.enums.TextSizeUnit

/**
 * @author: cangHX
 * @data: 2025/7/11 17:11
 * @desc:
 */
interface ITextBuilder : IAppendBuilder {

    /**
     * 设置背景色
     * */
    fun setBackgroundColor(@ColorInt color: Int): ITextBuilder

    /**
     * 设置字体颜色
     * */
    fun setTextColor(@ColorInt color: Int): ITextBuilder

    /**
     * 设置字体大小
     * */
    fun setTextSize(
        @IntRange(from = 0) size: Int,
        unit: TextSizeUnit = TextSizeUnit.UNIT_SP
    ): ITextBuilder

    /**
     * 设置字体大小缩放比例
     * */
    fun setTextSizeProportion(@FloatRange(from = 0.0) proportion: Float): ITextBuilder

    /**
     * 设置字体宽度缩放比例
     * */
    fun setTextWidthProportion(@FloatRange(from = 0.0) proportion: Float): ITextBuilder

    /**
     * 设置粗体
     * */
    fun setBold(): ITextBuilder

    /**
     * 设置斜体
     * */
    fun setItalic(): ITextBuilder

    /**
     * 设置粗体 + 斜体
     * */
    fun setBoldItalic(): ITextBuilder

    /**
     * 设置下划线
     * */
    fun setUnderline(): ITextBuilder

    /**
     * 设置删除线
     * */
    fun setStrikethrough(): ITextBuilder

    /**
     * 设置上角标
     * */
    fun setSuperscript(): ITextBuilder

    /**
     * 设置下角标
     * */
    fun setSubscript(): ITextBuilder

    /**
     * 设置字体
     * */
    fun setFont(fontFamily: String): ITextBuilder

    /**
     * 设置字体
     * */
    fun setFont(typeface: Typeface): ITextBuilder

    /**
     * 设置超链接
     * */
    fun setUrl(url: String): ITextBuilder

    /**
     * 设置点击效果
     * */
    fun setClick(listener: View.OnClickListener): ITextBuilder

    /**
     * 设置模糊效果
     *
     * @param radius    模糊强度, 建议 (0.0 - 20.0]
     * @param style     模糊模式
     * */
    fun setBlur(
        @FloatRange(from = 0.0, fromInclusive = false) radius: Float,
        style: BlurUnit
    ): ITextBuilder

    /**
     * 设置阴影效果
     * @param radius        阴影强度, 建议 (0.0 - 20.0]
     * @param dx            阴影偏移方向
     * @param dy            阴影偏移方向
     * @param shadowColor   阴影颜色
     * */
    fun setShadow(
        @FloatRange(from = 0.0, fromInclusive = false) radius: Float,
        dx: Float,
        dy: Float,
        @ColorInt shadowColor: Int
    ): ITextBuilder

    /**
     * 设置渐变效果
     * */
    fun setShader(shader: Shader): ITextBuilder
}