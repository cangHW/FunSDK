package com.proxy.service.imageloader.info.info.lottie

import android.animation.ValueAnimator

/**
 * @author: cangHX
 * @data: 2024/6/5 09:30
 * @desc:
 */
class LottieAnimatorUpdateListener constructor(val info: LottieInfo) :
    ValueAnimator.AnimatorUpdateListener {
    override fun onAnimationUpdate(p0: ValueAnimator) {
        info.animationUpdateCallback?.onUpdate(p0)
    }
}