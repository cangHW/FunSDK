package com.proxy.service.core.framework.data.span.builder

import androidx.annotation.ColorInt
import com.proxy.service.core.framework.data.span.builder.base.IAppendBuilder

/**
 * @author: cangHX
 * @data: 2025/7/11 17:11
 * @desc:
 */
interface ISpaceBuilder : IAppendBuilder {

    /**
     * 设置空格颜色
     * */
    fun setSpaceColor(@ColorInt color: Int): ISpaceBuilder

}