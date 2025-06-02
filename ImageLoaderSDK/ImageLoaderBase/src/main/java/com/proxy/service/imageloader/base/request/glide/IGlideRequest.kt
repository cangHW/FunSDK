package com.proxy.service.imageloader.base.request.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.proxy.service.imageloader.base.option.glide.IGlideOption
import com.proxy.service.imageloader.base.request.base.IRequest
import java.io.File

/**
 * @author: cangHX
 * @data: 2024/6/7 10:09
 * @desc:
 */
interface IGlideRequest<R> : IRequest<IGlideOption<R>> {

    /**
     * 加载 Bitmap
     * */
    fun loadBitmap(bitmap: Bitmap): IGlideOption<R>

    /**
     * 加载 Drawable
     * */
    fun loadDrawable(drawable: Drawable): IGlideOption<R>

    /**
     * 加载 Uri
     * */
    fun loadUri(uri: Uri): IGlideOption<R>

    /**
     * 加载 File
     * */
    fun loadFile(file: File): IGlideOption<R>

    /**
     * 加载 ByteArray
     * */
    fun loadByteArray(bytes: ByteArray): IGlideOption<R>
}