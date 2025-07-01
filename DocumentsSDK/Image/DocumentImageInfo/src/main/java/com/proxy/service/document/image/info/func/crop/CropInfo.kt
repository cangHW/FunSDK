package com.proxy.service.document.image.info.func.crop

import android.graphics.Color
import android.graphics.Matrix
import android.graphics.RectF
import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.callback.crop.OnDrawCropCallback
import com.proxy.service.document.image.info.utils.RectUtils

/**
 * @author: cangHX
 * @data: 2025/6/3 20:18
 * @desc:
 */
class CropInfo {

    var cropFrameFitBitmap: Boolean = false
    var cropFrameRect: RectF? = null
    var cropFrameWidthPx: Float? = null
    var cropFrameHeightPx: Float? = null
    var cropFrameLineColor: Int = Color.parseColor(ImageConstants.DEFAULT_CROP_FRAME_LINE_COLOR)
    var cropFrameLineWidth: Float = ImageConstants.DEFAULT_CROP_FRAME_LINE_WIDTH

    var maskColor: Int = Color.parseColor(ImageConstants.DEFAULT_CROP_MASK_COLOR)

    var drawCropCallback: OnDrawCropCallback? = null

    fun boundsChangedToCheckCropRect(
        destRect: RectF,
        bitmapRect: RectF,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        if (cropFrameRect != null) {
            destRect.set(cropFrameRect!!)
            return
        }

        if (cropFrameFitBitmap) {
            val matrix = Matrix()
            RectUtils.fitRect(bitmapRect, destRect, matrix, cropFrameLineWidth, left, top, right, bottom)
            return
        }

        if (cropFrameWidthPx == null) {
            return
        }
        if (cropFrameHeightPx == null) {
            return
        }

        val x = ((right - left) - cropFrameWidthPx!!) / 2f
        val y = ((bottom - top) - cropFrameHeightPx!!) / 2f

        destRect.set(x, y, x + cropFrameWidthPx!!, y + cropFrameHeightPx!!)
    }
}