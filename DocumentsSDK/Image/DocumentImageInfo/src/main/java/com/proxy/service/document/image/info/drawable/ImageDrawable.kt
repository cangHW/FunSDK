package com.proxy.service.document.image.info.drawable

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat

/**
 * @author: cangHX
 * @data: 2025/5/30 15:05
 * @desc:
 */
open class ImageDrawable(
    private val bitmap: Bitmap
) : BaseDrawable(bitmap) {

    override fun draw(canvas: Canvas) {
        mPaint.reset()
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true

        canvas.drawBitmap(bitmap, mMatrix, mPaint)
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}