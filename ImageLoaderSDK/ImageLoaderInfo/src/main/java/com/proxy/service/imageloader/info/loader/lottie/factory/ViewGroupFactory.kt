package com.proxy.service.imageloader.info.loader.lottie.factory

import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.proxy.service.imageloader.info.loader.lottie.factory.IFactory

/**
 * @author: cangHX
 * @data: 2024/6/5 08:57
 * @desc:
 */
object ViewGroupFactory : IFactory<ViewGroup> {
    override fun loadLottieAnimationView(viewGroup: ViewGroup): LottieAnimationViewImpl {
        var lottieView = getLottieAnimationView(viewGroup)
        if (lottieView == null) {
            lottieView = LottieAnimationViewImpl(viewGroup.context)
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            viewGroup.addView(lottieView, params)
        }
        return lottieView
    }
}