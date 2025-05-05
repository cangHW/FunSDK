package com.proxy.service.document.base.config.pdf

import com.proxy.service.document.base.config.pdf.builder.IPdfBuilder
import com.proxy.service.document.base.config.pdf.builder.IPdfBuilderGet
import com.proxy.service.document.base.config.pdf.callback.LoadStateCallback
import com.proxy.service.document.base.config.pdf.source.AssetPathSource
import com.proxy.service.document.base.config.pdf.source.BaseSource
import com.proxy.service.document.base.config.pdf.source.ByteArraySource
import com.proxy.service.document.base.config.pdf.source.FilePathSource
import com.proxy.service.document.base.config.pdf.source.FileSource
import java.io.File

/**
 * @author: cangHX
 * @data: 2025/4/29 22:08
 * @desc:
 */
class PdfConfig private constructor(
    private val builder: IPdfBuilderGet
) : IPdfBuilderGet {

    override fun getSources(): ArrayList<BaseSource> {
        return builder.getSources()
    }

    override fun getLoadStateCallback(): LoadStateCallback? {
        return builder.getLoadStateCallback()
    }

    companion object {
        fun builder(): IPdfBuilder {
            return Builder()
        }
    }

    private class Builder : IPdfBuilder, IPdfBuilderGet {

        private var loadCallback: LoadStateCallback? = null
        private val sources = ArrayList<BaseSource>()

        override fun setSourceAssetPath(assetPath: String, password: String?): IPdfBuilder {
            sources.clear()
            sources.add(AssetPathSource(assetPath, password))
            return this
        }

        override fun setSourceFilePath(filePath: String, password: String?): IPdfBuilder {
            sources.clear()
            sources.add(FilePathSource(filePath, password))
            return this
        }

        override fun setSourceFile(file: File, password: String?): IPdfBuilder {
            sources.clear()
            sources.add(FileSource(file, password))
            return this
        }

        override fun setSourceByteArray(bytes: ByteArray, password: String?): IPdfBuilder {
            sources.clear()
            sources.add(ByteArraySource(bytes, password))
            return this
        }

        override fun addSourceAssetPath(assetPath: String, password: String?): IPdfBuilder {
            sources.add(AssetPathSource(assetPath, password))
            return this
        }

        override fun addSourceFilePath(filePath: String, password: String?): IPdfBuilder {
            sources.add(FilePathSource(filePath, password))
            return this
        }

        override fun addSourceFile(file: File, password: String?): IPdfBuilder {
            sources.add(FileSource(file, password))
            return this
        }

        override fun addSourceByteArray(bytes: ByteArray, password: String?): IPdfBuilder {
            sources.add(ByteArraySource(bytes, password))
            return this
        }

        override fun setLoadCallback(callback: LoadStateCallback): IPdfBuilder {
            this.loadCallback = callback
            return this
        }

        override fun build(): PdfConfig {
            return PdfConfig(this)
        }

        override fun getSources(): ArrayList<BaseSource> {
            return sources
        }

        override fun getLoadStateCallback(): LoadStateCallback? {
            return loadCallback
        }
    }

}