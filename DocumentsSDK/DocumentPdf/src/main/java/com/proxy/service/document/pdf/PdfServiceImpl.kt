package com.proxy.service.document.pdf

import android.view.ViewGroup
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.document.base.PdfService
import com.proxy.service.document.base.pdf.config.PdfConfig
import com.proxy.service.document.base.pdf.loader.IPdfLoader
import com.proxy.service.document.base.pdf.view.IPdfView
import com.proxy.service.document.base.pdf.view.IViewFactory
import com.proxy.service.document.pdf.lifecycle.LifecycleManager
import com.proxy.service.document.pdf.loader.impl.PdfLoader
import com.proxy.service.document.pdf.view.PdfViewImpl
import com.proxy.service.document.pdf.view.ViewFactoryImpl

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