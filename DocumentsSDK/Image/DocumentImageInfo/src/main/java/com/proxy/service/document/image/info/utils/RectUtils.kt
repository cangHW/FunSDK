package com.proxy.service.document.image.info.utils

import android.graphics.Matrix
import android.graphics.RectF

/**
 * @author: cangHX
 * @data: 2025/6/29 11:23
 * @desc:
 */
object RectUtils {

    fun fitRect(
        bitmapRect: RectF,
        destRect: RectF,
        matrix: Matrix,
        offset: Float,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        val scaleX = (viewWidth - offset * 2) / bitmapRect.width()
        val scaleY = (viewHeight - offset * 2) / bitmapRect.height()
        val scale = minOf(scaleX, scaleY)
        matrix.postScale(scale, scale)

        val scaledWidth = (bitmapRect.width() * scale).toInt()
        val scaledHeight = (bitmapRect.height() * scale).toInt()
        val dx = (viewWidth - scaledWidth) / 2f
        val dy = (viewHeight - scaledHeight) / 2f
        matrix.postTranslate(dx, dy)

        matrix.mapRect(destRect, bitmapRect)
    }

}