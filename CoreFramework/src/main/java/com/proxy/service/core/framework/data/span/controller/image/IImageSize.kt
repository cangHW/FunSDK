package com.proxy.service.core.framework.data.span.controller.image

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2025/9/10 10:20
 * @desc:
 */
abstract class IImageSize {

    protected val TAG = "${CoreConfig.TAG}Span_ImageResize"

    /**
     * 设置 drawable 尺寸
     * */
    fun setDrawableSize(width: Int, height: Int): Boolean {
        if (width <= 0 || height <= 0) {
            CsLogger.tag(TAG).e("The width and height of the drawable are less than or equal to 0")
            return false
        }
        return parseDrawableSize(width, height)
    }

    /**
     * 处理 drawable 尺寸
     * */
    abstract fun parseDrawableSize(width: Int, height: Int): Boolean

    /**
     * 获取最终宽度
     * */
    abstract fun getFinalWidth(): Int

    /**
     * 获取最终高度
     * */
    abstract fun getFinalHeight(): Int
}