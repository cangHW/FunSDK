package com.proxy.service.document.pdf.info

import com.proxy.service.annotations.CloudApiService
import com.proxy.service.document.pdf.base.PdfService
import com.proxy.service.document.pdf.base.config.PdfConfig
import com.proxy.service.document.pdf.base.config.callback.LoadStateCallback
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.base.view.IPdfViewLoader
import com.proxy.service.document.pdf.info.loader.impl.PdfLoader
import com.proxy.service.document.pdf.info.view.PdfViewLoaderImpl

/**
 * @author: cangHX
 * @data: 2025/4/30 14:55
 * @desc:
 */
@CloudApiService(serviceTag = "service/pdf")
class PdfServiceImpl : PdfService {

    override fun createLoader(config: PdfConfig, callback: LoadStateCallback): IPdfLoader {
        val loader = PdfLoader()
        loader.setSourceList(config.getSources(), callback)
        return loader
    }

    override fun createViewFactory(config: PdfConfig): IPdfViewLoader {
        return PdfViewLoaderImpl(config)
    }
}