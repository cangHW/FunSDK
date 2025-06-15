package com.proxy.service.document.image.info.func.crop

import android.graphics.Color
import android.graphics.RectF
import com.proxy.service.document.image.base.constants.Constants
import com.proxy.service.document.image.base.callback.crop.OnDrawCropCallback

/**
 * @author: cangHX
 * @data: 2025/6/3 20:18
 * @desc:
 */
class CropInfo {

    var cropRect: RectF? = null
    var cropWidthPx: Float? = null
    var cropHeightPx: Float? = null

    var maskColor: Int = Color.parseColor(Constants.DEFAULT_CROP_MASK_COLOR)

    var cropLineColor: Int = Color.parseColor(Constants.DEFAULT_CROP_LINE_COLOR)

    var cropLineWidth: Float = Constants.DEFAULT_CROP_LINE_WIDTH

    var drawCropCallback: OnDrawCropCallback? = null

    fun boundsChangedToCheckCropRect(
        destRect: RectF,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        if (cropRect != null) {
            destRect.set(cropRect!!)
            return
        }

        if (cropWidthPx == null) {
            return
        }
        if (cropHeightPx == null) {
            return
        }

        val x = ((right - left) - cropWidthPx!!) / 2f
        val y = ((bottom - top) - cropHeightPx!!) / 2f

        destRect.set(x, y, x + cropWidthPx!!, y + cropHeightPx!!)
    }
}