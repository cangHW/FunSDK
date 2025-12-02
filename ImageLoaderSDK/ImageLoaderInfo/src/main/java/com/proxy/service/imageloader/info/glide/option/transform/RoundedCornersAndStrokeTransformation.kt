package com.proxy.service.imageloader.info.glide.option.transform

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.Shader
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.proxy.service.core.framework.data.log.CsLogger
import java.security.MessageDigest

/**
 * @author: cangHX
 * @data: 2025/12/1 18:10
 * @desc:
 */
class RoundedCornersAndStrokeTransformation(
    private val roundingRadius: Int,
    private val strokeColor: Int,
    private val strokeWidth: Float
) : BaseBitmapTransformation() {

    companion object {
        private const val VERSION = 1
        private const val ID =
            "com.proxy.service.imageloader.info.option.glide.transform.RoundedCornersAndStrokeTransformation.$VERSION"
    }

    override fun transform(
        context: Context,
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return if (roundingRadius != 0 && strokeWidth != 0f) {
            radiusAndStroke(pool, toTransform)
        } else if (roundingRadius != 0) {
            radius(pool, toTransform)
        } else if (strokeWidth != 0f) {
            stroke(pool, toTransform)
        } else {
            toTransform
        }
    }

    private fun radius(pool: BitmapPool, toTransform: Bitmap): Bitmap {
        return TransformationUtils.roundedCorners(pool, toTransform, roundingRadius)
    }

    private fun stroke(pool: BitmapPool, toTransform: Bitmap): Bitmap {
        val width: Int = toTransform.getWidth()
        val height: Int = toTransform.getHeight()
        val rect = RectF(strokeWidth, strokeWidth, width - strokeWidth, height - strokeWidth)

        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setHasAlpha(true)
        setCanvasBitmapDensity(toTransform, bitmap)

        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.setShader(BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP))
        canvas.drawRect(rect, paint)

        val strokeWidthHalf = -strokeWidth / 2
        rect.inset(strokeWidthHalf, strokeWidthHalf)

        paint.setShader(null)
        paint.style = Paint.Style.STROKE
        paint.color = strokeColor
        paint.strokeWidth = strokeWidth
        canvas.drawRect(rect, paint)

        canvas.setBitmap(null)

        return bitmap
    }

    private fun radiusAndStroke(pool: BitmapPool, toTransform: Bitmap): Bitmap {
        val width: Int = toTransform.getWidth()
        val height: Int = toTransform.getHeight()
        val rect = RectF(strokeWidth, strokeWidth, width - strokeWidth, height - strokeWidth)

        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setHasAlpha(true)
        setCanvasBitmapDensity(toTransform, bitmap)

        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val imageRoundingRadius = Math.max(roundingRadius - strokeWidth / 2, 0f)

        paint.setShader(BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP))
        if (imageRoundingRadius == 0f) {
            canvas.drawRect(rect, paint)
        } else {
            canvas.drawRoundRect(rect, imageRoundingRadius, imageRoundingRadius, paint)
        }

        val strokeWidthHalf = -strokeWidth / 2
        rect.inset(strokeWidthHalf, strokeWidthHalf)

        paint.setShader(null)
        paint.style = Paint.Style.STROKE
        paint.color = strokeColor
        paint.strokeWidth = strokeWidth
        canvas.drawRoundRect(rect, roundingRadius.toFloat(), roundingRadius.toFloat(), paint)

        canvas.setBitmap(null)

        return bitmap
    }

    override fun toString(): String {
        return "RoundedCornersAndStrokeTransformation(roundingRadius=$roundingRadius, strokeColor=$strokeColor, strokeWidth=$strokeWidth)"
    }

    override fun equals(other: Any?): Boolean {
        return other is RoundedCornersAndStrokeTransformation
                && other.roundingRadius == roundingRadius
                && other.strokeColor == strokeColor
                && other.strokeWidth == strokeWidth
    }

    override fun hashCode(): Int {
        return ID.hashCode() + roundingRadius * 1000 + strokeColor * 100 + strokeWidth.toInt() * 10
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + roundingRadius + strokeColor + strokeWidth).toByteArray(Key.CHARSET))
    }
}