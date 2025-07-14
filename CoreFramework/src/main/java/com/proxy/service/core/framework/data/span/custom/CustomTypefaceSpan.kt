package com.proxy.service.core.framework.data.span.custom

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

/**
 * 字体适配器
 *
 * @author: cangHX
 * @data: 2025/7/14 14:27
 * @desc:
 */
class CustomTypefaceSpan : TypefaceSpan {

    private var typeface: Typeface? = null

    constructor(typeface: Typeface) : super(null) {
        this.typeface = typeface
    }

    constructor(family: String) : super(family)

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        apply(ds)
    }

    override fun updateMeasureState(paint: TextPaint) {
        super.updateMeasureState(paint)
        apply(paint)
    }

    private fun apply(paint: Paint){
        val tempTypeface = typeface ?: return

        val oldStyle: Int
        val old = paint.typeface
        oldStyle = old?.style ?: 0

        val fake = oldStyle and tempTypeface.style.inv()
        if ((fake and Typeface.BOLD) != 0) {
            paint.isFakeBoldText = true
        }

        if ((fake and Typeface.ITALIC) != 0) {
            paint.textSkewX = -0.25f
        }

        paint.setTypeface(tempTypeface)
    }
}