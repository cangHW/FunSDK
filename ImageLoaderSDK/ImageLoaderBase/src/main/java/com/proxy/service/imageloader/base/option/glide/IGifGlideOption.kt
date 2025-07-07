package com.proxy.service.imageloader.base.option.glide

import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.base.drawable.CsGifDrawable
import com.proxy.service.imageloader.base.option.glide.callback.AnimationCallback

/**
 * 配置管理器
 *
 * @author: cangHX
 * @data: 2024/5/16 09:47
 * @desc:
 */
interface IGifGlideOption : IBaseOption<IGifGlideOption, CsGifDrawable> {

    /**
     * 设置是否自动播放
     *
     * @param isAutoPlay 是否自动播放, 默认为 [ImageLoaderConstants.IS_AUTO_PLAY]
     * */
    fun setAutoPlay(isAutoPlay: Boolean): IGifGlideOption

    /**
     * 设置循环播放次数
     *
     * @param count 播放次数, 如果小于 0 则无限循环播放, 等于 0 则使用资源自身的循环次数
     * */
    fun setLoopCount(count: Int): IGifGlideOption

    /**
     * 设置动画回调
     * */
    fun setAnimationCallback(callback: AnimationCallback<CsGifDrawable>): IGifGlideOption
}