package com.proxy.service.document.base.image.loader

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
interface IRequest {

    /**
     * 加载 bitmap
     * */
    fun loadBitmap(bitmap: Bitmap): IOption

    /**
     * 加载 Drawable
     * */
    fun loadDrawable(drawable: Drawable): IOption

    /**
     * 加载网络图片
     * */
    fun loadUrl(url: String): IOption

    /**
     * 加载 Uri
     * */
    fun loadUri(uri: Uri): IOption

    /**
     * 加载 File
     * */
    fun loadFile(file: File): IOption

    /**
     * 加载 File
     * */
    fun loadRes(@RawRes @DrawableRes resourceId: Int): IOption

    /**
     * 加载二进制数据
     * */
    fun loadByteArray(bytes: ByteArray): IOption

    /**
     * 加载 asset 文件
     *
     * assetPath 示例：asd/xxx.txt
     * */
    fun loadAsset(assetPath: String): IOption

}