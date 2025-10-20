package com.proxy.service.imageloader.info.glide.request

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.proxy.service.imageloader.base.drawable.CsGifDrawable
import com.proxy.service.imageloader.base.option.glide.IGifGlideOption
import com.proxy.service.imageloader.base.request.glide.IGlideRequest
import com.proxy.service.imageloader.info.glide.info.GifInfo
import com.proxy.service.imageloader.info.glide.option.GifGlideOptionImpl
import com.proxy.service.imageloader.info.glide.request.source.AssetGlideSource
import com.proxy.service.imageloader.info.glide.request.source.BitmapGlideSource
import com.proxy.service.imageloader.info.glide.request.source.ByteArrayGlideSource
import com.proxy.service.imageloader.info.glide.request.source.DrawableGlideSource
import com.proxy.service.imageloader.info.glide.request.source.FileGlideSource
import com.proxy.service.imageloader.info.glide.request.source.PathGlideSource
import com.proxy.service.imageloader.info.glide.request.source.ResGlideSource
import com.proxy.service.imageloader.info.glide.request.source.UriGlideSource
import com.proxy.service.imageloader.info.glide.request.source.UrlGlideSource
import java.io.File

/**
 * @author: cangHX
 * @data: 2024/5/16 09:53
 * @desc:
 */
class GifGlideRequestImpl(
    private val info: GifInfo<GifDrawable>
) : IGlideRequest<IGifGlideOption, CsGifDrawable> {

    override fun loadBitmap(bitmap: Bitmap): IGifGlideOption {
        info.sourceData = BitmapGlideSource(bitmap)
        return GifGlideOptionImpl(info)
    }

    override fun loadDrawable(drawable: Drawable): IGifGlideOption {
        info.sourceData = DrawableGlideSource(drawable)
        return GifGlideOptionImpl(info)
    }

    override fun loadUri(uri: Uri): IGifGlideOption {
        info.sourceData = UriGlideSource(uri)
        return GifGlideOptionImpl(info)
    }

    override fun loadFile(file: File): IGifGlideOption {
        info.sourceData = FileGlideSource(file)
        return GifGlideOptionImpl(info)
    }

    override fun loadByteArray(bytes: ByteArray): IGifGlideOption {
        info.sourceData = ByteArrayGlideSource(bytes)
        return GifGlideOptionImpl(info)
    }

    override fun loadUrl(url: String): IGifGlideOption {
        info.sourceData = UrlGlideSource(url, null)
        return GifGlideOptionImpl(info)
    }

    override fun loadUrl(url: String, key: String): IGifGlideOption {
        info.sourceData = UrlGlideSource(url, key)
        return GifGlideOptionImpl(info)
    }

    override fun loadPath(path: String): IGifGlideOption {
        info.sourceData = PathGlideSource(path)
        return GifGlideOptionImpl(info)
    }

    override fun loadAsset(fileName: String): IGifGlideOption {
        info.sourceData = AssetGlideSource(fileName)
        return GifGlideOptionImpl(info)
    }

    override fun loadRes(resourceId: Int): IGifGlideOption {
        info.sourceData = ResGlideSource(resourceId)
        return GifGlideOptionImpl(info)
    }
}