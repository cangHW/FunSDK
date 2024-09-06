package com.proxy.service.imageloader.info.request.glide.source

import com.bumptech.glide.RequestBuilder

/**
 * @author: cangHX
 * @data: 2024/5/16 11:34
 * @desc:
 */
abstract class BaseGlideSourceData {

    abstract fun <R> load(builder: RequestBuilder<R>?): RequestBuilder<R>?

}