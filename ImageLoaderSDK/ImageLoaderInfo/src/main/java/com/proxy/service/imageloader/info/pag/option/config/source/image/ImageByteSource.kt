package com.proxy.service.imageloader.info.pag.option.config.source.image

import org.libpag.PAGImage

/**
 * @author: cangHX
 * @data: 2025/10/16 18:07
 * @desc:
 */
class ImageByteSource(
    private val byteArray: ByteArray
) : BaseImageSource() {

    override fun loadImage(callback: IImageLoadCallback) {
        val image = PAGImage.FromBytes(byteArray)
        if (image == null) {
            callback.onError(IllegalArgumentException("The image loading failed. byteArray=$byteArray"))
            return
        }
        callback.onResult(image)
    }

    override fun toString(): String {
        return "ImageByteSource(byteArray=$byteArray)"
    }
}