package com.proxy.service.imageloader.base.constants

/**
 * @author: cangHX
 * @data: 2025/6/25 18:25
 * @desc:
 */
object ImageLoaderConstants {
    const val TAG = "ImageLoader"

    const val IS_AUTO_PLAY = true

    /**
     * 是否使用字体样式
     * */
    const val FONT_STYLE_ENABLE = true

    /**
     * 当字体加载失败时，动画是否继续执行
     * */
    const val ALLOW_ANIMATION_ON_FONT_FAILURE = true

    /**
     * 当图像加载失败时，动画是否继续执行
     * */
    const val ALLOW_ANIMATION_ON_IMAGE_FAILURE = true

    /**
     * 文件缓存最大值
     * */
    const val CACHE_MAX_SIZE = 250 * 1024 * 1024

    /**
     * lottie 文件格式
     * */
    const val LOTTIE_TYPE_JSON: String = ".json"
    const val LOTTIE_TYPE_ZIP: String = ".zip"
}