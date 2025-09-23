package com.proxy.service.imageloader.info.info.lottie

import android.animation.Animator

/**
 * @author: cangHX
 * @data: 2024/6/5 09:30
 * @desc:
 */
class LottieAnimatorPauseListener(
    private val info: LottieInfo
) : Animator.AnimatorPauseListener {

    override fun onAnimationPause(p0: Animator) {
        info.animationPauseCallback?.onAnimation(p0)
    }

    override fun onAnimationResume(p0: Animator) {
        info.animationResumeCallback?.onAnimation(p0)
    }
}