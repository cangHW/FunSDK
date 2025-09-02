package com.proxy.service.document.pdf.info.view

import android.view.ViewGroup
import com.proxy.service.document.pdf.base.enums.ViewShowType
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.base.view.IPdfView
import com.proxy.service.document.pdf.base.view.callback.OnShouldCreateCoverViewCallback
import com.proxy.service.document.pdf.info.view.cache.PartCache
import com.proxy.service.document.pdf.info.view.group.IFactory
import com.proxy.service.document.pdf.info.view.view.PdfLayout

/**
 * @author: cangHX
 * @data: 2025/5/14 16:05
 * @desc:
 */
class PdfViewImpl(viewGroup: ViewGroup, type: ViewShowType, maxShowCount: Int) : IPdfView {

    private val pdfLayout = PdfLayout(viewGroup.context)

    init {
        IFactory.of(viewGroup, pdfLayout)
        pdfLayout.setPageShowMaxCount(maxShowCount)
        pdfLayout.setShowType(type)
    }

    private var loader: IPdfLoader? = null
    private var cache: PartCache? = null

    fun setCreateCoverViewCallback(callback: OnShouldCreateCoverViewCallback?) {
        pdfLayout.setOnCreateCoverViewCallback(callback)
    }

    fun setLoader(loader: IPdfLoader?, cache: PartCache) {
        if (loader == null) {
            return
        }
        this.loader = loader
        this.cache = cache
        pdfLayout.setLoader(loader, cache)
    }

    override fun changeShowType(type: ViewShowType) {
        val tempLoader = loader ?: return
        val tempCache = cache ?: return
        pdfLayout.setShowType(type)
        pdfLayout.setLoader(tempLoader, tempCache)
    }

}