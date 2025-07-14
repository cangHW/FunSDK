package com.proxy.service.core.framework.data.span.enums

import android.graphics.BlurMaskFilter

/**
 * @author: cangHX
 * @data: 2025/7/14 10:03
 * @desc:
 */
enum class BlurUnit(val blur: BlurMaskFilter.Blur) {

    /**
     * 模糊效果均匀分布
     */
    NORMAL(BlurMaskFilter.Blur.NORMAL),

    /**
     * 模糊集中在文本内部，外部清晰
     */
    SOLID(BlurMaskFilter.Blur.SOLID),

    /**
     * 模糊集中在文本外部，内部清晰
     */
    OUTER(BlurMaskFilter.Blur.OUTER),

    /**
     * 模糊集中在文本边缘
     */
    INNER(BlurMaskFilter.Blur.INNER);

}