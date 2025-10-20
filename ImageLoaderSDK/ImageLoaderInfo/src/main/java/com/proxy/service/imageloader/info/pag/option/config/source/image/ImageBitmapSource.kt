package com.proxy.service.imageloader.info.pag.option.config.source.image

import android.graphics.Bitmap
import org.libpag.PAGImage

/**
 * @author: cangHX
 * @data: 2025/10/16 18:07
 * @desc:
 */
class ImageBitmapSource(
    private val bitmap: Bitmap
) : BaseImageSource() {

    override fun loadImage(callback: IImageLoadCallback) {
        val image = PAGImage.FromBitmap(bitmap)
        if (image == null) {
            callback.onError(IllegalArgumentException("The image loading failed. bitmap=$bitmap"))
            return
        }
        callback.onResult(image)
    }

    override fun toString(): String {
        return "ImageBitmapSource(bitmap=$bitmap)"
    }
}