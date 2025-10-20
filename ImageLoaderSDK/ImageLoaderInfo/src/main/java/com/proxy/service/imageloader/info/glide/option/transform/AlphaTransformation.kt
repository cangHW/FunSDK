package com.proxy.service.imageloader.info.glide.option.transform

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import java.security.MessageDigest

/**
 * 透明度转换器
 *
 * @author: cangHX
 * @data: 2024/5/18 13:34
 * @desc:
 */
class AlphaTransformation(
    private val alpha: Int
) : BaseBitmapTransformation() {

    companion object {
        private const val VERSION = 1
        private const val ID =
            "com.proxy.service.imageloader.info.option.glide.transform.AlphaTransformation.$VERSION"
    }

    override fun transform(
        context: Context,
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val result = Bitmap.createBitmap(
            toTransform.width,
            toTransform.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(result)
        val paint = Paint()
        paint.alpha = alpha
        canvas.drawBitmap(toTransform, 0f, 0f, paint)
        return result
    }

    override fun toString(): String {
        return "AlphaTransformation(alpha=$alpha)"
    }

    override fun equals(other: Any?): Boolean {
        return other is AlphaTransformation && other.alpha == alpha
    }

    override fun hashCode(): Int {
        return ID.hashCode() + alpha * 10
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + alpha).toByteArray(Key.CHARSET))
    }
}