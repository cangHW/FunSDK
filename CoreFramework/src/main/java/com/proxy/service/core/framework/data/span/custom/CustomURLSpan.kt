package com.proxy.service.core.framework.data.span.custom

import android.text.TextPaint
import android.text.style.URLSpan

/**
 * @author: cangHX
 * @data: 2025/7/14 17:21
 * @desc:
 */
class CustomURLSpan(url:String): URLSpan(url) {

    override fun updateDrawState(ds: TextPaint) {

    }

}