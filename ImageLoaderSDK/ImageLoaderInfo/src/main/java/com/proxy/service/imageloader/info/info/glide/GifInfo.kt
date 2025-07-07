package com.proxy.service.imageloader.info.info.glide

import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.base.drawable.CsGifDrawable
import com.proxy.service.imageloader.base.option.glide.callback.AnimationCallback

/**
 * @author: cangHX
 * @data: 2024/5/16 11:13
 * @desc:
 */
open class GifInfo<R> : GlideInfo<R>() {

    var isAutoPlay: Boolean = ImageLoaderConstants.IS_AUTO_PLAY
    var loopCount: Int = -1

    var animationCallback: AnimationCallback<CsGifDrawable>? = null
}