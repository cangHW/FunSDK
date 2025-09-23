package com.proxy.service.imageloader.base.option.lottie

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
interface ILottieOption : ILottieLoader, IOption<ILottieOption> {

    /**
     * 设置是否自动播放
     *
     * @param isAutoPlay 是否自动播放, 默认为 [ImageLoaderConstants.IS_AUTO_PLAY]
     * */
    fun setAutoPlay(isAutoPlay: Boolean): ILottieOption

    /**
     * 设置播放次数
     *
     * @param count 播放次数, 如果小于 0 则无限循环播放, 等于 0 则使用资源自身默认的循环次数
     * */
    fun setLoopCount(count: Int): ILottieOption

    /**
     * 设置播放模式
     * */
    fun setLoopModel(model: LottieLoopModel): ILottieOption

    /**
     * 设置动画播放速度
     * @param speed 设置播放速度。如果速度为小于 0，动画将向后播放
     * */
    fun setAnimationSpeed(speed: Float): ILottieOption

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