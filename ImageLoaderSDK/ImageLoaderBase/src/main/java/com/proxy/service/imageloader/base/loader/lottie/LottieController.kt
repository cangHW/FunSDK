package com.proxy.service.imageloader.base.loader.lottie

/**
 * @author: cangHX
 * @data: 2024/6/5 09:49
 * @desc:
 */
interface LottieController {

    /**
     * 播放动画
     * */
    fun playAnimation()

    /**
     * 暂停动画
     * */
    fun pauseAnimation()

    /**
     * 取消动画
     * */
    fun cancelAnimation()
}