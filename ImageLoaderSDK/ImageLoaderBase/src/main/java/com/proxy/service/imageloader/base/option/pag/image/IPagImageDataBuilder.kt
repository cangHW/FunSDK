package com.proxy.service.imageloader.base.option.pag.image

import androidx.annotation.DrawableRes
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants

/**
 * @author: cangHX
 * @data: 2025/10/16 17:05
 * @desc:
 */
interface IPagImageDataBuilder {

    /**
     * 设置缩放模式, 默认 [PagScaleMode.NONE]
     * */
    fun setScaleMode(mode: PagScaleMode): IPagImageDataBuilder

    /**
     * 加载错误占位图
     */
    fun error(@DrawableRes errorId: Int): IPagImageDataBuilder

    /**
     * 当图像加载失败时, 是否允许动画继续执行, 默认: [ImageLoaderConstants.ALLOW_ANIMATION_ON_IMAGE_FAILURE]
     * */
    fun allowAnimationOnImageFailure(allow: Boolean): IPagImageDataBuilder

    /**
     * 创建配置
     * */
    fun build(): PagImageData
}