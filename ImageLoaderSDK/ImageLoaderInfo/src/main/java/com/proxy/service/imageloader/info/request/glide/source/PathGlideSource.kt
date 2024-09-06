package com.proxy.service.imageloader.info.request.glide.source

import com.bumptech.glide.RequestBuilder

/**
 * @author: cangHX
 * @data: 2024/5/16 11:36
 * @desc:
 */
class PathGlideSource(private val path: String) : BaseGlideSourceData() {
    override fun <R> load(builder: RequestBuilder<R>?): RequestBuilder<R>? {
        return builder?.load(path)
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is PathGlideSource) {
            return path == other.path
        }
        return false
    }
}