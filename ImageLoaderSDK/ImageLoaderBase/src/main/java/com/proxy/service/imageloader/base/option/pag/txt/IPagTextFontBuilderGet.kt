package com.proxy.service.imageloader.base.option.pag.txt

/**
 * @author: cangHX
 * @data: 2025/10/15 22:49
 * @desc:
 */
interface IPagTextFontBuilderGet {

    /**
     * 获取字体文件的在线链接
     * */
    fun getFontFamilyUrl(): String?

    /**
     * 获取在线字体文件的缓存 key
     * */
    fun getFontFamilyUrlCacheKey(): String?

    /**
     * 获取字体文件的本地路径
     * */
    fun getFontFamilyPath(): String?

    /**
     * 获取字体文件的 assets 名称
     * */
    fun getFontFamilyAssetsName(): String?

    /**
     * 是否使用字体样式
     * */
    fun isFontStyleEnable(): Boolean

    /**
     * 当字体加载失败时, 是否允许动画继续执行
     * */
    fun allowAnimationOnFontFailure(): Boolean

}