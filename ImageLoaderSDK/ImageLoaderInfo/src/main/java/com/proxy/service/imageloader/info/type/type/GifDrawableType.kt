package com.proxy.service.imageloader.info.type.type

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.gif.GifDrawable

/**
 * @author: cangHX
 * @data: 2024/5/16 14:39
 * @desc:
 */
class GifDrawableType: BaseSourceType<GifDrawable>() {
    override fun load(requestManager: RequestManager?): RequestBuilder<GifDrawable>? {
        return requestManager?.asGif()
    }
}