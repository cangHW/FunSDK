package com.proxy.service.imageloader.info.pag.option.config.source.image

import com.proxy.service.core.framework.app.context.CsContextManager
import org.libpag.PAGImage

/**
 * @author: cangHX
 * @data: 2025/10/16 18:07
 * @desc:
 */
class ImageAssetsSource(
    private val assetsName: String
) : BaseImageSource() {

    override fun loadImage(callback: IImageLoadCallback) {
        val context = CsContextManager.getApplication()
        val image = PAGImage.FromAssets(context.assets, assetsName)
        if (image == null) {
            callback.onError(IllegalArgumentException("The image loading failed. assetsName=$assetsName"))
            return
        }
        callback.onResult(image)
    }

    override fun toString(): String {
        return "ImageAssetsSource(assetsName=$assetsName)"
    }
}