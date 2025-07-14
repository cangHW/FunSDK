package com.proxy.service.core.framework.data.span.custom

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

/**
 * 可点击 span
 *
 * @author: cangHX
 * @data: 2025/7/14 16:13
 * @desc:
 */
class CustomClickableSpan(
    private val listener: View.OnClickListener?
) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) {

    }

    override fun onClick(widget: View) {
        listener?.onClick(widget)
    }

}