package com.proxy.service.imageloader.info.pag.option.config.source.image

import org.libpag.PAGImage

/**
 * @author: cangHX
 * @data: 2025/10/16 18:04
 * @desc:
 */
abstract class BaseImageSource {

    interface IImageLoadCallback {

        fun onResult(pagImage: PAGImage)

        fun onError(result: Throwable?)

    }

    open fun preLoad() {}

    abstract fun loadImage(callback: IImageLoadCallback)

}