package com.proxy.service.imageloader.base.option.glide.format

import android.content.Context
import android.graphics.Bitmap
import java.security.MessageDigest

/**
 * @author: cangHX
 * @data: 2024/5/18 13:51
 * @desc:
 */
abstract class BitmapTransformation {

    private var callback: BitmapPoolCallback? = null

    abstract fun transform(
        context: Context,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap

    fun setBitmapPoolCallback(callback: BitmapPoolCallback) {
        this.callback = callback
    }

    protected fun getBitmapFromPool(width: Int, height: Int, config: Bitmap.Config): Bitmap {
        return callback!!.getBitmap(width, height, config)
    }

    abstract fun updateDiskCacheKey(messageDigest: MessageDigest)

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

}