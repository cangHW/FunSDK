package com.proxy.service.document.pdf.loader.source

import com.proxy.service.document.base.config.pdf.source.ByteArraySource
import com.proxy.service.document.pdf.core.PdfiumCore

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