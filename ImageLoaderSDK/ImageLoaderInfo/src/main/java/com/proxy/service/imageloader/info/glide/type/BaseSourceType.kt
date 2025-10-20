package com.proxy.service.imageloader.info.glide.type

import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager

/**
 * @author: cangHX
 * @data: 2024/5/16 14:36
 * @desc:
 */
abstract class BaseSourceType<R> {

    abstract fun load(requestManager: RequestManager?): RequestBuilder<R>?

}