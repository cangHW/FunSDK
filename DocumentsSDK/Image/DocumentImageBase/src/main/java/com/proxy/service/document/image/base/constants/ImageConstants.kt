package com.proxy.service.document.image.base.constants

import com.proxy.service.core.framework.app.resource.CsDpUtils

/**
 * @author: cangHX
 * @data: 2025/5/2 20:34
 * @desc:
 */
object ImageConstants {

    const val LOG_TAG_IMAGE_START = "Document_Image_"

    const val DEFAULT_MIN_SCALE = 0.5f
    const val DEFAULT_MAX_SCALE = 10f

    const val DEFAULT_CROP_MASK_COLOR = "#CC000000"
    const val DEFAULT_CROP_FRAME_LINE_COLOR = "#ffffff"
    const val DEFAULT_CROP_FRAME_LINE_WIDTH = 4f

    const val DEFAULT_CROP_FRAME_MIN_SIZE = 80f
    const val DEFAULT_CROP_FRAME_CENTER_DRAGGABLE = true
    const val DEFAULT_CROP_FRAME_CORNER_DRAGGABLE = true
    const val DEFAULT_CROP_FRAME_EDGE_DRAGGABLE = true
    const val DEFAULT_CROP_FRAME_ASPECT_RATIO_LOCK = false
    var DEFAULT_CROP_TOUCH_OFFSET_WIDTH = CsDpUtils.dp2px(10f)
}