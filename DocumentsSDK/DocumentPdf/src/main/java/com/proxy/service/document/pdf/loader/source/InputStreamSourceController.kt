package com.proxy.service.document.pdf.loader.source

import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.document.base.pdf.config.enums.LoadErrorEnum
import com.proxy.service.document.base.pdf.config.source.InputStreamSource
import com.proxy.service.document.pdf.core.OpenResult
import com.proxy.service.document.pdf.core.PdfiumCore
import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * @author: cangHX
 * @data: 2025/5/6 20:04
 * @desc:
 */
class InputStreamSourceController(private val source: InputStreamSource) : BaseController() {

    override fun getDocumentInfo(): Result {
        var result: OpenResult? = null
        try {
            val filePath = fileFromInputStream(source.inputStream)
            result = PdfiumCore.getInstance().nativeOpenDocumentByPath(filePath, source.password)
            if (result.hand != -1L) {
                return Result.success(this, result.hand)
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return Result.failed(result?.errCode ?: LoadErrorEnum.UNKNOWN.errorCode)
    }

    override fun destroy() {
        // nothing
    }

    @Throws(IOException::class)
    private fun fileFromInputStream(inputStream: InputStream): String {
        val context = CsContextManager.getApplication()
        val outFile = File(context.cacheDir, "InputStreamPdf.pdf")
        CsFileWriteUtils.setSourceStream(inputStream).writeSync(outFile)
        return outFile.absolutePath
    }
}