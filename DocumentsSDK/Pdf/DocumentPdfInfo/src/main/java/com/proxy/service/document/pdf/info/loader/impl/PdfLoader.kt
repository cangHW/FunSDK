package com.proxy.service.document.pdf.info.loader.impl

import android.net.Uri
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.document.pdf.base.config.callback.LoadStateCallback
import com.proxy.service.document.pdf.base.config.enums.LoadErrorEnum
import com.proxy.service.document.pdf.base.config.info.FailedResult
import com.proxy.service.document.pdf.base.config.source.AssetPathSource
import com.proxy.service.document.pdf.base.config.source.BaseSource
import com.proxy.service.document.pdf.base.config.source.ByteArraySource
import com.proxy.service.document.pdf.base.config.source.FilePathSource
import com.proxy.service.document.pdf.base.config.source.FileSource
import com.proxy.service.document.pdf.base.config.source.InputStreamSource
import com.proxy.service.document.pdf.base.config.source.UriSource
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.info.constants.Constants
import com.proxy.service.document.pdf.info.loader.source.BaseController
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.File
import java.io.InputStream

/**
 * @author: cangHX
 * @data: 2025/4/30 14:56
 * @desc:
 */
class PdfLoader : PdfRender(), IPdfLoader {

    private val handler = CsTask.launchTaskGroup(Constants.SOURCE_THREAD)

    fun setSourceList(list: ArrayList<BaseSource>, callback: LoadStateCallback?) {
        handler?.start {
            val success = ArrayList<BaseSource>()
            val failed = ArrayList<FailedResult>()
            synchronized(lock) {
                callStart(callback, ArrayList(list))
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
            callComplete(callback, success, failed)
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
        if (isNotReady()) {
            return
        }
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
        addSourceFilePath(docs.size, filePath, password, callback)
    }

    override fun addSourceFilePath(
        index: Int,
        filePath: String,
        password: String?,
        callback: LoadStateCallback?
    ) {
        if (isNotReady()) {
            return
        }
        handler?.start {
            val source = FilePathSource(filePath, password)
            addSource(index, source, callback)
        }
    }

    override fun addSourceFile(file: File, password: String?, callback: LoadStateCallback?) {
        addSourceFile(docs.size, file, password, callback)
    }

    override fun addSourceFile(
        index: Int,
        file: File,
        password: String?,
        callback: LoadStateCallback?
    ) {
        if (isNotReady()) {
            return
        }
        handler?.start {
            val source = FileSource(file, password)
            addSource(index, source, callback)
        }
    }

    override fun addSourceByteArray(
        bytes: ByteArray,
        password: String?,
        callback: LoadStateCallback?
    ) {
        addSourceByteArray(docs.size, bytes, password, callback)
    }

    override fun addSourceByteArray(
        index: Int,
        bytes: ByteArray,
        password: String?,
        callback: LoadStateCallback?
    ) {
        if (isNotReady()) {
            return
        }
        handler?.start {
            val source = ByteArraySource(bytes, password)
            addSource(index, source, callback)
        }
    }

    override fun addSourceInputStream(
        inputStream: InputStream,
        password: String?,
        callback: LoadStateCallback?
    ) {
        addSourceInputStream(docs.size, inputStream, password, callback)
    }

    override fun addSourceInputStream(
        index: Int,
        inputStream: InputStream,
        password: String?,
        callback: LoadStateCallback?
    ) {
        if (isNotReady()) {
            return
        }
        handler?.start {
            val source = InputStreamSource(inputStream, password)
            addSource(index, source, callback)
        }
    }

    override fun addSourceUri(uri: Uri, password: String?, callback: LoadStateCallback?) {
        addSourceUri(docs.size, uri, password, callback)
    }

    override fun addSourceUri(
        index: Int,
        uri: Uri,
        password: String?,
        callback: LoadStateCallback?
    ) {
        if (isNotReady()) {
            return
        }
        handler?.start {
            val source = UriSource(uri, password)
            addSource(index, source, callback)
        }
    }

    override fun destroy() {
        if (isDestroy.compareAndSet(false, true)) {
            handler?.start {
                synchronized(lock) {
                    docs.forEach {
                        it.close()
                    }
                    docs.clear()
                }
            }
        }
    }


    private fun addSource(index: Int, source: BaseSource, callback: LoadStateCallback?) {
        var result: BaseController.Result?

        synchronized(lock) {
            if (isNotReady()) {
                return
            }

            callStart(callback, listOf(source))
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
            callComplete(callback, listOf(source), listOf())
        } else {
            callComplete(
                callback,
                listOf(),
                listOf(FailedResult(source, result?.error ?: LoadErrorEnum.UNKNOWN))
            )
        }
    }

    private fun callStart(callback: LoadStateCallback?, sources: List<BaseSource>) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                callback?.onLoadStart(sources)
                return ""
            }
        })?.start()
    }

    private fun callComplete(
        callback: LoadStateCallback?,
        success: List<BaseSource>,
        failed: List<FailedResult>
    ) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                callback?.onLoadComplete(success, failed)
                return ""
            }
        })?.start()
    }

}