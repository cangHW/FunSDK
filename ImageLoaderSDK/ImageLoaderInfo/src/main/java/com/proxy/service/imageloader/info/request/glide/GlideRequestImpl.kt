package com.proxy.service.imageloader.info.request.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.proxy.service.imageloader.base.option.glide.IGlideOption
import com.proxy.service.imageloader.base.request.glide.IGlideRequest
import com.proxy.service.imageloader.info.info.glide.GlideInfo
import com.proxy.service.imageloader.info.option.glide.GlideOptionImpl
import com.proxy.service.imageloader.info.request.glide.source.AssetGlideSource
import com.proxy.service.imageloader.info.request.glide.source.BitmapGlideSource
import com.proxy.service.imageloader.info.request.glide.source.ByteArrayGlideSource
import com.proxy.service.imageloader.info.request.glide.source.DrawableGlideSource
import com.proxy.service.imageloader.info.request.glide.source.FileGlideSource
import com.proxy.service.imageloader.info.request.glide.source.PathGlideSource
import com.proxy.service.imageloader.info.request.glide.source.ResGlideSource
import com.proxy.service.imageloader.info.request.glide.source.UriGlideSource
import com.proxy.service.imageloader.info.request.glide.source.UrlGlideSource
import java.io.File

/**
 * @author: cangHX
 * @data: 2024/5/16 09:53
 * @desc:
 */
open class GlideRequestImpl<R>(
    private val info: GlideInfo<R>
) : IGlideRequest<R> {

    override fun loadBitmap(bitmap: Bitmap): IGlideOption<R> {
        info.sourceData = BitmapGlideSource(bitmap)
        return GlideOptionImpl(info)
    }

    override fun loadDrawable(drawable: Drawable): IGlideOption<R> {
        info.sourceData = DrawableGlideSource(drawable)
        return GlideOptionImpl(info)
    }

    override fun loadUri(uri: Uri): IGlideOption<R> {
        info.sourceData = UriGlideSource(uri)
        return GlideOptionImpl(info)
    }

    override fun loadFile(file: File): IGlideOption<R> {
        info.sourceData = FileGlideSource(file)
        return GlideOptionImpl(info)
    }

    override fun loadByteArray(bytes: ByteArray): IGlideOption<R> {
        info.sourceData = ByteArrayGlideSource(bytes)
        return GlideOptionImpl(info)
    }

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