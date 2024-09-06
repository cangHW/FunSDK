package com.proxy.service.imageloader.info.loader.lottie.factory

import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView

/**
 * @author: cangHX
 * @data: 2024/6/5 07:59
 * @desc:
 */
object ConstraintLayoutFactory : IFactory<ConstraintLayout> {
    override fun loadLottieAnimationView(viewGroup: ConstraintLayout): LottieAnimationView {
        var lottieView = getLottieAnimationView(viewGroup)
        if (lottieView == null) {
            lottieView = LottieAnimationView(viewGroup.context)
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            viewGroup.addView(lottieView, params)
        }
        return lottieView
    }
}