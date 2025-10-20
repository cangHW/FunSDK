package com.proxy.service.imageloader.base.loader.lottie

import androidx.annotation.FloatRange
import com.proxy.service.imageloader.base.option.lottie.ILottieAction
import com.proxy.service.imageloader.base.option.lottie.ILottieOption
import com.proxy.service.imageloader.base.option.lottie.LottieLoopModel

/**
 * @author: cangHX
 * @data: 2024/6/5 09:49
 * @desc:
 */
interface LottieController : ILottieAction<Unit> {

    /**
     * 播放动画
     * */
    fun playAnimation()

    /**
     * 暂停动画
     * */
    fun pauseAnimation()

    /**
     * 继续动画
     * */
    fun resumeAnimation()

    /**
     * 取消动画
     * */
    fun cancelAnimation()
}