package com.proxy.service.core.framework.data.span.controller.image

/**
 * @author: cangHX
 * @data: 2025/9/10 10:24
 * @desc:
 */
class ImageSizeController(
    private val widthPx: Int,
    private val heightPx: Int
) : IImageSize() {

    override fun parseDrawableSize(width: Int, height: Int): Boolean {
        return true
    }

    override fun getFinalWidth(): Int {
        return widthPx
    }

    override fun getFinalHeight(): Int {
        return heightPx
    }
}