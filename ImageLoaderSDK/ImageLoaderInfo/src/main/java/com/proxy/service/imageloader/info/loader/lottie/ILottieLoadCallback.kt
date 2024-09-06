package com.proxy.service.imageloader.info.loader.lottie

import com.airbnb.lottie.LottieComposition

/**
 * @author: cangHX
 * @data: 2024/6/4 18:07
 * @desc:
 */
interface ILottieLoadCallback {

    fun onResult(result: LottieComposition?)

    fun onError(result: Throwable?)

}