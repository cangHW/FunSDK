package com.proxy.service.imageloader.info.pag.option.config

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.base.option.pag.image.PagImageData
import com.proxy.service.imageloader.base.option.pag.image.PagScaleMode.CENTER_CROP
import com.proxy.service.imageloader.base.option.pag.image.PagScaleMode.CENTER_INSIDE
import com.proxy.service.imageloader.base.option.pag.image.PagScaleMode.FIT_XY
import com.proxy.service.imageloader.base.option.pag.image.PagScaleMode.NONE
import com.proxy.service.imageloader.info.pag.option.config.source.image.BaseImageSource
import com.proxy.service.imageloader.info.pag.option.config.source.image.BaseImageSource.IImageLoadCallback
import com.proxy.service.imageloader.info.pag.option.config.source.image.ImageAssetsSource
import com.proxy.service.imageloader.info.pag.option.config.source.image.ImageBitmapSource
import com.proxy.service.imageloader.info.pag.option.config.source.image.ImageByteSource
import com.proxy.service.imageloader.info.pag.option.config.source.image.ImagePathSource
import com.proxy.service.imageloader.info.pag.option.config.source.image.ImageResSource
import com.proxy.service.imageloader.info.pag.option.config.source.image.ImageUrlSource
import org.libpag.PAGFile
import org.libpag.PAGImage
import org.libpag.PAGScaleMode

/**
 * @author: cangHX
 * @data: 2025/10/13 20:39
 * @desc:
 */
class ImageConfig private constructor(
    private val index: Int?,
    private val name: String?,
    private val data: PagImageData
) : BaseConfig() {

    companion object {
        fun create(index: Int, data: PagImageData): ImageConfig {
            return ImageConfig(index, null, data)
        }

        fun create(name: String, data: PagImageData): ImageConfig {
            return ImageConfig(null, name, data)
        }
    }

    private var imageSource: BaseImageSource? = null

    override fun tryPreLoad() {
        try {
            val url = data.getImageUrl()
            if (!url.isNullOrEmpty() && url.isNotBlank()) {
                imageSource = ImageUrlSource(url, data.getImageUrlCacheKey())
                return
            }

            data.getImageBitmap()?.let {
                imageSource = ImageBitmapSource(it)
                return@tryPreLoad
            }

            val path = data.getImagePath()
            if (!path.isNullOrEmpty() && path.isNotBlank()) {
                imageSource = ImagePathSource(path)
                return
            }

            val assetsName = data.getImageAssetsName()
            if (!assetsName.isNullOrEmpty() && assetsName.isNotBlank()) {
                imageSource = ImageAssetsSource(assetsName)
                return
            }

            data.getImageByte()?.let {
                imageSource = ImageByteSource(it)
                return@tryPreLoad
            }

            data.getImageRes()?.let {
                imageSource = ImageResSource(it)
                return@tryPreLoad
            }
        } finally {
            imageSource?.preLoad()
        }
    }

    override fun load(pagFile: PAGFile, callback: IConfigLoadCallback) {
        if (index == null && name == null) {
            callback.onResult(pagFile)
            return
        }
        val iS = imageSource
        if (iS == null) {
            callback.onResult(pagFile)
            return
        }

        try {
            iS.loadImage(object : IImageLoadCallback {
                override fun onResult(pagImage: PAGImage) {
                    loadImageSuccess(pagFile, pagImage, callback)
                }

                override fun onError(result: Throwable?) {
                    loadImageError(result, pagFile, callback)
                }
            })
        } catch (throwable: Throwable) {
            loadImageError(throwable, pagFile, callback)
        }
    }

    private fun loadImageSuccess(
        pagFile: PAGFile,
        pagImage: PAGImage,
        callback: IConfigLoadCallback
    ) {
        try {
            replaceImageConfig(pagImage)
            index?.let {
                pagFile.replaceImage(it, pagImage)
            }
            name?.let {
                pagFile.replaceImageByName(it, pagImage)
            }
            callback.onResult(pagFile)
        } catch (throwable: Throwable) {
            callback.onError(throwable)
        }
    }

    private fun loadImageError(
        throwable: Throwable?,
        pagFile: PAGFile,
        callback: IConfigLoadCallback
    ) {
        if (data.allowAnimationOnImageFailure()) {
            val errorMsg =
                "The image loading failed. Continue playing the animation. source=$imageSource"
            if (throwable == null) {
                CsLogger.tag(ImageLoaderConstants.TAG).w(errorMsg)
            } else {
                CsLogger.tag(ImageLoaderConstants.TAG).w(throwable, errorMsg)
            }
            val errorSource = ImageResSource(data.getErrorDrawableRes() ?: 0)
            errorSource.loadImage(object : IImageLoadCallback {
                override fun onResult(pagImage: PAGImage) {
                    loadImageSuccess(pagFile, pagImage, callback)
                }

                override fun onError(result: Throwable?) {
                    callback.onError(result)
                }
            })
        } else {
            callback.onError(throwable)
        }
    }

    private fun replaceImageConfig(pagImage: PAGImage) {
        when (data.getScaleMode()) {
            NONE -> {
                pagImage.setScaleMode(PAGScaleMode.None)
            }

            FIT_XY -> {
                pagImage.setScaleMode(PAGScaleMode.Stretch)
            }

            CENTER_INSIDE -> {
                pagImage.setScaleMode(PAGScaleMode.LetterBox)
            }

            CENTER_CROP -> {
                pagImage.setScaleMode(PAGScaleMode.Zoom)
            }
        }
    }
}