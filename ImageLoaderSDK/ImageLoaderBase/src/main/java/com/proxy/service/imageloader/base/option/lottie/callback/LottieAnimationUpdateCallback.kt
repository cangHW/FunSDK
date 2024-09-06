package com.proxy.service.imageloader.base.option.lottie.callback

import android.animation.ValueAnimator

/**
 * @author: cangHX
 * @data: 2024/6/5 09:19
 * @desc:
 */
interface LottieAnimationUpdateCallback {

    fun onUpdate(animation: ValueAnimator)

}