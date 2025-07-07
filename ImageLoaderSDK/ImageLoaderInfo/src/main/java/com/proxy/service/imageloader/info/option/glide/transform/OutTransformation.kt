package com.proxy.service.imageloader.info.option.glide.transform

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.proxy.service.imageloader.base.option.glide.format.BitmapPoolCallback
import com.proxy.service.imageloader.base.option.glide.format.BitmapTransformation
import java.security.MessageDigest

/**
 * @author: cangHX
 * @data: 2024/5/18 14:01
 * @desc:
 */
class OutTransformation(
    private val transform: BitmapTransformation
) : BaseBitmapTransformation(), BitmapPoolCallback {

    private var pool: BitmapPool? = null

    init {
        transform.setBitmapPoolCallback(this)
    }

    override fun transform(
        context: Context,
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        this.pool = pool

        return transform.transform(context, toTransform, outWidth, outHeight)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        transform.updateDiskCacheKey(messageDigest)
    }

    override fun getBitmap(width: Int, height: Int, config: Bitmap.Config): Bitmap {
        return pool!!.get(width, height, config)
    }

    override fun equals(other: Any?): Boolean {
        return transform == other
    }

    override fun hashCode(): Int {
        return transform.hashCode()
    }
}