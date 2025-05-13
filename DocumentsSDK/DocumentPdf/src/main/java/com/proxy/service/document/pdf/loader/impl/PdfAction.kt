package com.proxy.service.document.pdf.loader.impl

import com.proxy.service.document.base.pdf.IPdfAction
import com.proxy.service.document.base.pdf.info.CatalogueData
import com.proxy.service.document.base.pdf.info.LinkData
import com.proxy.service.document.base.pdf.info.MetaData
import com.proxy.service.document.base.pdf.info.PageSize
import com.proxy.service.document.pdf.core.PdfiumCore
import com.proxy.service.document.pdf.loader.utils.LoaderUtils

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
            val doc = LoaderUtils.findDocByPageIndex(docs, pageIndex)
            val page = doc?.getPageInfo(pageIndex)
            return page?.getPageWidthPixel() ?: 0
        }
    }

    override fun getPageHeightPixel(pageIndex: Int): Int {
        if (isNotReady()) {
            return -1
        }
        synchronized(lock) {
            val doc = LoaderUtils.findDocByPageIndex(docs, pageIndex)
            val page = doc?.getPageInfo(pageIndex)
            return page?.getPageHeightPixel() ?: 0
        }
    }

    override fun getPageSizePixel(pageIndex: Int): PageSize {
        if (isNotReady()) {
            return PageSize(-1, -1)
        }
        synchronized(lock) {
            val doc = LoaderUtils.findDocByPageIndex(docs, pageIndex)
            val page = doc?.getPageInfo(pageIndex)

            val size = PageSize(
                width = page?.getPageWidthPixel() ?: 0,
                height = page?.getPageHeightPixel() ?: 0
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
            val doc = LoaderUtils.findDocByPageIndex(docs, pageIndex)
            val page = doc?.getPageInfo(pageIndex)
            page?.getLinks()?.let {
                links.addAll(it)
            }
        }
        return links
    }
}