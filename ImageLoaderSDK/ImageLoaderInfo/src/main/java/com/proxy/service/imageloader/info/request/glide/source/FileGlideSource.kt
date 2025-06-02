package com.proxy.service.imageloader.info.request.glide.source

import com.bumptech.glide.RequestBuilder
import java.io.File

/**
 * @author: cangHX
 * @data: 2024/5/16 11:36
 * @desc:
 */
class FileGlideSource(private val file: File) : BaseGlideSourceData() {
    override fun <R> load(builder: RequestBuilder<R>?): RequestBuilder<R>? {
        return builder?.load(file)
    }

    override fun hashCode(): Int {
        return "${file.absoluteFile}".hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is FileGlideSource) {
            return file == other.file
        }
        return false
    }
}