package com.proxy.service.core.framework.data.span.controller.image

import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2025/9/10 10:24
 * @desc:
 */
class ImageMaxSizeController(
    private val maxWidthPx: Int,
    private val maxHeightPx: Int,
    private val keepAspectRatio: Boolean
) : IImageSize() {

    private var finalWidth = maxWidthPx
    private var finalHeight = maxHeightPx

    override fun parseDrawableSize(width: Int, height: Int): Boolean {
        if (keepAspectRatio) {
            val aspectRatio = width.toFloat() / height.toFloat()
            if (width > maxWidthPx || height > maxHeightPx) {
                if (maxWidthPx / maxHeightPx.toFloat() > aspectRatio) {
                    finalWidth = (maxHeightPx * aspectRatio).toInt()
                } else {
                    finalHeight = (maxWidthPx / aspectRatio).toInt()
                }
            } else {
                finalWidth = width
                finalHeight = height
            }
        } else {
            finalWidth = Math.min(width, maxWidthPx)
            finalHeight = Math.min(height, maxHeightPx)
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