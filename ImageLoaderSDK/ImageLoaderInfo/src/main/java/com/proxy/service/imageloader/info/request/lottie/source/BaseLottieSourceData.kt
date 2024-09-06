package com.proxy.service.imageloader.info.request.lottie.source

import android.content.Context
import com.proxy.service.imageloader.info.loader.lottie.ILottieLoadCallback

/**
 * @author: cangHX
 * @data: 2024/5/16 11:34
 * @desc:
 */
abstract class BaseLottieSourceData {

    abstract fun load(context: Context?, listener: ILottieLoadCallback?)

}