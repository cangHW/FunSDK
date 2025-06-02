package com.proxy.service.imageloader.info.request.glide.source

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder

/**
 * @author: cangHX
 * @data: 2025/5/30 11:41
 * @desc:
 */
class DrawableGlideSource(private val drawable: Drawable) : BaseGlideSourceData() {

    override fun <R> load(builder: RequestBuilder<R>?): RequestBuilder<R>? {
        return builder?.load(drawable)
    }

    override fun hashCode(): Int {
        return "$drawable".hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is DrawableGlideSource) {
            return drawable == other.drawable
        }
        return false
    }
}