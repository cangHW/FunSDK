package com.proxy.service.document.image.func.crop.controller

import android.graphics.Matrix
import android.graphics.RectF
import com.proxy.service.document.base.image.callback.base.OnDragCallback
import com.proxy.service.document.base.image.callback.base.OnScaleCallback
import com.proxy.service.document.base.image.loader.base.IOption
import com.proxy.service.document.image.func.crop.CropInfo

/**
 * @author: cangHX
 * @data: 2025/6/3 10:16
 * @desc:
 */
class CropControllerImpl(
    private val option: IOption,
    info: CropInfo
) : BaseController(option, info), OnDragCallback, OnScaleCallback {

    override fun init() {
        super.init()
        option.setMinScale(1f)
        option.setDragCallback(this)
        option.setScaleCallback(this)
    }

    override fun onBoundChanged(
        bitmapRect: RectF,
        matrix: Matrix,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ): Boolean {
        super.onBoundChanged(bitmapRect, matrix, left, top, right, bottom)
        val scaleX = cropRect.width() / bitmapRect.width()
        val scaleY = cropRect.height() / bitmapRect.height()

        val scale = maxOf(scaleX, scaleY)

        matrix.postScale(scale, scale)

        val scaledWidth = (bitmapRect.width() * scale).toInt()
        val scaledHeight = (bitmapRect.height() * scale).toInt()

        val dx = (cropRect.width() - scaledWidth) / 2f + cropRect.left
        val dy = (cropRect.height() - scaledHeight) / 2f + cropRect.top
        matrix.postTranslate(dx, dy)
        return true
    }

    override fun onDraggedEnd(bitmapRect: RectF, matrix: Matrix) {
        checkMatrixRect(matrix, bitmapRect)
    }

    override fun onScaleEnd(bitmapRect: RectF, matrix: Matrix) {
        checkMatrixRect(matrix, bitmapRect)
    }

    private val destRectF = RectF(0f, 0f, 0f, 0f)
    private fun checkMatrixRect(matrix: Matrix, srcRect: RectF) {
        if (!matrix.mapRect(destRectF, srcRect)) {
            return
        }

        val minOffsetX = cropRect.left - (destRectF.width() - cropRect.width())
        val maxOffsetX = cropRect.left
        val minOffsetY = cropRect.top - (destRectF.height() - cropRect.height())
        val maxOffsetY = cropRect.top

        val translateX = if (destRectF.left > maxOffsetX) {
            maxOffsetX - destRectF.left
        } else if (destRectF.left < minOffsetX) {
            minOffsetX - destRectF.left
        } else {
            0f
        }

        val translateY = if (destRectF.top > maxOffsetY) {
            maxOffsetY - destRectF.top
        } else if (destRectF.top < minOffsetY) {
            minOffsetY - destRectF.top
        } else {
            0f
        }

        matrix.postTranslate(translateX, translateY)
    }
}