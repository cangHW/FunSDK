package com.proxy.service.imageloader.info.glide.request.source

import com.bumptech.glide.RequestBuilder

/**
 * @author: cangHX
 * @data: 2024/5/16 11:37
 * @desc:
 */
class AssetGlideSource(private val fileName: String) : BaseGlideSourceData() {
    override fun <R> load(builder: RequestBuilder<R>?): RequestBuilder<R>? {
        return builder?.load("file:///android_asset/$fileName")
    }

    override fun hashCode(): Int {
        return fileName.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is AssetGlideSource){
            return fileName == other.fileName
        }
        return false
    }
}