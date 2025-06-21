package com.proxy.service.document.pdf.base.config

import android.net.Uri
import com.proxy.service.document.pdf.base.config.builder.IPdfBuilder
import com.proxy.service.document.pdf.base.config.builder.IPdfBuilderGet
import com.proxy.service.document.pdf.base.config.source.AssetPathSource
import com.proxy.service.document.pdf.base.config.source.BaseSource
import com.proxy.service.document.pdf.base.config.source.ByteArraySource
import com.proxy.service.document.pdf.base.config.source.FilePathSource
import com.proxy.service.document.pdf.base.config.source.FileSource
import com.proxy.service.document.pdf.base.config.source.InputStreamSource
import com.proxy.service.document.pdf.base.config.source.UriSource
import java.io.File
import java.io.InputStream

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

    companion object {
        fun builder(): IPdfBuilder {
            return Builder()
        }
    }

    private class Builder : IPdfBuilder, IPdfBuilderGet {

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

        override fun setSourceInputStream(
            inputStream: InputStream,
            password: String?
        ): IPdfBuilder {
            sources.clear()
            sources.add(InputStreamSource(inputStream, password))
            return this
        }

        override fun setSourceUri(uri: Uri, password: String?): IPdfBuilder {
            sources.clear()
            sources.add(UriSource(uri, password))
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

        override fun addSourceInputStream(
            inputStream: InputStream,
            password: String?
        ): IPdfBuilder {
            sources.add(InputStreamSource(inputStream, password))
            return this
        }

        override fun addSourceUri(uri: Uri, password: String?): IPdfBuilder {
            sources.add(UriSource(uri, password))
            return this
        }

        override fun build(): PdfConfig {
            return PdfConfig(this)
        }

        override fun getSources(): ArrayList<BaseSource> {
            return sources
        }
    }

}