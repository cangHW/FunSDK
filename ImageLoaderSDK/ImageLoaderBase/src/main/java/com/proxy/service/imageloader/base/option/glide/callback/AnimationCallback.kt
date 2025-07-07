package com.proxy.service.imageloader.base.option.glide.callback

/**
 * @author: cangHX
 * @data: 2025/7/7 09:41
 * @desc:
 */
interface AnimationCallback<R> {

    /**
     * 动画开始
     * */
    fun onAnimationStart(drawable: R)

    /**
     * 动画结束
     * */
    fun onAnimationEnd(drawable: R)

}