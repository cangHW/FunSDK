package com.proxy.service.document.pdf.info.loader.source

import android.os.ParcelFileDescriptor
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.document.pdf.base.config.enums.LoadErrorEnum
import com.proxy.service.document.pdf.base.config.source.UriSource
import com.proxy.service.document.pdf.info.core.PdfiumCore
import com.proxy.service.document.pdf.info.loader.utils.FileUtils

/**
 * @author: cangHX
 * @data: 2025/5/6 20:04
 * @desc:
 */
class UriSourceController(private val source: UriSource) : BaseController() {

    private var pfd: ParcelFileDescriptor? = null

    override fun getDocumentInfo(): Result {
        val pair = FileUtils.getNumFd(source.uri)
        pfd = pair.first
        val fd = pair.second
        if (fd != -1) {
            val result = PdfiumCore.getInstance().nativeOpenDocumentByFd(fd, source.password)
            if (result.hand != -1L) {
                return Result.success(this, result.hand)
            }
            return Result.failed(result.errCode)
        }
        return Result.failed(LoadErrorEnum.FILE_ERROR.errorCode)
    }

    override fun destroy() {
        CsFileUtils.close(pfd)
    }
}