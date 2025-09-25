package com.proxy.service.imageloader.info.loader.lottie.controller

import com.airbnb.lottie.LottieAnimationView
import com.proxy.service.imageloader.base.loader.lottie.LottieController
import com.proxy.service.imageloader.info.info.lottie.LottieInfo

/**
 * @author: cangHX
 * @data: 2024/6/5 09:52
 * @desc:
 */
class LottieControllerImpl(
    private val view: LottieAnimationView,
    private val info: LottieInfo
): LottieController {

    override fun playAnimation() {
        view.speed = info.speed
        view.progress = info.progress
        view.playAnimation()
    }

    override fun pauseAnimation() {
        view.pauseAnimation()
    }

    override fun cancelAnimation() {
        view.cancelAnimation()
    }
}