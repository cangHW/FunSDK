package com.proxy.service.document.pdf.info.loader.cache

import android.util.LruCache
import com.proxy.service.document.pdf.base.constants.PdfConstants
import com.proxy.service.document.pdf.info.loader.info.PageTextInfo

/**
 * @author: cangHX
 * @data: 2025/8/25 14:47
 * @desc:
 */
class LruPageTextCache: LruCache<Int, PageTextInfo>(PdfConstants.DEFAULT_CACHE_TEXT_PAGE_MAX_COUNT) {

    override fun entryRemoved(
        evicted: Boolean,
        key: Int?,
        oldValue: PageTextInfo?,
        newValue: PageTextInfo?
    ) {
        oldValue?.clear()
    }

}