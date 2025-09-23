package com.proxy.service.imageloader.info.loader.lottie.factory

import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView

/**
 * @author: cangHX
 * @data: 2024/6/5 07:59
 * @desc:
 */
interface IFactory<T : ViewGroup> {

    fun getLottieAnimationView(viewGroup: ViewGroup): LottieAnimationViewImpl? {
        var index = 0
        while (index < viewGroup.childCount) {
            val child = viewGroup.getChildAt(index)
            if (child is LottieAnimationViewImpl) {
                return child
            }
            index++
        }
        return null
    }

    fun loadLottieAnimationView(viewGroup: T): LottieAnimationViewImpl
}