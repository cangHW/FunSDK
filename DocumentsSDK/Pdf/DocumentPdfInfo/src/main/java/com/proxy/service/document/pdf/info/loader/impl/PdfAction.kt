package com.proxy.service.document.pdf.info.loader.impl

import com.proxy.service.document.pdf.base.loader.IPdfAction
import com.proxy.service.document.pdf.base.bean.CatalogueData
import com.proxy.service.document.pdf.base.bean.LinkData
import com.proxy.service.document.pdf.base.bean.MetaData
import com.proxy.service.document.pdf.base.bean.PageSize
import com.proxy.service.document.pdf.info.core.PdfiumCore

/**
 * @author: cangHX
 * @data: 2025/4/30 14:58
 * @desc:
 */
open class PdfAction : PdfEdit(), IPdfAction {

    override fun getPageCount(): Int {
        if (isNotReady()) {
            return -1
        }
        var totalCount = 0
        synchronized(lock) {
            if (isNotReady()) {
                return -1
            }
            docs.forEach {
                totalCount += it.getPageCount()
            }
        }
        return totalCount
    }

    override fun getPageWidthPixel(pageIndex: Int): Int {
        if (isNotReady()) {
            return -1
        }
        synchronized(lock) {
            if (isNotReady()) {
                return -1
            }
            val doc = findDocByPageIndex(pageIndex)
            val page = doc?.getPageInfo(pageIndex)
            return page?.getPageWidthPixel() ?: 0
        }
    }

    override fun getPageHeightPixel(pageIndex: Int): Int {
        if (isNotReady()) {
            return -1
        }
        synchronized(lock) {
            if (isNotReady()) {
                return -1
            }
            val doc = findDocByPageIndex(pageIndex)
            val page = doc?.getPageInfo(pageIndex)
            return page?.getPageHeightPixel() ?: 0
        }
    }

    override fun getPageSizePixel(pageIndex: Int): PageSize {
        if (isNotReady()) {
            return PageSize(-1, -1)
        }
        synchronized(lock) {
            if (isNotReady()) {
                return PageSize(-1, -1)
            }
            val doc = findDocByPageIndex(pageIndex)
            val page = doc?.getPageInfo(pageIndex)

            val size = PageSize(
                widthPixel = page?.getPageWidthPixel() ?: 0,
                heightPixel = page?.getPageHeightPixel() ?: 0
            )
            return size
        }
    }

    override fun getDocumentMeta(): MetaData {
        if (isNotReady()) {
            return MetaData()
        }
        if (meta == null) {
            synchronized(lock) {
                if (isNotReady()) {
                    return MetaData()
                }
                if (meta == null) {
                    val doc = docs.get(0)
                    meta = PdfiumCore.getInstance().getDocumentMeta(doc.hand)
                }
            }
        }
        return meta!!
    }

    override fun getDocumentCatalogue(): ArrayList<CatalogueData> {
        if (isNotReady()) {
            return ArrayList()
        }
        if (catalogue == null) {
            synchronized(lock) {
                if (isNotReady()) {
                    return ArrayList()
                }
                if (catalogue == null) {
                    val doc = docs.get(0)
                    catalogue = PdfiumCore.getInstance().getDocumentCatalogue(doc.hand)
                }
            }
        }
        return catalogue!!
    }

    override fun getPageLinks(pageIndex: Int): ArrayList<LinkData> {
        if (isNotReady()) {
            return ArrayList()
        }
        val links = ArrayList<LinkData>()
        synchronized(lock) {
            if (isNotReady()) {
                return ArrayList()
            }
            val doc = findDocByPageIndex(pageIndex)
            val page = doc?.getPageInfo(pageIndex)
            page?.getLinks()?.let {
                links.addAll(it)
            }
        }
        return links
    }
}