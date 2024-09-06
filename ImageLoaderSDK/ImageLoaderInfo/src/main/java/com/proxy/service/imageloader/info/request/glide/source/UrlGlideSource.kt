package com.proxy.service.imageloader.info.request.glide.source

import com.bumptech.glide.RequestBuilder

/**
 * @author: cangHX
 * @data: 2024/5/16 11:36
 * @desc:
 */
class UrlGlideSource(private val url: String) : BaseGlideSourceData() {
    override fun <R> load(builder: RequestBuilder<R>?): RequestBuilder<R>? {
        return builder?.load(url)
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is UrlGlideSource) {
            return url == other.url
        }
        return false
    }
}