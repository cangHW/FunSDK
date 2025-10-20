package com.proxy.service.imageloader.base.option.lottie

import androidx.annotation.FloatRange
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.base.loader.lottie.ILottieLoader
import com.proxy.service.imageloader.base.option.base.IOption
import com.proxy.service.imageloader.base.option.lottie.callback.LottieAnimationCallback
import com.proxy.service.imageloader.base.option.lottie.callback.LottieAnimationUpdateCallback

/**
 * @author: cangHX
 * @data: 2024/6/4 17:31
 * @desc:
 */
interface ILottieOption : ILottieLoader, IOption<ILottieOption>, ILottieAction<ILottieOption> {

    /**
     * 设置是否自动播放
     *
     * @param isAutoPlay 是否自动播放, 默认为 [ImageLoaderConstants.IS_AUTO_PLAY]
     * */
    fun setAutoPlay(isAutoPlay: Boolean): ILottieOption

    /**
     * 设置动画开始回调
     * */
    fun setAnimationStartCallback(callback: LottieAnimationCallback): ILottieOption

    /**
     * 设置动画结束回调
     * */
    fun setAnimationEndCallback(callback: LottieAnimationCallback): ILottieOption

    /**
     * 设置动画取消回调
     * */
    fun setAnimationCancelCallback(callback: LottieAnimationCallback): ILottieOption

    /**
     * 设置动画重播回调，每次重播都会回调一次
     * */
    fun setAnimationRepeatCallback(callback: LottieAnimationCallback): ILottieOption

    /**
     * 设置动画暂停播放回调
     * */
    fun setAnimationPauseCallback(callback: LottieAnimationCallback): ILottieOption

    /**
     * 设置动画继续播放回调
     * */
    fun setAnimationResumeCallback(callback: LottieAnimationCallback): ILottieOption

    /**
     * 设置动画更新回调
     * */
    fun setAnimationUpdateCallback(callback: LottieAnimationUpdateCallback): ILottieOption
}