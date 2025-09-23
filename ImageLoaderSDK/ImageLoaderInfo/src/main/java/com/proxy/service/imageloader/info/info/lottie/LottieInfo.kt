package com.proxy.service.imageloader.info.info.lottie

import android.content.Context
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.base.loader.lottie.LottieController
import com.proxy.service.imageloader.base.option.base.LoadErrorCallback
import com.proxy.service.imageloader.base.option.lottie.LottieLoopModel
import com.proxy.service.imageloader.base.option.lottie.callback.LottieAnimationCallback
import com.proxy.service.imageloader.base.option.lottie.callback.LottieAnimationUpdateCallback
import com.proxy.service.imageloader.info.R
import com.proxy.service.imageloader.info.info.base.BaseInfo
import com.proxy.service.imageloader.info.loader.lottie.ILottieLoadCallback
import com.proxy.service.imageloader.info.loader.lottie.factory.LottieAnimationViewImpl
import com.proxy.service.imageloader.info.loader.lottie.factory.LottieAnimationViewImpl.OnDrawErrorCallback
import com.proxy.service.imageloader.info.request.lottie.source.BaseLottieSourceData

/**
 * @author: cangHX
 * @data: 2024/6/4 17:32
 * @desc:
 */
class LottieInfo : BaseInfo() {

    var sourceData: BaseLottieSourceData? = null

    var isAutoPlay: Boolean = ImageLoaderConstants.IS_AUTO_PLAY
    var loopCount: Int? = null
    var loopModel: LottieLoopModel = LottieLoopModel.RESTART
    var speed: Float = 1f

    private var listener = LottieAnimatorListener(this)
    private var pauseListener = LottieAnimatorPauseListener(this)
    private var updateListener = LottieAnimatorUpdateListener(this)

    var loadErrorCallback: LoadErrorCallback? = null

    var animationStartCallback: LottieAnimationCallback? = null
    var animationEndCallback: LottieAnimationCallback? = null
    var animationCancelCallback: LottieAnimationCallback? = null
    var animationRepeatCallback: LottieAnimationCallback? = null

    var animationPauseCallback: LottieAnimationCallback? = null
    var animationResumeCallback: LottieAnimationCallback? = null

    var animationUpdateCallback: LottieAnimationUpdateCallback? = null

    fun loadConfig(view: LottieAnimationViewImpl, controller: LottieController) {
        if (sourceData == null) {
            return
        }

        val source = view.getTag(R.id.cs_imageload_info_lottie_view_tag) as? LottieInfo?
        view.setTag(R.id.cs_imageload_info_lottie_view_tag, this)

        view.removeAnimatorListener(source?.listener)
        view.removeAnimatorPauseListener(source?.pauseListener)
        view.removeUpdateListener(source?.updateListener)

        view.addAnimatorListener(listener)
        view.addAnimatorPauseListener(pauseListener)
        view.addAnimatorUpdateListener(updateListener)

        view.setOnDrawErrorCallback(object : OnDrawErrorCallback{
            override fun onErrorCallback(throwable: Throwable) {
                loadErrorCallback?.onLoadError()
            }
        })

        sourceData?.load(getContext(), object : ILottieLoadCallback {
            override fun onResult(result: LottieComposition?) {
                if (result == null) {
                    return
                }
                view.setComposition(result)
                setConfig(view, controller)
            }

            override fun onError(result: Throwable?) {
                view.setTag(R.id.cs_imageload_info_lottie_view_tag, null)
                loadErrorCallback?.onLoadError()
            }
        })
    }

    private fun setConfig(view: LottieAnimationView, controller: LottieController) {
        if (view.composition == null) {
            return
        }
        val source = view.getTag(R.id.cs_imageload_info_lottie_view_tag) as? LottieInfo? ?: return
        source.loopCount?.let {
            view.repeatCount = it
        }
        view.repeatMode = source.loopModel.model
        checkPlay(view, controller)
    }

    private fun checkPlay(view: LottieAnimationView, controller: LottieController) {
        if (view.isAnimating) {
            controller.cancelAnimation()
        }
        if (isAutoPlay) {
            controller.playAnimation()
        }
    }

    private fun getContext(): Context? {
        activity?.let {
            return it
        }
        fragment?.let {
            if (it.isAdded && !it.isDetached && !it.isRemoving) {
                return it.context
            } else {
                return null
            }
        }
        view?.let {
            return it.context
        }
        ctx?.let {
            return it
        }
        return null
    }
}