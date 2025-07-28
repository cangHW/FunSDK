package com.proxy.service.document.pdf.base.constants

import com.proxy.service.document.pdf.base.enums.CacheType
import com.proxy.service.document.pdf.base.enums.PagePixelFormat
import com.proxy.service.document.pdf.base.enums.ViewShowType

/**
 * @author: cangHX
 * @data: 2025/5/2 20:34
 * @desc:
 */
object PdfConstants {

    const val LOG_TAG_PDF_START = "Document_Pdf_"

    const val DEFAULT_BG_COLOR = 0xFFFFFFFF

    val DEFAULT_PAGE_PIXEL_FORMAT = PagePixelFormat.ARGB_8888

    val DEFAULT_CACHE_TYPE = CacheType.COUNT
    const val DEFAULT_PAGE_CACHE_MAX_COUNT = 8
    const val PAGE_CACHE_MIN_COUNT = 3
    const val DEFAULT_PAGE_CACHE_MAX_SIZE = 80 * 1024 * 1024
    const val PAGE_CACHE_MIN_SIZE = 30 * 1024 * 1024

    const val DEFAULT_PAGE_SHOW_MAX_COUNT = Int.MAX_VALUE

    val DEFAULT_VIEW_SHOW_TYPE = ViewShowType.SINGLE_PAGE_HORIZONTAL
}