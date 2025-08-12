package com.proxy.service.core.service.document

import com.proxy.service.api.CloudSystem
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.pdf.base.PdfService
import com.proxy.service.document.pdf.base.config.PdfConfig
import com.proxy.service.document.pdf.base.config.callback.LoadStateCallback
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.base.view.IPdfViewLoader

/**
 * 文档-pdf 加载框架入口
 *
 * @author: cangHX
 * @data: 2025/6/7 14:45
 * @desc:
 */
object CsDocumentPdf {

    private const val TAG = "${CoreConfig.TAG}DocumentPdf"

    private var service: PdfService? = null

    private fun getService(): PdfService? {
        if (service == null) {
            service = CloudSystem.getService(PdfService::class.java)
        }
        if (service == null) {
            CsLogger.tag(TAG)
                .e("Please check to see if it is referenced. <io.github.canghw:Service-Document-Pdf:xxx>")
        }
        return service
    }

    /**
     * 创建 pdf 加载器，如果需要自定义 view 则通过加载器控制
     * */
    fun createLoader(config: PdfConfig, callback: LoadStateCallback): IPdfLoader? {
        return getService()?.createLoader(config, callback)
    }

    /**
     * 创建 pdf 视图加载器, 如果需要使用默认视图
     * */
    fun createViewFactory(config: PdfConfig): IPdfViewLoader? {
        return getService()?.createViewFactory(config)
    }

}