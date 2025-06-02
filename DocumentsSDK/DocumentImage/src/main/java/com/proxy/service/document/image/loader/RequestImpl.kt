package com.proxy.service.document.image.loader

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.proxy.service.document.base.image.loader.IOption
import com.proxy.service.document.base.image.loader.IRequest
import com.proxy.service.imageloader.base.request.glide.IGlideRequest
import java.io.File

/**
 * @author: cangHX
 * @data: 2025/5/30 10:49
 * @desc:
 */
class RequestImpl(private val glideRequest: IGlideRequest<Bitmap>?) : IRequest {

    override fun loadBitmap(bitmap: Bitmap): IOption {
        val option = glideRequest?.loadBitmap(bitmap)
        return OptionImpl(option)
    }

    override fun loadDrawable(drawable: Drawable): IOption {
        val option = glideRequest?.loadDrawable(drawable)
        return OptionImpl(option)
    }

    override fun loadUrl(url: String): IOption {
        val option = glideRequest?.loadUrl(url)
        return OptionImpl(option)
    }

    override fun loadUri(uri: Uri): IOption {
        val option = glideRequest?.loadUri(uri)
        return OptionImpl(option)
    }

    override fun loadFile(file: File): IOption {
        val option = glideRequest?.loadFile(file)
        return OptionImpl(option)
    }

    override fun loadRes(resourceId: Int): IOption {
        val option = glideRequest?.loadRes(resourceId)
        return OptionImpl(option)
    }

    override fun loadByteArray(bytes: ByteArray): IOption {
        val option = glideRequest?.loadByteArray(bytes)
        return OptionImpl(option)
    }

    override fun loadAsset(assetPath: String): IOption {
        val option = glideRequest?.loadAsset(assetPath)
        return OptionImpl(option)
    }
}