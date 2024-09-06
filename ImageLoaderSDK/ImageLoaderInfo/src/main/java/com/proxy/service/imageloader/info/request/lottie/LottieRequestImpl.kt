package com.proxy.service.imageloader.info.request.lottie

import com.proxy.service.imageloader.base.option.lottie.ILottieOption
import com.proxy.service.imageloader.base.request.lottie.ILottieRequest
import com.proxy.service.imageloader.info.info.lottie.LottieInfo
import com.proxy.service.imageloader.info.option.lottie.LottieOptionImpl
import com.proxy.service.imageloader.info.request.lottie.source.AssetLottieSource
import com.proxy.service.imageloader.info.request.lottie.source.PathLottieSource
import com.proxy.service.imageloader.info.request.lottie.source.ResLottieSource
import com.proxy.service.imageloader.info.request.lottie.source.UrlLottieSource

/**
 * @author: cangHX
 * @data: 2024/6/7 10:43
 * @desc:
 */
class LottieRequestImpl(private val info: LottieInfo) : ILottieRequest {
    override fun loadUrl(url: String): ILottieOption {
        info.sourceData = UrlLottieSource(url)
        return LottieOptionImpl(info)
    }

    override fun loadPath(path: String): ILottieOption {
        info.sourceData = PathLottieSource(path)
        return LottieOptionImpl(info)
    }

    override fun loadAsset(fileName: String): ILottieOption {
        info.sourceData = AssetLottieSource(fileName)
        return LottieOptionImpl(info)
    }

    override fun loadRes(resourceId: Int): ILottieOption {
        info.sourceData = ResLottieSource(resourceId)
        return LottieOptionImpl(info)
    }
}