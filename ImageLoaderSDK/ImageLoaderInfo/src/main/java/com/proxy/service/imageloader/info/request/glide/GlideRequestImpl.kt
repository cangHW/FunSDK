package com.proxy.service.imageloader.info.request.glide

import com.proxy.service.imageloader.base.option.glide.IGlideOption
import com.proxy.service.imageloader.base.request.glide.IGlideRequest
import com.proxy.service.imageloader.info.info.glide.GlideInfo
import com.proxy.service.imageloader.info.option.glide.GlideOptionImpl
import com.proxy.service.imageloader.info.request.glide.source.AssetGlideSource
import com.proxy.service.imageloader.info.request.glide.source.PathGlideSource
import com.proxy.service.imageloader.info.request.glide.source.ResGlideSource
import com.proxy.service.imageloader.info.request.glide.source.UrlGlideSource

/**
 * @author: cangHX
 * @data: 2024/5/16 09:53
 * @desc:
 */
open class GlideRequestImpl<R>(private val info: GlideInfo<R>) :
    IGlideRequest<R> {

    override fun loadUrl(url: String): IGlideOption<R> {
        info.sourceData = UrlGlideSource(url)
        return GlideOptionImpl(info)
    }

    override fun loadPath(path: String): IGlideOption<R> {
        info.sourceData = PathGlideSource(path)
        return GlideOptionImpl(info)
    }

    override fun loadAsset(fileName: String): IGlideOption<R> {
        info.sourceData = AssetGlideSource(fileName)
        return GlideOptionImpl(info)
    }

    override fun loadRes(resourceId: Int): IGlideOption<R> {
        info.sourceData = ResGlideSource(resourceId)
        return GlideOptionImpl(info)
    }
}