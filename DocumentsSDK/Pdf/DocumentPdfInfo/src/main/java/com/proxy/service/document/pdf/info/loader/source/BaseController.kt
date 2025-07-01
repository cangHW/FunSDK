package com.proxy.service.document.pdf.info.loader.source

import com.proxy.service.document.pdf.base.config.enums.LoadErrorEnum
import com.proxy.service.document.pdf.base.config.source.AssetPathSource
import com.proxy.service.document.pdf.base.config.source.BaseSource
import com.proxy.service.document.pdf.base.config.source.ByteArraySource
import com.proxy.service.document.pdf.base.config.source.FilePathSource
import com.proxy.service.document.pdf.base.config.source.FileSource
import com.proxy.service.document.pdf.base.config.source.InputStreamSource
import com.proxy.service.document.pdf.base.config.source.UriSource
import com.proxy.service.document.pdf.base.constants.PdfConstants
import com.proxy.service.document.pdf.info.loader.info.DocumentInfo

/**
 * @author: cangHX
 * @data: 2025/5/2 20:49
 * @desc:
 */
abstract class BaseController {

    companion object {

        const val TAG = "${PdfConstants.LOG_TAG_PDF_START}Controller"

        fun findController(source: BaseSource): BaseController? {
            when (source) {
                is AssetPathSource -> {
                    return AssetPathSourceController(source)
                }

                is FilePathSource -> {
                    return FilePathSourceController(source)
                }

                is FileSource -> {
                    return FileSourceController(source)
                }

                is ByteArraySource -> {
                    return ByteArraySourceController(source)
                }

                is InputStreamSource -> {
                    return InputStreamSourceController(source)
                }

                is UriSource -> {
                    return UriSourceController(source)
                }

                else -> return null
            }
        }

    }

    class Result private constructor(val document: DocumentInfo?, val error: LoadErrorEnum?) {

        companion object {
            fun success(controller: BaseController, hand: Long): Result {
                return Result(DocumentInfo(controller, hand), null)
            }

            fun failed(errCode: Long): Result {
                return Result(null, LoadErrorEnum.valueOf(errCode))
            }
        }

    }

    abstract fun getDocumentInfo(): Result

    abstract fun destroy()
}