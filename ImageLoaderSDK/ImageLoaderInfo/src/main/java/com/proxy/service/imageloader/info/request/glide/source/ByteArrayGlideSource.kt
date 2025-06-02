package com.proxy.service.imageloader.info.request.glide.source

import com.bumptech.glide.RequestBuilder

/**
 * @author: cangHX
 * @data: 2024/5/16 11:36
 * @desc:
 */
class ByteArrayGlideSource(private val bytes: ByteArray) : BaseGlideSourceData() {
    override fun <R> load(builder: RequestBuilder<R>?): RequestBuilder<R>? {
        return builder?.load(bytes)
    }

    override fun hashCode(): Int {
        return "$bytes".hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is ByteArrayGlideSource) {
            return bytes.contentEquals(other.bytes)
        }
        return false
    }
}