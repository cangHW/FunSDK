package com.proxy.service.imageloader.info.glide.type

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.gif.GifDrawable

/**
 * @author: cangHX
 * @date: 2024/5/16 14:39
 * @desc:
 */
class GifDrawableType: BaseSourceType<GifDrawable>() {
    override fun load(requestManager: RequestManager?): RequestBuilder<GifDrawable>? {
        return requestManager?.asGif()
    }
}