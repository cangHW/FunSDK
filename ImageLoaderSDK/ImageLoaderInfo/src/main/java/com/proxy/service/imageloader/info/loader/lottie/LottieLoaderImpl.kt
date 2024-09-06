package com.proxy.service.imageloader.info.loader.lottie

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.proxy.service.imageloader.base.loader.lottie.ILottieLoader
import com.proxy.service.imageloader.base.loader.lottie.LottieController
import com.proxy.service.imageloader.info.info.lottie.LottieInfo
import com.proxy.service.imageloader.info.loader.lottie.controller.LottieControllerEmpty
import com.proxy.service.imageloader.info.loader.lottie.controller.LottieControllerImpl
import com.proxy.service.imageloader.info.loader.lottie.factory.ConstraintLayoutFactory
import com.proxy.service.imageloader.info.loader.lottie.factory.FrameLayoutFactory
import com.proxy.service.imageloader.info.loader.lottie.factory.LinearLayoutFactory
import com.proxy.service.imageloader.info.loader.lottie.factory.RelativeLayoutFactory
import com.proxy.service.imageloader.info.loader.lottie.factory.ViewGroupFactory

/**
 * @author: cangHX
 * @data: 2024/6/4 17:45
 * @desc:
 */
open class LottieLoaderImpl constructor(private val info: LottieInfo) : ILottieLoader {
    override fun into(linearLayout: LinearLayout?): LottieController {
        if (linearLayout == null) {
            return LottieControllerEmpty()
        }
        val view = LinearLayoutFactory.loadLottieAnimationView(linearLayout)
        val controller = LottieControllerImpl(view, info)
        info.loadConfig(view, controller)
        return controller
    }

    override fun into(relativeLayout: RelativeLayout?): LottieController {
        if (relativeLayout == null) {
            return LottieControllerEmpty()
        }
        val view = RelativeLayoutFactory.loadLottieAnimationView(relativeLayout)
        val controller = LottieControllerImpl(view, info)
        info.loadConfig(view, controller)
        return controller
    }

    override fun into(frameLayout: FrameLayout?): LottieController {
        if (frameLayout == null) {
            return LottieControllerEmpty()
        }
        val view = FrameLayoutFactory.loadLottieAnimationView(frameLayout)
        val controller = LottieControllerImpl(view, info)
        info.loadConfig(view, controller)
        return controller
    }

    override fun into(viewGroup: ViewGroup?): LottieController {
        if (viewGroup == null) {
            return LottieControllerEmpty()
        }
        val controller: LottieController = when (viewGroup) {
            is LinearLayout -> {
                into(viewGroup)
            }

            is RelativeLayout -> {
                into(viewGroup)
            }

            is FrameLayout -> {
                into(viewGroup)
            }

            is ConstraintLayout -> {
                val view = ConstraintLayoutFactory.loadLottieAnimationView(viewGroup)
                val controller = LottieControllerImpl(view, info)
                info.loadConfig(view, controller)
                controller
            }

            else -> {
                val view = ViewGroupFactory.loadLottieAnimationView(viewGroup)
                val controller = LottieControllerImpl(view, info)
                info.loadConfig(view, controller)
                controller
            }
        }
        return controller
    }

}