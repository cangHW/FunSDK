package com.proxy.service.document.image.info.utils

import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * @author: cangHX
 * @data: 2025/6/7 10:39
 * @desc:
 */
class AnimatorManager private constructor() {

    companion object {
        fun create(): AnimatorManager {
            return AnimatorManager()
        }
    }

    private var animator: ValueAnimator? = null
    private var lastValue = 0f

    fun startAnim(callback: (value: Float) -> Unit) {
        cancelAnim()
        lastValue = 0f

        animator = ValueAnimator.ofFloat(0f, 1f)
        animator?.interpolator = AccelerateDecelerateInterpolator()
        animator?.duration = 120
        animator?.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            callback(value - lastValue)
            lastValue = value
        }
        animator?.start()
    }

    fun cancelAnim() {
        animator?.cancel()
    }

}