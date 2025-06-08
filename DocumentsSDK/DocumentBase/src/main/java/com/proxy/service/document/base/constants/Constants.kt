package com.proxy.service.document.base.constants

/**
 * @author: cangHX
 * @data: 2025/5/2 20:34
 * @desc:
 */
object Constants {

    private const val LOG_TAG_START = "Document_"

    const val LOG_TAG_PDF_START = "${LOG_TAG_START}Pdf_"

    const val LOG_TAG_IMAGE_START = "${LOG_TAG_START}Image_"

    const val DEFAULT_BG_COLOR = 0xFFFFFFFF

    const val DEFAULT_MIN_SCALE = 0.5f
    const val DEFAULT_MAX_SCALE = 50f

    const val DEFAULT_CROP_SIZE = 300
    const val DEFAULT_CROP_MASK_COLOR = "#CC000000"
    const val DEFAULT_CROP_LINE_COLOR = "#ffffff"
    const val DEFAULT_CROP_LINE_WIDTH = 4f
}