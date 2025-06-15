package com.proxy.service.document.image.base.loader.base

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import java.io.File

/**
 * @author: cangHX
 * @data: 2025/5/30 10:05
 * @desc:
 */
interface IRequest<T> {

    /**
     * 加载 bitmap
     * */
    fun loadBitmap(bitmap: Bitmap): T

    /**
     * 加载 Drawable
     * */
    fun loadDrawable(drawable: Drawable): T

    /**
     * 加载网络图片
     * */
    fun loadUrl(url: String): T

    /**
     * 加载 Uri
     * */
    fun loadUri(uri: Uri): T

    /**
     * 加载 File
     * */
    fun loadFile(file: File): T

    /**
     * 加载 File
     * */
    fun loadRes(@RawRes @DrawableRes resourceId: Int): T

    /**
     * 加载二进制数据
     * */
    fun loadByteArray(bytes: ByteArray): T

    /**
     * 加载 asset 文件
     *
     * assetPath 示例：asd/xxx.txt
     * */
    fun loadAsset(assetPath: String): T

}