package com.proxy.service.imageloader.base.option.lottie.callback

/**
 * @author: cangHX
 * @data: 2024/6/5 09:19
 * @desc:
 */
interface LottieLoadErrorCallback {

    fun onAnimationError(result: Throwable)

}