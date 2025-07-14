package com.proxy.service.core.framework.data.span

import com.proxy.service.core.framework.data.span.builder.base.IAppendBuilder
import com.proxy.service.core.framework.data.span.impl.SpanImpl

/**
 * 富文本生成器
 *
 * @author: cangHX
 * @data: 2025/7/11 17:07
 * @desc:
 */
object CsSpanUtils {

    /**
     * 构建富文本
     * */
    fun builder(): IAppendBuilder {
        return SpanImpl()
    }

}