package com.proxy.service.imageloader.info.lottie.loader

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.proxy.service.imageloader.base.loader.lottie.ILottieLoader
import com.proxy.service.imageloader.base.loader.lottie.LottieController
import com.proxy.service.imageloader.info.lottie.info.LottieInfo
import com.proxy.service.imageloader.info.lottie.loader.controller.LottieControllerEmpty
import com.proxy.service.imageloader.info.lottie.loader.controller.LottieControllerImpl
import com.proxy.service.imageloader.info.lottie.loader.view.LottieAnimationViewImpl
import com.proxy.service.imageloader.info.utils.ViewUtils

/**
 * @author: cangHX
 * @data: 2024/6/4 17:45
 * @desc:
 */
open class LottieLoaderImpl(
    private val info: LottieInfo
) : ILottieLoader {

    override fun into(linearLayout: LinearLayout?): LottieController {
        if (linearLayout == null) {
            return LottieControllerEmpty()
        }
        return into(linearLayout as ViewGroup)
    }

    override fun into(relativeLayout: RelativeLayout?): LottieController {
        if (relativeLayout == null) {
            return LottieControllerEmpty()
        }
        return into(relativeLayout as ViewGroup)
    }

    override fun into(frameLayout: FrameLayout?): LottieController {
        if (frameLayout == null) {
            return LottieControllerEmpty()
        }
        return into(frameLayout as ViewGroup)
    }

    override fun into(viewGroup: ViewGroup?): LottieController {
        if (viewGroup == null) {
            return LottieControllerEmpty()
        }
        val view = getLottieView(viewGroup)
        val controller = LottieControllerImpl(view)
        info.loadConfig(view, controller)
        return controller
    }

    private fun getLottieView(viewGroup: ViewGroup): LottieAnimationViewImpl {
        var index = 0
        var lottieView: LottieAnimationViewImpl? = null
        while (index < viewGroup.childCount) {
            val child = viewGroup.getChildAt(index)
            if (child is LottieAnimationViewImpl) {
                lottieView = child
                break
            }
            index++
        }
        if (lottieView == null) {
            lottieView = LottieAnimationViewImpl(viewGroup.context)
            ViewUtils.addView(lottieView, viewGroup, info.width, info.height)
        }
        return lottieView
    }
}