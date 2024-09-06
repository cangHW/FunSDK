package com.proxy.service.imageloader.info.loader.lottie.factory

import android.widget.RelativeLayout
import com.airbnb.lottie.LottieAnimationView
import com.proxy.service.imageloader.info.loader.lottie.factory.IFactory

/**
 * @author: cangHX
 * @data: 2024/6/5 08:57
 * @desc:
 */
object RelativeLayoutFactory : IFactory<RelativeLayout> {
    override fun loadLottieAnimationView(viewGroup: RelativeLayout): LottieAnimationView {
        var lottieView = getLottieAnimationView(viewGroup)
        if (lottieView == null) {
            lottieView = LottieAnimationView(viewGroup.context)
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            viewGroup.addView(lottieView, params)
        }
        return lottieView
    }
}