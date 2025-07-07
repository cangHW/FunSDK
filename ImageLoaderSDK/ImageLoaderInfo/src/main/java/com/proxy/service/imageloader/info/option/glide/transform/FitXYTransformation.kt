package com.proxy.service.imageloader.info.option.glide.transform

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import java.security.MessageDigest

/**
 * @author: cangHX
 * @data: 2024/6/3 14:40
 * @desc:
 */
class FitXYTransformation : BaseBitmapTransformation() {

    companion object {
        private const val VERSION = 1
        private const val ID = "com.proxy.service.imageloader.info.option.glide.transform.FitXyTransformation.$VERSION"
    }

    override fun transform(
        context: Context,
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return Bitmap.createScaledBitmap(toTransform, outWidth, outHeight, false)
    }

    override fun toString(): String {
        return "FitXyTransformation"
    }

    override fun equals(other: Any?): Boolean {
        return other is FitXYTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID.toByteArray(Key.CHARSET))
    }
}