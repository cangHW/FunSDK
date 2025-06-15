package com.proxy.service.document.pdf.info

import com.proxy.service.annotations.CloudApiService
import com.proxy.service.document.pdf.base.PdfService
import com.proxy.service.document.pdf.base.config.PdfConfig
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.base.view.IViewFactory
import com.proxy.service.document.pdf.info.loader.impl.PdfLoader
import com.proxy.service.document.pdf.info.view.ViewFactoryImpl

/**
 * @author: cangHX
 * @data: 2025/4/30 14:55
 * @desc:
 */
@CloudApiService(serviceTag = "service/pdf")
class PdfServiceImpl : PdfService {

    override fun createLoader(config: PdfConfig): IPdfLoader {
        val loader = PdfLoader()
        loader.setSourceList(config.getSources(), config.getLoadStateCallback())
        return loader
    }

    override fun createViewFactory(config: PdfConfig): IViewFactory {
        return ViewFactoryImpl(config)
    }
}