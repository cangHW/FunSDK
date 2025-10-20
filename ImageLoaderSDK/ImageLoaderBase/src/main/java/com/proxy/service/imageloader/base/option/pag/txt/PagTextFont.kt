package com.proxy.service.imageloader.base.option.pag.txt

import com.proxy.service.imageloader.base.constants.ImageLoaderConstants

/**
 * @author: cangHX
 * @data: 2025/10/15 22:49
 * @desc:
 */
class PagTextFont private constructor(
    private val builder: IPagTextFontBuilderGet
) : IPagTextFontBuilderGet {

    companion object {

        /**
         * 使用字体文件的在线链接
         * */
        fun builderWithUrl(url: String): IPagTextFontBuilder {
            val builder = Builder()
            builder.setFontFamilyUrl(url, null)
            return builder
        }

        /**
         * 使用字体文件的在线链接
         * */
        fun builderWithUrl(url: String, cacheKey: String): IPagTextFontBuilder {
            val builder = Builder()
            builder.setFontFamilyUrl(url, cacheKey)
            return builder
        }

        /**
         * 使用字体文件的本地路径
         * */
        fun builderWithPath(path: String): IPagTextFontBuilder {
            val builder = Builder()
            builder.setFontFamilyPath(path)
            return builder
        }

        /**
         * 使用字体文件的 assets 名称
         * */
        fun builderWithAssets(fileName: String): IPagTextFontBuilder {
            val builder = Builder()
            builder.setFontFamilyAssets(fileName)
            return builder
        }
    }

    override fun getFontFamilyUrl(): String? {
        return builder.getFontFamilyUrl()
    }

    override fun getFontFamilyUrlCacheKey(): String? {
        return builder.getFontFamilyUrlCacheKey()
    }

    override fun getFontFamilyPath(): String? {
        return builder.getFontFamilyPath()
    }

    override fun getFontFamilyAssetsName(): String? {
        return builder.getFontFamilyAssetsName()
    }

    override fun isFontStyleEnable(): Boolean {
        return builder.isFontStyleEnable()
    }

    override fun allowAnimationOnFontFailure(): Boolean {
        return builder.allowAnimationOnFontFailure()
    }

    private class Builder : IPagTextFontBuilder, IPagTextFontBuilderGet {

        private var allowAnimationOnFontFailure =
            ImageLoaderConstants.ALLOW_ANIMATION_ON_FONT_FAILURE

        private var fontUrl: String? = null
        private var fontUrlKey: String? = null
        private var fontPath: String? = null
        private var fontFileName: String? = null

        private var styleEnable = ImageLoaderConstants.FONT_STYLE_ENABLE

        fun setFontFamilyUrl(url: String, key: String?) {
            this.fontUrl = url
            this.fontUrlKey = key
        }

        fun setFontFamilyPath(path: String) {
            this.fontPath = path
        }

        fun setFontFamilyAssets(fileName: String) {
            this.fontFileName = fileName
        }

        override fun setFontStyleEnable(enable: Boolean): IPagTextFontBuilder {
            this.styleEnable = enable
            return this
        }

        override fun allowAnimationOnFontFailure(allow: Boolean): IPagTextFontBuilder {
            this.allowAnimationOnFontFailure = allow
            return this
        }

        override fun build(): PagTextFont {
            return PagTextFont(this)
        }

        override fun getFontFamilyUrl(): String? {
            return fontUrl
        }

        override fun getFontFamilyUrlCacheKey(): String? {
            return fontUrlKey
        }

        override fun getFontFamilyPath(): String? {
            return fontPath
        }

        override fun getFontFamilyAssetsName(): String? {
            return fontFileName
        }

        override fun isFontStyleEnable(): Boolean {
            return styleEnable
        }

        override fun allowAnimationOnFontFailure(): Boolean {
            return allowAnimationOnFontFailure
        }

    }

}