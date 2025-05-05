package com.proxy.service.document.pdf.loader.source

import android.os.ParcelFileDescriptor
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.document.base.config.pdf.enums.LoadErrorEnum
import com.proxy.service.document.base.config.pdf.source.AssetPathSource
import com.proxy.service.document.pdf.core.PdfiumCore
import com.proxy.service.document.pdf.loader.utils.FileUtils
import java.io.File
import java.io.IOException

/**
 * @author: cangHX
 * @data: 2025/5/2 20:48
 * @desc:
 */
class AssetPathSourceController(private val source: AssetPathSource) : BaseController() {

    private var pfd: ParcelFileDescriptor? = null

    override fun getDocumentInfo(): Result {
        val file = fileFromAsset(source.assetPath)
        val pair = FileUtils.getNumFd(file)
        pfd = pair.first
        val fd = pair.second
        if (fd != -1) {
            val result = PdfiumCore.getInstance().nativeOpenDocumentByFd(fd, source.password)
            if (result.hand != -1L){
                return Result.success(this, result.hand)
            }
            return Result.failed(result.errCode)
        }
        return Result.failed(LoadErrorEnum.FILE_ERROR.errorCode)
    }

    override fun destroy() {
        CsFileUtils.close(pfd)
    }

    @Throws(IOException::class)
    private fun fileFromAsset(assetPath: String): File {
        val context = CsContextManager.getApplication()
        val outFile = File(context.cacheDir, "${assetPath}-cs_pdf.pdf")
//        if (assetPath.contains("/")) {
//            outFile.parentFile?.mkdirs()
//        }
        CsFileWriteUtils.setSourceAssetPath(assetPath).writeSync(outFile)
        return outFile
    }
}