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

    /**
     * 日志头
     * */
    const val LOG_TAG_PDF_START = "Document_Pdf_"

    /**
     * 默认背景色
     * */
    const val DEFAULT_BG_COLOR = 0xFFFFFFFF

    /**
     * 默认页面像素格式
     * */
    val DEFAULT_PAGE_PIXEL_FORMAT = PagePixelFormat.ARGB_8888

    /**
     * 默认缓存类型
     * */
    val DEFAULT_CACHE_TYPE = CacheType.COUNT

    /**
     * 默认缓存页面最大数量
     * */
    const val DEFAULT_PAGE_CACHE_MAX_COUNT = 8

    /**
     * 缓存页面最小数量
     * */
    const val PAGE_CACHE_MIN_COUNT = 3

    /**
     * 默认缓存页面最大值
     * */
    const val DEFAULT_PAGE_CACHE_MAX_SIZE = 80 * 1024 * 1024

    /**
     * 默认缓存页面最小值
     * */
    const val PAGE_CACHE_MIN_SIZE = 30 * 1024 * 1024

    /**
     * 默认能够展示的页面数量
     * */
    const val DEFAULT_PAGE_SHOW_MAX_COUNT = Int.MAX_VALUE

    /**
     * 默认页面展示类型
     * */
    val DEFAULT_VIEW_SHOW_TYPE = ViewShowType.SINGLE_PAGE_HORIZONTAL

    /**
     * 默认缓存文字内容的页面最大数量
     * */
    const val DEFAULT_CACHE_TEXT_PAGE_MAX_COUNT = 5
}