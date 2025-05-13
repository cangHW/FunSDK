package com.proxy.service.document.pdf.loader.impl

import com.proxy.service.core.service.task.CsTask
import com.proxy.service.document.base.config.pdf.PdfConfig
import com.proxy.service.document.base.config.pdf.callback.LoadStateCallback
import com.proxy.service.document.base.config.pdf.enums.LoadErrorEnum
import com.proxy.service.document.base.config.pdf.info.FailedResult
import com.proxy.service.document.base.config.pdf.source.AssetPathSource
import com.proxy.service.document.base.config.pdf.source.BaseSource
import com.proxy.service.document.base.config.pdf.source.ByteArraySource
import com.proxy.service.document.base.config.pdf.source.FilePathSource
import com.proxy.service.document.base.config.pdf.source.FileSource
import com.proxy.service.document.base.pdf.IPdfLoader
import com.proxy.service.document.pdf.constants.Constants
import com.proxy.service.document.pdf.loader.source.BaseController
import java.io.File

/**
 * @author: cangHX
 * @data: 2025/4/30 14:56
 * @desc:
 */
class PdfLoader(private val config: PdfConfig) : PdfRender(), IPdfLoader {

    private val handler = CsTask.launchTaskGroup(Constants.SOURCE_THREAD)

    fun setSourceList(list: ArrayList<BaseSource>) {
        handler?.start {
            val success = ArrayList<BaseSource>()
            val failed = ArrayList<FailedResult>()
            synchronized(lock) {
                config.getLoadStateCallback()?.onLoadStart(ArrayList(list))
                var pageStart = 0
                list.forEach { source ->
                    val result = BaseController.findController(source)?.getDocumentInfo()
                    if (result?.document != null) {
                        result.document.setPageStart(pageStart)
                        pageStart += result.document.getPageCount()
                        docs.add(result.document)
                        success.add(source)
                    } else {
                        failed.add(FailedResult(source, result?.error ?: LoadErrorEnum.UNKNOWN))
                    }
                }
            }
            config.getLoadStateCallback()?.onLoadComplete(success, failed)
        }
    }

    override fun addSourceAssetPath(
        assetPath: String,
        password: String?,
        callback: LoadStateCallback?
    ) {
        addSourceAssetPath(docs.size, assetPath, password, callback)
    }

    override fun addSourceAssetPath(
        index: Int,
        assetPath: String,
        password: String?,
        callback: LoadStateCallback?
    ) {
        handler?.start {
            val source = AssetPathSource(assetPath, password)
            addSource(index, source, callback)
        }
    }

    override fun addSourceFilePath(
        filePath: String,
        password: String?,
        callback: LoadStateCallback?
    ) {
        addSourceFilePath(docs.size, filePath, password)
    }

    override fun addSourceFilePath(
        index: Int,
        filePath: String,
        password: String?,
        callback: LoadStateCallback?
    ) {
        handler?.start {
            val source = FilePathSource(filePath, password)
            addSource(index, source, callback)
        }
    }

    override fun addSourceFile(file: File, password: String?, callback: LoadStateCallback?) {
        addSourceFile(docs.size, file, password)
    }

    override fun addSourceFile(
        index: Int,
        file: File,
        password: String?,
        callback: LoadStateCallback?
    ) {
        handler?.start {
            val source = FileSource(file, password)
            addSource(index, source, callback)
        }
    }

    override fun addSourceData(bytes: ByteArray, password: String?, callback: LoadStateCallback?) {
        addSourceData(docs.size, bytes, password)
    }

    override fun addSourceData(
        index: Int,
        bytes: ByteArray,
        password: String?,
        callback: LoadStateCallback?
    ) {
        handler?.start {
            val source = ByteArraySource(bytes, password)
            addSource(index, source, callback)
        }
    }

    override fun destroy() {
        synchronized(lock) {
            docs.forEach {
                it.close()
            }
            docs.clear()
        }
    }


    private fun addSource(index: Int, source: BaseSource, callback: LoadStateCallback?) {
        var result: BaseController.Result?

        synchronized(lock) {
            callback?.onLoadStart(listOf(source))
            result = BaseController.findController(source)?.getDocumentInfo()
            result?.document?.let { document ->
                if (docs.size == index) {
                    document.setPageStart(getPageCount())
                }
                docs.add(index, document)
                var nextPageStart = docs.getOrNull(index - 1)?.getPageEnd()?.let {
                    it + 1
                } ?: let {
                    0
                }

                var tempIndex = index
                while (tempIndex < docs.size) {
                    docs.getOrNull(tempIndex)?.let {
                        it.setPageStart(nextPageStart)
                        nextPageStart += it.getPageCount()
                    }
                    tempIndex++
                }
            }
        }

        if (result?.document != null) {
            callback?.onLoadComplete(listOf(source), listOf())
        } else {
            callback?.onLoadComplete(
                listOf(),
                listOf(FailedResult(source, result?.error ?: LoadErrorEnum.UNKNOWN))
            )
        }
    }

}