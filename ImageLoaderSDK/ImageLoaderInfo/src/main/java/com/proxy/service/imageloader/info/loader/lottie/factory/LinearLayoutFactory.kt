package com.proxy.service.imageloader.info.loader.lottie.factory

import android.view.Gravity
import android.widget.LinearLayout
import com.airbnb.lottie.LottieAnimationView
import com.proxy.service.imageloader.info.loader.lottie.factory.IFactory

/**
 * @author: cangHX
 * @data: 2024/6/5 08:57
 * @desc:
 */
object LinearLayoutFactory : IFactory<LinearLayout> {
    override fun loadLottieAnimationView(viewGroup: LinearLayout): LottieAnimationView {
        var lottieView = getLottieAnimationView(viewGroup)
        if (lottieView == null) {
            lottieView = LottieAnimationView(viewGroup.context)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.CENTER
            viewGroup.addView(lottieView, params)
        }
        return lottieView
    }
}