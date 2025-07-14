package com.proxy.service.core.framework.data.span.impl

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.proxy.service.core.framework.data.span.builder.SerializableSpannableStringBuilder
import com.proxy.service.core.framework.data.span.builder.base.IBaseBuilder

/**
 * @author: cangHX
 * @data: 2025/7/14 10:07
 * @desc:
 */
abstract class AbstractBase : IBaseBuilder {

    companion object {
        @JvmStatic
        protected val TYPE_NONE: Int = -1

        @JvmStatic
        protected val TYPE_SPACE: Int = 0

        @JvmStatic
        protected val TYPE_IMAGE: Int = 1

        @JvmStatic
        protected val TYPE_TEXT: Int = 2
    }

    protected val mBuilder = SerializableSpannableStringBuilder()

    protected var isHasClick = false

    protected var flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    protected var mType: Int = TYPE_NONE

    override fun create(): SpannableStringBuilder {
        applyLast()
        return mBuilder
    }

    override fun createTo(textview: TextView) {
        applyLast()
        if (isHasClick && textview.movementMethod == null) {
            textview.movementMethod = LinkMovementMethod.getInstance()
        }

        textview.text = mBuilder
    }

    /**
     * 处理 span
     * */
    protected open fun applyLast() {}

}