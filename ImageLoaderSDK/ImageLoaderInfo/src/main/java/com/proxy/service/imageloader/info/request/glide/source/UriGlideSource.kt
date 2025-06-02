package com.proxy.service.imageloader.info.request.glide.source

import android.net.Uri
import com.bumptech.glide.RequestBuilder

/**
 * @author: cangHX
 * @data: 2024/5/16 11:36
 * @desc:
 */
class UriGlideSource(private val uri: Uri) : BaseGlideSourceData() {

    override fun <R> load(builder: RequestBuilder<R>?): RequestBuilder<R>? {
        return builder?.load(uri)
    }

    override fun hashCode(): Int {
        return uri.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is UriGlideSource) {
            return uri == other.uri
        }
        return false
    }
}