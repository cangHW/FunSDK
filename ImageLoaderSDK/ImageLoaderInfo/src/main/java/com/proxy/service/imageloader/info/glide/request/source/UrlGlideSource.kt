package com.proxy.service.imageloader.info.glide.request.source

import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.signature.ObjectKey

/**
 * @author: cangHX
 * @data: 2024/5/16 11:36
 * @desc:
 */
class UrlGlideSource(
    private val url: String,
    private val key: String?
) : BaseGlideSourceData() {

    override fun <R> load(builder: RequestBuilder<R>?): RequestBuilder<R>? {
        val signature = key
        if (signature.isNullOrEmpty() || signature.isBlank()) {
            return builder?.load(url)
        }
        return builder?.load(url)?.signature(ObjectKey(signature))
    }

    override fun hashCode(): Int {
        return (url + key).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is UrlGlideSource) {
            return url == other.url && key == other.key
        }
        return false
    }
}