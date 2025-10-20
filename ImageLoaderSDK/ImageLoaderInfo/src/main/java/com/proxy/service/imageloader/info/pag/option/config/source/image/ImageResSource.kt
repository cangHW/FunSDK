package com.proxy.service.imageloader.info.pag.option.config.source.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.proxy.service.core.framework.app.context.CsContextManager
import org.libpag.PAGImage

/**
 * @author: cangHX
 * @data: 2025/10/16 18:07
 * @desc:
 */
class ImageResSource(
    @RawRes @DrawableRes private val resourceId: Int
) : BaseImageSource() {

    companion object {
        private val DEFAULT_BITMAP by lazy {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }
    }

    override fun loadImage(callback: IImageLoadCallback) {
        if (resourceId == 0) {
            callback.onResult(PAGImage.FromBitmap(DEFAULT_BITMAP))
            return
        }
        Glide.with(CsContextManager.getApplication())
            .asBitmap()
            .load(resourceId)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    try {
                        val image = PAGImage.FromBitmap(resource)
                        if (image == null) {
                            callback.onError(IllegalArgumentException("The image loading failed. resourceId=$resourceId"))
                            return
                        }
                        callback.onResult(image)
                    } catch (throwable: Throwable) {
                        callback.onError(throwable)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    callback.onError(null)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    callback.onError(null)
                }
            })
    }

    override fun toString(): String {
        return "ImageResSource(resourceId=$resourceId)"
    }
}