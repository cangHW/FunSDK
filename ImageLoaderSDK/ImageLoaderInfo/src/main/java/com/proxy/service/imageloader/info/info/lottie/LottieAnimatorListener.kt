package com.proxy.service.imageloader.info.info.lottie

import android.animation.Animator

/**
 * @author: cangHX
 * @data: 2024/6/5 09:29
 * @desc:
 */
class LottieAnimatorListener(val info: LottieInfo) : Animator.AnimatorListener {
    override fun onAnimationStart(p0: Animator) {
        info.animationStartCallback?.onAnimation(p0)
    }

    override fun onAnimationEnd(p0: Animator) {
        info.animationEndCallback?.onAnimation(p0)
    }

    override fun onAnimationCancel(p0: Animator) {
        info.animationCancelCallback?.onAnimation(p0)
    }

    override fun onAnimationRepeat(p0: Animator) {
        info.animationRepeatCallback?.onAnimation(p0)
    }
}