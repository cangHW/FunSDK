package com.proxy.service.imageloader.info.loader.lottie.factory

import android.view.Gravity
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView

/**
 * @author: cangHX
 * @data: 2024/6/5 08:57
 * @desc:
 */
object FrameLayoutFactory : IFactory<FrameLayout> {
    override fun loadLottieAnimationView(viewGroup: FrameLayout): LottieAnimationViewImpl {
        var lottieView = getLottieAnimationView(viewGroup)
        if (lottieView == null) {
            lottieView = LottieAnimationViewImpl(viewGroup.context)
            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.CENTER
            viewGroup.addView(lottieView, params)
        }
        return lottieView
    }
}