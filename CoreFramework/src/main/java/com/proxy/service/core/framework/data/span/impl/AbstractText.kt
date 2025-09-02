package com.proxy.service.core.framework.data.span.impl

import android.graphics.BlurMaskFilter
import android.graphics.Shader
import android.graphics.Typeface
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.MaskFilterSpan
import android.text.style.RelativeSizeSpan
import android.text.style.ScaleXSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.UnderlineSpan
import android.view.View
import com.proxy.service.core.framework.app.resource.CsDpUtils
import com.proxy.service.core.framework.data.span.builder.ITextBuilder
import com.proxy.service.core.framework.data.span.custom.CustomClickableSpan
import com.proxy.service.core.framework.data.span.custom.CustomTypefaceSpan
import com.proxy.service.core.framework.data.span.custom.CustomURLSpan
import com.proxy.service.core.framework.data.span.custom.ShaderSpan
import com.proxy.service.core.framework.data.span.custom.ShadowSpan
import com.proxy.service.core.framework.data.span.enums.BlurUnit
import com.proxy.service.core.framework.data.span.enums.TextSizeUnit

/**
 * @author: cangHX
 * @data: 2025/7/14 10:10
 * @desc:
 */
abstract class AbstractText : AbstractImage(), ITextBuilder {

    /**
     * 文字
     * */
    protected var text: CharSequence? = null

    /**
     * 背景颜色
     * */
    private var textBackgroundColor: Int? = null

    /**
     * 文字颜色
     * */
    private var textColor: Int? = null

    /**
     * 文字大小
     * */
    private var textSize: Int? = null

    /**
     * 文字大小缩放比例
     * */
    private var textSizeProportion: Float? = null

    /**
     * 文字宽度缩放比例
     * */
    private var textWidthProportion: Float? = null

    /**
     * 粗体
     * */
    private var textIsBold = false

    /**
     * 斜体
     * */
    private var textIsItalic = false

    /**
     * 粗体+斜体
     * */
    private var textIsBoldItalic = false

    /**
     * 下划线
     * */
    private var textIsUnderline = false

    /**
     * 删除线
     * */
    private var textIsStrikethrough = false

    /**
     * 上角标
     * */
    private var textIsSuperscript = false

    /**
     * 下角标
     * */
    private var textIsSubscript = false

    /**
     * 字体
     * */
    private var textFontFamily: String? = null

    /**
     * 字体
     * */
    private var textTypeface: Typeface? = null

    /**
     * 超链接
     * */
    private var textUrl: String? = null


    /**
     * 模糊效果
     * */
    private var textBlurRadius: Float? = null
    private var textBlurStyle: BlurUnit? = null

    /**
     * 投影效果
     * */
    private var textShadowRadius: Float? = null
    private var textShadowDx: Float? = null
    private var textShadowDy: Float? = null
    private var textShadowColor: Int? = null

    /**
     * 渐变色
     * */
    private var textShader: Shader? = null

    /**
     * 点击事件
     * */
    private var textListener: View.OnClickListener? = null

    override fun setBackgroundColor(color: Int): ITextBuilder {
        this.textBackgroundColor = color
        return this
    }

    override fun setTextColor(color: Int): ITextBuilder {
        this.textColor = color
        return this
    }

    override fun setTextSize(size: Int, unit: TextSizeUnit): ITextBuilder {
        when (unit) {
            TextSizeUnit.UNIT_SP -> {
                this.textSize = CsDpUtils.sp2px(size.toFloat())
            }

            TextSizeUnit.UNIT_DP -> {
                this.textSize = CsDpUtils.dp2px(size.toFloat())
            }

            else -> {
                this.textSize = size
            }
        }
        return this
    }

    override fun setTextSizeProportion(proportion: Float): ITextBuilder {
        this.textSizeProportion = proportion
        return this
    }

    override fun setTextWidthProportion(proportion: Float): ITextBuilder {
        this.textWidthProportion = proportion
        return this
    }

    override fun setBold(): ITextBuilder {
        this.textIsBold = true
        return this
    }

    override fun setItalic(): ITextBuilder {
        this.textIsItalic = true
        return this
    }

    override fun setBoldItalic(): ITextBuilder {
        this.textIsBoldItalic = true
        return this
    }

    override fun setUnderline(): ITextBuilder {
        this.textIsUnderline = true
        return this
    }

    override fun setStrikethrough(): ITextBuilder {
        this.textIsStrikethrough = true
        return this
    }

    override fun setSuperscript(): ITextBuilder {
        this.textIsSuperscript = true
        return this
    }

    override fun setSubscript(): ITextBuilder {
        this.textIsSubscript = true
        return this
    }

    override fun setFont(fontFamily: String): ITextBuilder {
        this.textFontFamily = fontFamily
        return this
    }

    override fun setFont(typeface: Typeface): ITextBuilder {
        this.textTypeface = typeface
        return this
    }

    override fun setUrl(url: String): ITextBuilder {
        this.textUrl = url
        return this
    }

    override fun setClick(listener: View.OnClickListener): ITextBuilder {
        this.textListener = listener
        return this
    }

