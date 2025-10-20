package com.proxy.service.imageloader.info.lottie.request.source

import android.content.Context
import com.airbnb.lottie.LottieComposition

/**
 * @author: cangHX
 * @data: 2024/5/16 11:34
 * @desc:
 */
abstract class BaseLottieSourceData {

    interface ILottieLoadCallback {

        fun onResult(result: LottieComposition)

        fun onError(result: Throwable?)

    }

    abstract fun load(context: Context?, listener: ILottieLoadCallback?)

}