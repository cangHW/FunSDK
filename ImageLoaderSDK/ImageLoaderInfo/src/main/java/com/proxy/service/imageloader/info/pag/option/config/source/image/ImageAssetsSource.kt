package com.proxy.service.imageloader.info.pag.option.config.source.image

import android.text.TextUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import org.libpag.PAGImage

/**
 * @author: cangHX
 * @data: 2025/10/16 18:07
 * @desc:
 */
class ImageAssetsSource(
    private val fileName: String
) : BaseImageSource() {

    override fun loadImage(callback: IImageLoadCallback) {
        if (TextUtils.isEmpty(fileName)) {
            callback.onError(IllegalArgumentException("The image assets fileName cannot be empty. fileName=$fileName"))
            return
        }

        val context = CsContextManager.getApplication()
        val image = PAGImage.FromAssets(context.assets, fileName)
        if (image == null) {
            callback.onError(IllegalArgumentException("The image loading failed. assetsName=$fileName"))
            return
        }
        callback.onResult(image)
    }

    override fun toString(): String {
        return "ImageAssetsSource(fileName=$fileName)"
    }
}