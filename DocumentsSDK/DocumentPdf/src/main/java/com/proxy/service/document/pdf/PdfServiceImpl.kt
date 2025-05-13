package com.proxy.service.document.pdf

import com.proxy.service.annotations.CloudApiService
import com.proxy.service.document.base.PdfService
import com.proxy.service.document.base.config.pdf.PdfConfig
import com.proxy.service.document.base.pdf.IPdfLoader
import com.proxy.service.document.pdf.loader.impl.PdfLoader

/**
 * @author: cangHX
 * @data: 2025/4/30 14:55
 * @desc:
 */
@CloudApiService(serviceTag = "service/pdf")
class PdfServiceImpl:PdfService {

    override fun createLoader(config: PdfConfig): IPdfLoader {
        val loader = PdfLoader(config)
        loader.setSourceList(config.getSources())

        return loader
    }
}