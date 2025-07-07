package com.proxy.service.imageloader.info.info.glide

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation
import com.bumptech.glide.load.MultiTransformation
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.base.drawable.CsWebpDrawable
import com.proxy.service.imageloader.base.option.glide.callback.AnimationCallback

/**
 * @author: cangHX
 * @data: 2025/7/6 10:17
 * @desc:
 */
class WebpInfo : GlideInfo<Drawable>() {

    var isAutoPlay: Boolean = ImageLoaderConstants.IS_AUTO_PLAY
    var loopCount: Int = -1

    var animationCallback: AnimationCallback<CsWebpDrawable>? = null

    override fun getRequestBuilder(): RequestBuilder<Drawable>? {
        var builder = sourceType?.load(getRequestManager())
        builder = sourceData?.load(builder)

        if (transformList.size > 1) {
            val transformation = MultiTransformation(*(transformList.toTypedArray()))
            requestOptions = requestOptions.optionalTransform(
                WebpDrawable::class.java,
                WebpDrawableTransformation(transformation)
            )
        } else if (transformList.size == 1) {
            requestOptions = requestOptions.optionalTransform(
                WebpDrawable::class.java,
                WebpDrawableTransformation(transformList.get(0))
            )
        }
        builder = builder?.apply(requestOptions)

        return builder
    }

}