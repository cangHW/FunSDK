package com.proxy.service.document.pdf.info.loader.source

import com.proxy.service.document.pdf.base.config.source.ByteArraySource
import com.proxy.service.document.pdf.info.core.PdfiumCore

/**
 * @author: cangHX
 * @data: 2025/5/2 20:48
 * @desc:
 */
class ByteArraySourceController(private val source: ByteArraySource) : BaseController() {

    override fun getDocumentInfo(): Result {
        val result =
            PdfiumCore.getInstance().nativeOpenDocumentByMem(source.byteArray, source.password)
        if (result.hand != -1L) {
            return Result.success(this, result.hand)
        }
        return Result.failed(result.errCode)
    }

    override fun destroy() {
        // nothing
    }
}