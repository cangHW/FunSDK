package com.proxy.service.imageloader.info.request.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.proxy.service.imageloader.base.drawable.CsWebpDrawable
import com.proxy.service.imageloader.base.option.glide.IWebpGlideOption
import com.proxy.service.imageloader.base.request.glide.IGlideRequest
import com.proxy.service.imageloader.info.info.glide.WebpInfo
import com.proxy.service.imageloader.info.option.glide.WebpGlideOptionImpl
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
class WebpGlideRequestImpl(
    private val info: WebpInfo
) : IGlideRequest<IWebpGlideOption, CsWebpDrawable> {

    override fun loadBitmap(bitmap: Bitmap): IWebpGlideOption {
        info.sourceData = BitmapGlideSource(bitmap)
        return WebpGlideOptionImpl(info)
    }

    override fun loadDrawable(drawable: Drawable): IWebpGlideOption {
        info.sourceData = DrawableGlideSource(drawable)
        return WebpGlideOptionImpl(info)
    }

    override fun loadUri(uri: Uri): IWebpGlideOption {
        info.sourceData = UriGlideSource(uri)
        return WebpGlideOptionImpl(info)
    }

    override fun loadFile(file: File): IWebpGlideOption {
        info.sourceData = FileGlideSource(file)
        return WebpGlideOptionImpl(info)
    }

    override fun loadByteArray(bytes: ByteArray): IWebpGlideOption {
        info.sourceData = ByteArrayGlideSource(bytes)
        return WebpGlideOptionImpl(info)
    }

    override fun loadUrl(url: String): IWebpGlideOption {
        info.sourceData = UrlGlideSource(url)
        return WebpGlideOptionImpl(info)
    }

    override fun loadPath(path: String): IWebpGlideOption {
        info.sourceData = PathGlideSource(path)
        return WebpGlideOptionImpl(info)
    }

    override fun loadAsset(fileName: String): IWebpGlideOption {
        info.sourceData = AssetGlideSource(fileName)
        return WebpGlideOptionImpl(info)
    }

    override fun loadRes(resourceId: Int): IWebpGlideOption {
        info.sourceData = ResGlideSource(resourceId)
        return WebpGlideOptionImpl(info)
    }
}