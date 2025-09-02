package com.proxy.service.document.pdf.info.loader.impl

import com.proxy.service.document.pdf.base.loader.IPdfText

/**
 * @author: cangHX
 * @data: 2025/8/25 11:13
 * @desc:
 */
open class PdfText : PdfEdit(), IPdfText {
    /**
     * 获取页面内文字数量
     * */
    override fun getTextCount(pageIndex: Int): Int {
        if (isNotReady()) {
            return -1
        }
        synchronized(lock) {
            if (isNotReady()) {
                return -1
            }
            val doc = findDocByPageIndex(pageIndex)
            val page = doc?.getPageInfo(pageIndex)
            val pageText = page?.getPageTextInfo()
            return pageText?.getTextCount() ?: -1
        }
    }

    /**
     * 获取页面内文字
     * */
    override fun getText(pageIndex: Int): String {
        if (isNotReady()) {
            return ""
        }
        synchronized(lock) {
            if (isNotReady()) {
                return ""
            }
            val doc = findDocByPageIndex(pageIndex)
            val page = doc?.getPageInfo(pageIndex)
            val pageText = page?.getPageTextInfo()
            return pageText?.getText() ?: ""
        }
    }
}