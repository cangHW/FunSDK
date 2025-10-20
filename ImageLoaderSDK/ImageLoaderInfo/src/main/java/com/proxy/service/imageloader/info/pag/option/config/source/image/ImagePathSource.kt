package com.proxy.service.imageloader.info.pag.option.config.source.image

import android.text.TextUtils
import org.libpag.PAGImage

/**
 * @author: cangHX
 * @data: 2025/10/16 18:07
 * @desc:
 */
class ImagePathSource(
    private val path: String
) : BaseImageSource() {

    override fun loadImage(callback: IImageLoadCallback) {
        if (TextUtils.isEmpty(path)) {
            callback.onError(IllegalArgumentException("The image path cannot be empty. path=$path"))
            return
        }

        val image = PAGImage.FromPath(path)
        if (image == null) {
            callback.onError(IllegalArgumentException("The image loading failed. path=$path"))
            return
        }
        callback.onResult(image)
    }

    override fun toString(): String {
        return "ImagePathSource(path=$path)"
    }
}