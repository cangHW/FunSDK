package com.proxy.service.core.framework.data.span.controller.image

/**
 * @author: cangHX
 * @data: 2025/9/10 10:24
 * @desc:
 */
class ImageWidthController(
    private val widthPx: Int,
    private val keepAspectRatio: Boolean
) : IImageSize() {

    private var finalWidth = widthPx
    private var finalHeight = 0

    override fun parseDrawableSize(width: Int, height: Int): Boolean {
        if (keepAspectRatio) {
            val aspectRatio = height.toFloat() / width
            finalHeight = Math.round(widthPx * aspectRatio)
        } else {
            finalHeight = height
        }
        return true
    }

    override fun getFinalWidth(): Int {
        return finalWidth
    }

    override fun getFinalHeight(): Int {
        return finalHeight
    }
}