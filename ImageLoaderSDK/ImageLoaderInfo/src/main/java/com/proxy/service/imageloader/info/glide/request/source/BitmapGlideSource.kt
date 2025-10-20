package com.proxy.service.imageloader.info.glide.request.source

import android.graphics.Bitmap
import com.bumptech.glide.RequestBuilder

/**
 * @author: cangHX
 * @data: 2025/5/30 11:41
 * @desc:
 */
class BitmapGlideSource(private val bitmap: Bitmap) : BaseGlideSourceData() {

    override fun <R> load(builder: RequestBuilder<R>?): RequestBuilder<R>? {
        return builder?.load(bitmap)
    }

    override fun hashCode(): Int {
        return "$bitmap".hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is BitmapGlideSource) {
            return bitmap == other.bitmap
        }
        return false
    }
}