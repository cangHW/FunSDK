package com.proxy.service.imageloader.base.option.lottie

import android.animation.ValueAnimator

/**
 * @author: cangHX
 * @data: 2024/6/4 18:56
 * @desc:
 */
enum class LottieRepeatModel constructor(val model: Int) {

    /**
     * 播放完成后下次播放会正序播放
     * */
    RESTART(ValueAnimator.RESTART),

    /**
     * 播放完成后下次播放会倒序播放
     * */
    REVERSE(ValueAnimator.REVERSE);

}