package com.proxy.service.document.image.drawable

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable

/**
 * @author: cangHX
 * @data: 2025/5/30 15:05
 * @desc:
 */
open class ImageDrawable(
    private val bitmap: Bitmap
) : Drawable() {

    protected val paint = Paint()
    protected val matrix = Matrix()
    protected val srcRectF = Rect(0, 0, bitmap.width, bitmap.height)

    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true

        canvas.drawBitmap(bitmap, matrix, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}