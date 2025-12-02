package com.proxy.service.imageloader.info.glide.option.transform

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import java.security.MessageDigest

/**
 * 水印遮罩转换器
 *
 * @author: cangHX
 * @data: 2024/5/18 11:57
 * @desc:
 */
class MaskTransformation(@DrawableRes val drawableId: Int) :
    BaseBitmapTransformation() {

    companion object {
        private const val VERSION = 1
        private const val ID =
            "com.proxy.service.imageloader.info.option.glide.transform.MaskTransformation.$VERSION"
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun transform(
        context: Context, pool: BitmapPool,
        toTransform: Bitmap, outWidth: Int, outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setHasAlpha(true)

        val maskDrawable = ContextCompat.getDrawable(context, drawableId)

        setCanvasBitmapDensity(toTransform, bitmap)

        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        maskDrawable?.setBounds(0, 0, width, height)
        maskDrawable?.draw(canvas)

        canvas.setBitmap(null)
        return bitmap
    }

    override fun toString(): String {
        return "MaskTransformation(drawableId=$drawableId)"
    }

    override fun equals(other: Any?): Boolean {
        return other is MaskTransformation &&
                other.drawableId == drawableId
    }

    override fun hashCode(): Int {
        return ID.hashCode() + drawableId * 10
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + drawableId).toByteArray(Key.CHARSET))
    }

}