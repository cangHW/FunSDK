package com.proxy.service.imageloader.base.option.pag.txt

import com.proxy.service.imageloader.base.constants.ImageLoaderConstants

/**
 * @author: cangHX
 * @data: 2025/10/15 22:49
 * @desc:
 */
interface IPagTextFontBuilder {

    /**
     * 设置是否使用字体样式, 默认: [ImageLoaderConstants.FONT_STYLE_ENABLE]
     * */
    fun setFontStyleEnable(enable: Boolean): IPagTextFontBuilder

    /**
     * 当字体加载失败时, 是否允许动画继续执行, 默认: [ImageLoaderConstants.ALLOW_ANIMATION_ON_FONT_FAILURE]
     * */
    fun allowAnimationOnFontFailure(allow: Boolean): IPagTextFontBuilder

    /**
     * 创建配置
     * */
    fun build(): PagTextFont
}