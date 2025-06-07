package com.proxy.service.document.image.drawable

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable

/**
 * @author: cangHX
 * @data: 2025/6/3 10:24
 * @desc:
 */
abstract class BaseDrawable(
    private val bitmap: Bitmap
) : Drawable() {

    protected val mPaint = Paint()
    protected val mMatrix = Matrix()
    protected val mSrcRectF = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
    protected var mCurrentScale = 1f

    fun getBitmap(): Bitmap {
        return bitmap
    }

    fun getMatrix(): Matrix {
        return mMatrix
    }

    fun getCurrentScale(): Float {
        return mCurrentScale
    }
}