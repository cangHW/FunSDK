package com.proxy.service.core.framework.data.span.custom

import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance

/**
 * 投影效果
 *
 * @author: cangHX
 * @data: 2025/7/14 14:59
 * @desc:
 */
class ShadowSpan(
    private val radius: Float,
    private val dx: Float,
    private val dy: Float,
    private val shadowColor: Int
) : CharacterStyle(), UpdateAppearance {

    override fun updateDrawState(tp: TextPaint?) {
        tp?.setShadowLayer(radius, dx, dy, shadowColor)
    }

}