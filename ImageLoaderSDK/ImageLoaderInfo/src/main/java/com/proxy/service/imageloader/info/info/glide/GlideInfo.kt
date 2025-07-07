package com.proxy.service.imageloader.info.info.glide

import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.request.RequestOptions
import com.proxy.service.imageloader.info.info.base.BaseInfo
import com.proxy.service.imageloader.info.request.glide.source.BaseGlideSourceData
import com.proxy.service.imageloader.info.type.type.BaseSourceType

/**
 * @author: cangHX
 * @data: 2024/5/16 11:13
 * @desc:
 */
open class GlideInfo<R> : BaseInfo() {

    var sourceType: BaseSourceType<R>? = null
    var sourceData: BaseGlideSourceData? = null

    var requestOptions: RequestOptions = RequestOptions()
    var transformList: ArrayList<Transformation<Bitmap>> = ArrayList()

    open fun getRequestBuilder(): RequestBuilder<R>? {
        var builder = sourceType?.load(getRequestManager())
        builder = sourceData?.load(builder)

        if (transformList.isNotEmpty()) {
            requestOptions = requestOptions.transform(*(transformList.toTypedArray()))
        }
        builder = builder?.apply(requestOptions)

        return builder
    }

    protected fun getRequestManager(): RequestManager? {
        activity?.let {
            return Glide.with(it)
        }
        fragment?.let {
            return Glide.with(it)
        }
        view?.let {
            return Glide.with(it)
        }
        ctx?.let {
            return Glide.with(it)
        }
        return null
    }

}