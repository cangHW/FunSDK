package com.proxy.service.imageloader.base.request.lottie

import androidx.annotation.RawRes
import com.proxy.service.imageloader.base.option.lottie.ILottieOption
import com.proxy.service.imageloader.base.request.base.IRequest

/**
 * @author: cangHX
 * @data: 2024/6/7 10:09
 * @desc:
 */
interface ILottieRequest : IRequest<ILottieOption> {

    /**
     * 加载资源文件
     * */
    fun loadRes(@RawRes resourceId: Int): ILottieOption

}