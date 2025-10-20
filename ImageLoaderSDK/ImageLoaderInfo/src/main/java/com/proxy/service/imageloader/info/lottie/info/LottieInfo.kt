package com.proxy.service.imageloader.info.lottie.info

import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.base.loader.lottie.LottieController
import com.proxy.service.imageloader.base.option.base.LoadErrorCallback
import com.proxy.service.imageloader.base.option.lottie.LottieLoopModel
import com.proxy.service.imageloader.base.option.lottie.callback.LottieAnimationCallback
import com.proxy.service.imageloader.base.option.lottie.callback.LottieAnimationUpdateCallback
import com.proxy.service.imageloader.info.R
import com.proxy.service.imageloader.info.base.BaseInfo
import com.proxy.service.imageloader.info.lottie.loader.view.LottieAnimationViewImpl
import com.proxy.service.imageloader.info.lottie.loader.view.LottieAnimationViewImpl.OnDrawErrorCallback
import com.proxy.service.imageloader.info.lottie.request.source.BaseLottieSourceData

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
    var progress: Float = 0f
    var speed: Float = 1f

    var width: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT

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
            loadErrorCallback?.onLoadError()
            return
        }

        if (view.isAnimating) {
            controller.cancelAnimation()
        }

        checkListenerForView(view)

        sourceData?.load(getContext(), object : BaseLottieSourceData.ILottieLoadCallback {
            override fun onResult(result: LottieComposition) {
                view.setComposition(result)
                setConfig(controller)
            }

            override fun onError(result: Throwable?) {
                result?.let {
                    CsLogger.tag(ImageLoaderConstants.TAG).d(it)
                }
                loadErrorCallback?.onLoadError()
            }
        })
    }

    private fun checkListenerForView(view: LottieAnimationViewImpl) {
        val source = view.getTag(R.id.cs_imageload_info_lottie_view_tag) as? LottieInfo?
        view.setTag(R.id.cs_imageload_info_lottie_view_tag, this)

        if (source != null) {
            view.removeAnimatorListener(source.listener)
            view.removeAnimatorPauseListener(source.pauseListener)
            view.removeUpdateListener(source.updateListener)
        }
        view.addAnimatorListener(listener)
        view.addAnimatorPauseListener(pauseListener)
        view.addAnimatorUpdateListener(updateListener)

        view.setOnDrawErrorCallback(object : OnDrawErrorCallback {
            override fun onErrorCallback(throwable: Throwable) {
                loadErrorCallback?.onLoadError()
            }
        })
    }

    private fun setConfig(controller: LottieController) {
        loopCount?.let {
            controller.setLoopCount(it)
        }
        controller.setLoopModel(loopModel)
        controller.setAnimationSpeed(speed)
        if (isAutoPlay) {
            controller.playAnimation()
        }
    }

}