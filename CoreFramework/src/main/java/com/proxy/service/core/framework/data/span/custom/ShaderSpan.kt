package com.proxy.service.core.framework.data.span.custom

import android.graphics.Shader
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance

/**
 * 渐变效果
 *
 * @author: cangHX
 * @data: 2025/7/14 14:56
 * @desc:
 */
class ShaderSpan(
    private val mShader: Shader
) : CharacterStyle(), UpdateAppearance {

    override fun updateDrawState(tp: TextPaint?) {
        tp?.setShader(mShader)
    }

}