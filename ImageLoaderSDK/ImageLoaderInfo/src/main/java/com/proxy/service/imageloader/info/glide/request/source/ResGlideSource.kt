package com.proxy.service.imageloader.info.glide.request.source

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.bumptech.glide.RequestBuilder

/**
 * @author: cangHX
 * @data: 2024/5/16 11:38
 * @desc:
 */
class ResGlideSource(
    @RawRes @DrawableRes private val resourceId: Int
) : BaseGlideSourceData() {

    override fun <R> load(builder: RequestBuilder<R>?): RequestBuilder<R>? {
        return builder?.load(resourceId)
    }

    override fun hashCode(): Int {
        return "$resourceId".hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is ResGlideSource) {
            return resourceId == other.resourceId
        }
        return false
    }
}