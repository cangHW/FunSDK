package com.proxy.service.document.image.func.crop

import android.graphics.Color
import android.graphics.RectF
import com.proxy.service.document.base.constants.Constants
import com.proxy.service.document.base.image.callback.crop.OnDrawCropCallback

/**
 * @author: cangHX
 * @data: 2025/6/3 20:18
 * @desc:
 */
class CropInfo {

    var cropRect: RectF? = null
    var cropWidthPx: Int = Constants.DEFAULT_CROP_SIZE
    var cropHeightPx: Int = Constants.DEFAULT_CROP_SIZE

    var maskColor: Int = Color.parseColor(Constants.DEFAULT_CROP_MASK_COLOR)

    var cropLineColor: Int = Color.parseColor(Constants.DEFAULT_CROP_LINE_COLOR)

    var cropLineWidth: Float = Constants.DEFAULT_CROP_LINE_WIDTH

    var drawCropCallback: OnDrawCropCallback? = null
}