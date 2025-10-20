package com.proxy.service.imageloader.base.option.pag.image

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants

/**
 * @author: cangHX
 * @data: 2025/10/13 20:32
 * @desc:
 */
class PagImageData private constructor(
    private val builder: IPagImageDataBuilderGet
) : IPagImageDataBuilderGet {

    companion object {

        /**
         * 使用 bitmap
         * */
        fun builderWithBitmap(bitmap: Bitmap): IPagImageDataBuilder {
            val build = Build()
            build.setImageBitmap(bitmap)
            return build
        }

        /**
         * 使用图片的在线链接
         * */
        fun builderWithUrl(url: String): IPagImageDataBuilder {
            val build = Build()
            build.setImageUrl(url, null)
            return build
        }

        /**
         * 使用图片的在线链接
         *
         * @param cacheKey
         * */
        fun builderWithUrl(url: String, cacheKey: String): IPagImageDataBuilder {
            val build = Build()
            build.setImageUrl(url, cacheKey)
            return build
        }

        /**
         * 使用图片的 assets 名称
         * */
        fun builderWithAssets(fileName: String): IPagImageDataBuilder {
            val build = Build()
            build.setImageAssets(fileName)
            return build
        }

        /**
         * 使用图片的本地路径
         * */
        fun builderWithPath(path: String): IPagImageDataBuilder {
            val build = Build()
            build.setImagePath(path)
            return build
        }

        /**
         * 使用图片的字节数组
         * */
        fun builderWithByte(byteArray: ByteArray): IPagImageDataBuilder {
            val build = Build()
            build.setImageByte(byteArray)
            return build
        }

        /**
         * 使用图片的资源 id
         * */
        fun builderWithRes(@RawRes @DrawableRes resourceId: Int): IPagImageDataBuilder {
            val build = Build()
            build.setImageRes(resourceId)
            return build
        }
    }

    override fun getImageBitmap(): Bitmap? {
        return builder.getImageBitmap()
    }

    override fun getImageAssetsName(): String? {
        return builder.getImageAssetsName()
    }

    override fun getImageByte(): ByteArray? {
        return builder.getImageByte()
    }

    override fun getImagePath(): String? {
        return builder.getImagePath()
    }

    override fun getImageUrl(): String? {
        return builder.getImageUrl()
    }

    override fun getImageUrlCacheKey(): String? {
        return builder.getImageUrlCacheKey()
    }

    override fun getImageRes(): Int? {
        return builder.getImageRes()
    }

    override fun getScaleMode(): PagScaleMode {
        return builder.getScaleMode()
    }

    override fun getErrorDrawableRes(): Int? {
        return builder.getErrorDrawableRes()
    }

    override fun allowAnimationOnImageFailure(): Boolean {
        return builder.allowAnimationOnImageFailure()
    }

    private class Build : IPagImageDataBuilder, IPagImageDataBuilderGet {

        private var url: String? = null
        private var urlCacheKey: String? = null
        private var path: String? = null
        private var bitmap: Bitmap? = null
        private var assetsName: String? = null
        private var byteArray: ByteArray? = null
        private var resourceId: Int? = null

        private var pagScaleMode: PagScaleMode = PagScaleMode.NONE

        private var errorId: Int? = null
        private var allowAnimationOnImageFailure: Boolean =
            ImageLoaderConstants.ALLOW_ANIMATION_ON_IMAGE_FAILURE

        fun setImageUrl(url: String, cacheKey: String?) {
            this.url = url
            this.urlCacheKey = cacheKey
        }

        fun setImagePath(path: String) {
            this.path = path
        }

        fun setImageByte(byteArray: ByteArray) {
            this.byteArray = byteArray
        }

        fun setImageRes(@RawRes @DrawableRes resourceId: Int) {
            this.resourceId = resourceId
        }

        fun setImageBitmap(bitmap: Bitmap) {
            this.bitmap = bitmap
        }

        fun setImageAssets(fileName: String) {
            this.assetsName = fileName
        }

        override fun setScaleMode(mode: PagScaleMode): IPagImageDataBuilder {
            this.pagScaleMode = mode
            return this
        }

        override fun error(errorId: Int): IPagImageDataBuilder {
            this.errorId = errorId
            return this
        }

        override fun allowAnimationOnImageFailure(allow: Boolean): IPagImageDataBuilder {
            this.allowAnimationOnImageFailure = allow
            return this
        }

        override fun build(): PagImageData {
            return PagImageData(this)
        }

        override fun getImageUrl(): String? {
            return url
        }

        override fun getImageUrlCacheKey(): String? {
            return urlCacheKey
        }

        override fun getImagePath(): String? {
            return path
        }

        override fun getImageBitmap(): Bitmap? {
            return bitmap
        }

        override fun getImageAssetsName(): String? {
            return assetsName
        }

        override fun getImageByte(): ByteArray? {
            return byteArray
        }

        override fun getImageRes(): Int? {
            return resourceId
        }

        override fun getScaleMode(): PagScaleMode {
            return pagScaleMode
        }

        override fun getErrorDrawableRes(): Int? {
            return errorId
        }

        override fun allowAnimationOnImageFailure(): Boolean {
            return allowAnimationOnImageFailure
        }
    }
}