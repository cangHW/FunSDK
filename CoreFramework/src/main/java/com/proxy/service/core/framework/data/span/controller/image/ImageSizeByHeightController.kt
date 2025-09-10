package com.proxy.service.core.framework.data.span.controller.image

/**
 * @author: cangHX
 * @data: 2025/9/10 10:24
 * @desc:
 */
class ImageSizeByHeightController(
    private val heightPx: Int,
    private val keepAspectRatio: Boolean
) : IImageSize() {

    private var finalWidth = 0
    private var finalHeight = heightPx

    override fun parseDrawableSize(width: Int, height: Int): Boolean {
        if (keepAspectRatio) {
            val aspectRatio = width.toFloat() / height
            finalWidth = Math.round(heightPx * aspectRatio)
        } else {
            finalWidth = width
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