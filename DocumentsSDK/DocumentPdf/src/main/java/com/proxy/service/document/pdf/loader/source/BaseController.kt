package com.proxy.service.document.pdf.loader.source

import com.proxy.service.document.base.config.pdf.enums.LoadErrorEnum
import com.proxy.service.document.base.config.pdf.source.AssetPathSource
import com.proxy.service.document.base.config.pdf.source.BaseSource
import com.proxy.service.document.base.config.pdf.source.ByteArraySource
import com.proxy.service.document.base.config.pdf.source.FilePathSource
import com.proxy.service.document.base.config.pdf.source.FileSource
import com.proxy.service.document.base.config.pdf.source.InputStreamSource
import com.proxy.service.document.base.config.pdf.source.UriSource
import com.proxy.service.document.base.constants.Constants
import com.proxy.service.document.pdf.loader.info.DocumentInfo

/**
 * @author: cangHX
 * @data: 2025/5/2 20:49
 * @desc:
 */
abstract class BaseController {

    companion object {

        const val TAG = "${Constants.LOG_TAG_PDF_START}Controller"

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