    override fun setBlur(radius: Float, style: BlurUnit): ITextBuilder {
        this.textBlurRadius = radius
        this.textBlurStyle = style
        return this
    }

    override fun setShadow(radius: Float, dx: Float, dy: Float, shadowColor: Int): ITextBuilder {
        this.textShadowRadius = radius
        this.textShadowDx = dx
        this.textShadowDy = dy
        this.textShadowColor = shadowColor
        return this
    }

    override fun setShader(shader: Shader): ITextBuilder {
        this.textShader = shader
        return this
    }

    override fun applyLast() {
        super.applyLast()
        val tempText = text
        if (mType == TYPE_TEXT && tempText != null) {
            val start = mBuilder.length
            mBuilder.append(tempText)
            val end = mBuilder.length

            val tempTextBackgroundColor = textBackgroundColor
            if (tempTextBackgroundColor != null) {
                mBuilder.setSpan(BackgroundColorSpan(tempTextBackgroundColor), start, end, flag)
            }

            val tempTextColor = textColor
            if (tempTextColor != null) {
                mBuilder.setSpan(ForegroundColorSpan(tempTextColor), start, end, flag)
            }

            val tempTextSize = textSize
            if (tempTextSize != null) {
                mBuilder.setSpan(AbsoluteSizeSpan(tempTextSize, false), start, end, flag)
            }

            val tempTextSizeProportion = textSizeProportion
            if (tempTextSizeProportion != null) {
                mBuilder.setSpan(RelativeSizeSpan(tempTextSizeProportion), start, end, flag)
            }

            val tempTextWidthProportion = textWidthProportion
            if (tempTextWidthProportion != null) {
                mBuilder.setSpan(ScaleXSpan(tempTextWidthProportion), start, end, flag)
            }

            if (textIsBold) {
                mBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, flag)
            }

            if (textIsItalic) {
                mBuilder.setSpan(StyleSpan(Typeface.ITALIC), start, end, flag)
            }

            if (textIsBoldItalic) {
                mBuilder.setSpan(StyleSpan(Typeface.BOLD_ITALIC), start, end, flag)
            }

            if (textIsUnderline) {
                mBuilder.setSpan(UnderlineSpan(), start, end, flag);
            }

            if (textIsStrikethrough) {
                mBuilder.setSpan(StrikethroughSpan(), start, end, flag)
            }

            if (textIsSuperscript) {
                mBuilder.setSpan(SuperscriptSpan(), start, end, flag)
            }

            if (textIsSubscript) {
                mBuilder.setSpan(SubscriptSpan(), start, end, flag)
            }

            val tempTextFontFamily = textFontFamily
            if (tempTextFontFamily != null) {
                mBuilder.setSpan(CustomTypefaceSpan(tempTextFontFamily), start, end, flag)
            }

            val tempTextTypeface = textTypeface
            if (tempTextTypeface != null) {
                mBuilder.setSpan(CustomTypefaceSpan(tempTextTypeface), start, end, flag)
            }

            val tempTextUrl = textUrl
            if (tempTextUrl != null) {
                isHasClick = true
                mBuilder.setSpan(CustomURLSpan(tempTextUrl), start, end, flag);
            }

            if (textListener != null) {
                isHasClick = true
                mBuilder.setSpan(CustomClickableSpan(textListener), start, end, flag)
            }

            val tempTextBlurRadius = textBlurRadius
            val tempTextBlurStyle = textBlurStyle?.blur
            if (tempTextBlurRadius != null && tempTextBlurStyle != null) {
                val filter = BlurMaskFilter(tempTextBlurRadius, tempTextBlurStyle)
                mBuilder.setSpan(MaskFilterSpan(filter), start, end, flag)
            }

            val tempTextShadowRadius = textShadowRadius
            val tempTextShadowDx = textShadowDx
            val tempTextShadowDy = textShadowDy
            val tempTextShadowColor = textShadowColor
            if (
                tempTextShadowRadius != null &&
                tempTextShadowDx != null &&
                tempTextShadowDy != null &&
                tempTextShadowColor != null
            ) {
                val shadow = ShadowSpan(
                    tempTextShadowRadius,
                    tempTextShadowDx,
                    tempTextShadowDy,
                    tempTextShadowColor
                )
                mBuilder.setSpan(shadow, start, end, flag)
            }

            val tempTextShader = textShader
            if (tempTextShader != null) {
                mBuilder.setSpan(ShaderSpan(tempTextShader), start, end, flag);
            }
        }
        text = null
        textBackgroundColor = null
        textColor = null
        textSize = null
        textSizeProportion = null
        textWidthProportion = null

        textIsBold = false
        textIsItalic = false
        textIsBoldItalic = false
        textIsUnderline = false
        textIsStrikethrough = false
        textIsSuperscript = false
        textIsSubscript = false

        textFontFamily = null
        textTypeface = null
        textUrl = null

        textBlurRadius = null
        textBlurStyle = null

        textShadowRadius = null
        textShadowDx = null
        textShadowDy = null
        textShadowColor = null

        textShader = null

        textListener = null
    }

}