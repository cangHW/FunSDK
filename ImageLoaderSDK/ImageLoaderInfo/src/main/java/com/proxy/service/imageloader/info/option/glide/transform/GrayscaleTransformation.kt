package com.proxy.service.imageloader.info.option.glide.transform

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import java.security.MessageDigest

/**
 * @author: cangHX
 * @data: 2024/5/18 11:55
 * @desc:
 */
class GrayscaleTransformation : BaseBitmapTransformation() {

    companion object {
        private const val VERSION = 1
        private const val ID =
            "com.proxy.service.imageloader.info.option.glide.transform.GrayscaleTransformation.$VERSION"
    }

    override fun transform(
        context: Context,
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val config = toTransform.config
        val bitmap = pool[width, height, config]

        setCanvasBitmapDensity(toTransform, bitmap)

        val canvas = Canvas(bitmap)
        val saturation = ColorMatrix()
        saturation.setSaturation(0f)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(saturation)
        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        return bitmap
    }

    override fun toString(): String {
        return "GrayscaleTransformation()"
    }

    override fun equals(other: Any?): Boolean {
        return other is GrayscaleTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID.toByteArray(Key.CHARSET))
    }

}