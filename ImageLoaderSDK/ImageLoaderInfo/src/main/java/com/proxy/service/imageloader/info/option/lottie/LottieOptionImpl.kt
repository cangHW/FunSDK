package com.proxy.service.imageloader.info.option.lottie

import android.animation.ValueAnimator
import com.proxy.service.imageloader.base.option.base.LoadErrorCallback
import com.proxy.service.imageloader.base.option.lottie.ILottieOption
import com.proxy.service.imageloader.base.option.lottie.LottieLoopModel
import com.proxy.service.imageloader.base.option.lottie.callback.LottieAnimationCallback
import com.proxy.service.imageloader.base.option.lottie.callback.LottieAnimationUpdateCallback
import com.proxy.service.imageloader.info.info.lottie.LottieInfo
import com.proxy.service.imageloader.info.loader.lottie.LottieLoaderImpl

/**
 * @author: cangHX
 * @data: 2024/6/4 17:33
 * @desc:
 */
class LottieOptionImpl(
    private val info: LottieInfo
) : LottieLoaderImpl(info), ILottieOption {
    override fun setAutoPlay(isAutoPlay: Boolean): ILottieOption {
        info.isAutoPlay = isAutoPlay
        return this
    }

    override fun setLoopCount(count: Int): ILottieOption {
        if (count != 0) {
            val value = count - 1
            if (value < 0) {
                info.loopCount = ValueAnimator.INFINITE
            } else {
                info.loopCount = value
            }
        }
        return this
    }

    override fun setLoopModel(model: LottieLoopModel): ILottieOption {
        info.loopModel = model
        return this
    }

    override fun setAnimationProgress(progress: Float): ILottieOption {
        info.progress = progress
        return this
    }

    override fun setAnimationSpeed(speed: Float): ILottieOption {
        info.speed = speed
        return this
    }

    override fun setLoadErrorCallback(callback: LoadErrorCallback): ILottieOption {
        info.loadErrorCallback = callback
        return this
    }

    override fun setAnimationStartCallback(callback: LottieAnimationCallback): ILottieOption {
        info.animationStartCallback = callback
        return this
    }

    override fun setAnimationEndCallback(callback: LottieAnimationCallback): ILottieOption {
        info.animationEndCallback = callback
        return this
    }

    override fun setAnimationCancelCallback(callback: LottieAnimationCallback): ILottieOption {
        info.animationCancelCallback = callback
        return this
    }

    override fun setAnimationRepeatCallback(callback: LottieAnimationCallback): ILottieOption {
        info.animationRepeatCallback = callback
        return this
    }

    override fun setAnimationPauseCallback(callback: LottieAnimationCallback): ILottieOption {
        info.animationPauseCallback = callback
        return this
    }

    override fun setAnimationResumeCallback(callback: LottieAnimationCallback): ILottieOption {
        info.animationResumeCallback = callback
        return this
    }

    override fun setAnimationUpdateCallback(callback: LottieAnimationUpdateCallback): ILottieOption {
        info.animationUpdateCallback = callback
        return this
    }
}