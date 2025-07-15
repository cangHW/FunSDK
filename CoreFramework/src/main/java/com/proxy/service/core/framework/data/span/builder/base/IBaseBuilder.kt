package com.proxy.service.core.framework.data.span.builder.base

import android.text.SpannableStringBuilder
import android.widget.TextView

/**
 * @author: cangHX
 * @data: 2025/7/11 17:11
 * @desc:
 */
interface IBaseBuilder {

    /**
     * 是否可以点击, 用于判断是否需要设置 movementMethod
     * */
    fun isCanClick():Boolean

    /**
     * 构建数据
     * */
    fun create(): SpannableStringBuilder

    /**
     * 构建数据并展示到 TextView
     * */
    fun createTo(textview: TextView)

}