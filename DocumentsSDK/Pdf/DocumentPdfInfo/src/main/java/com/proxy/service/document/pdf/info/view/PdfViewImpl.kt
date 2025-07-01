package com.proxy.service.document.pdf.info.view

import android.view.ViewGroup
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.base.view.IPdfView
import com.proxy.service.document.pdf.info.view.cache.PartCache
import com.proxy.service.document.pdf.info.view.group.IFactory
import com.proxy.service.document.pdf.info.view.view.TempView

/**
 * @author: cangHX
 * @data: 2025/5/14 16:05
 * @desc:
 */
class PdfViewImpl(viewGroup: ViewGroup) : IPdfView {

    private val pdfView = TempView(viewGroup.context)

    init {
        IFactory.of(viewGroup, pdfView)
    }

    fun setLoader(loader: IPdfLoader?, cache: PartCache) {
        if (loader == null) {
            return
        }
        pdfView.setLoader(loader, cache)
    }

}