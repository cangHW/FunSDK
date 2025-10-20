package com.proxy.service.imageloader.info.lottie.loader.controller

import com.airbnb.lottie.LottieAnimationView
import com.proxy.service.imageloader.base.loader.lottie.LottieController
import com.proxy.service.imageloader.base.option.lottie.LottieLoopModel

/**
 * @author: cangHX
 * @data: 2024/6/5 09:52
 * @desc:
 */
class LottieControllerImpl(
    private val view: LottieAnimationView
): LottieController {

    override fun setLoopCount(count: Int) {
        view.repeatCount = count
    }

    override fun setLoopModel(model: LottieLoopModel) {
        view.repeatMode = model.model
    }

    override fun setAnimationProgress(progress: Float) {
        view.progress = progress
    }

    override fun setAnimationSpeed(speed: Float) {
        view.speed = speed
    }

    override fun playAnimation() {
        view.playAnimation()
    }

    override fun pauseAnimation() {
        view.pauseAnimation()
    }

    override fun resumeAnimation() {
        view.resumeAnimation()
    }

    override fun cancelAnimation() {
        view.cancelAnimation()
    }
}