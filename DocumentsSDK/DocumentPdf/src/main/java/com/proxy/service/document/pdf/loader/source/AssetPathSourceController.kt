package com.proxy.service.document.pdf.loader.source

import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.document.base.pdf.config.enums.LoadErrorEnum
import com.proxy.service.document.base.pdf.config.source.AssetPathSource
import com.proxy.service.document.pdf.core.OpenResult
import com.proxy.service.document.pdf.core.PdfiumCore
import java.io.File
import java.io.IOException

/**
 * @author: cangHX
 * @data: 2025/5/2 20:48
 * @desc:
 */
class AssetPathSourceController(private val source: AssetPathSource) : BaseController() {

    override fun getDocumentInfo(): Result {
        var result: OpenResult? = null
        try {
            val filePath = fileFromAsset(source.assetPath)
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
    private fun fileFromAsset(assetPath: String): String {
        val context = CsContextManager.getApplication()
        val outFile = File(context.cacheDir, "${assetPath}-AssetPdf.pdf")
        CsFileWriteUtils.setSourceAssetPath(assetPath).writeSync(outFile)
        return outFile.absolutePath
    }
}