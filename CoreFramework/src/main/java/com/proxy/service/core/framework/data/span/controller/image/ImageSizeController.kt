package com.proxy.service.core.framework.data.span.controller.image

/**
 * @author: cangHX
 * @data: 2025/9/10 10:24
 * @desc:
 */
class ImageSizeController(
    private val widthPx: Int,
    private val heightPx: Int,
    private val keepAspectRatio: Boolean
) : IImageSize() {

    private var finalWidth = widthPx
    private var finalHeight = heightPx

    override fun parseDrawableSize(width: Int, height: Int): Boolean {
        if (keepAspectRatio) {
            val aspectRatio = width.toFloat() / height.toFloat()
            if (widthPx / heightPx.toFloat() > aspectRatio) {
                finalWidth = (heightPx * aspectRatio).toInt()
            } else {
                finalHeight = (widthPx / aspectRatio).toInt()
            }
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