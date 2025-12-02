package com.proxy.service.imageloader.info.glide.option.transform

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.Shader
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest

/**
 * @author: cangHX
 * @data: 2025/12/1 18:11
 * @desc:
 */
class GranularRoundedCornersTransformation(
    private val tlRoundingRadiusPx: Float,
    private val trRoundingRadiusPx: Float,
    private val brRoundingRadiusPx: Float,
    private val blRoundingRadiusPx: Float,
    private val strokeColor: Int,
    private val strokeWidth: Float
) : BaseBitmapTransformation() {

    companion object {
        private const val VERSION = 1
        private const val ID =
            "com.proxy.service.imageloader.info.option.glide.transform.GranularRoundedCornersTransformation.$VERSION"
    }

    override fun transform(
        context: Context,
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val isHasRoundedCorners = tlRoundingRadiusPx != 0f ||
                trRoundingRadiusPx != 0f ||
                brRoundingRadiusPx != 0f ||
                blRoundingRadiusPx != 0f

        return if (isHasRoundedCorners && strokeWidth != 0f) {
            radiusAndStroke(pool, toTransform)
        } else if (isHasRoundedCorners) {
            radius(pool, toTransform)
        } else if (strokeWidth != 0f) {
            stroke(pool, toTransform)
        } else {
            toTransform
        }
    }

    private fun radius(pool: BitmapPool, toTransform: Bitmap): Bitmap {
        return TransformationUtils.roundedCorners(
            pool,
            toTransform,
            tlRoundingRadiusPx,
            trRoundingRadiusPx,
            brRoundingRadiusPx,
            blRoundingRadiusPx
        )
    }

    private fun stroke(pool: BitmapPool, toTransform: Bitmap): Bitmap {
        val width: Int = toTransform.getWidth()
        val height: Int = toTransform.getHeight()
        val rect = RectF(strokeWidth, strokeWidth, width - strokeWidth, height - strokeWidth)

        val bitmap: Bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
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
        val path = Path()

        val bitmap: Bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setHasAlpha(true)
        setCanvasBitmapDensity(toTransform, bitmap)

        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.setShader(BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP))
        val imageTlRoundingRadius = Math.max(tlRoundingRadiusPx - strokeWidth / 2, 0f)
        val imageTrRoundingRadius = Math.max(trRoundingRadiusPx - strokeWidth / 2, 0f)
        val imageBrRoundingRadius = Math.max(brRoundingRadiusPx - strokeWidth / 2, 0f)
        val imageBlRoundingRadius = Math.max(blRoundingRadiusPx - strokeWidth / 2, 0f)
        if (imageTlRoundingRadius == 0f && imageTrRoundingRadius == 0f && imageBrRoundingRadius == 0f && imageBlRoundingRadius == 0f) {
            canvas.drawRect(rect, paint)
        } else {
            path.addRoundRect(
                rect,
                floatArrayOf(
                    imageTlRoundingRadius,
                    imageTlRoundingRadius,
                    imageTrRoundingRadius,
                    imageTrRoundingRadius,
                    imageBrRoundingRadius,
                    imageBrRoundingRadius,
                    imageBlRoundingRadius,
                    imageBlRoundingRadius
                ),
                Path.Direction.CW
            )
            canvas.drawPath(path, paint)
        }

        val strokeWidthHalf = -strokeWidth / 2
        rect.inset(strokeWidthHalf, strokeWidthHalf)
        path.reset()
        path.addRoundRect(
            rect,
            floatArrayOf(
                tlRoundingRadiusPx,
                tlRoundingRadiusPx,
                trRoundingRadiusPx,
                trRoundingRadiusPx,
                brRoundingRadiusPx,
                brRoundingRadiusPx,
                blRoundingRadiusPx,
                blRoundingRadiusPx
            ),
            Path.Direction.CW
        )

        paint.setShader(null)
        paint.style = Paint.Style.STROKE
        paint.color = strokeColor
        paint.strokeWidth = strokeWidth
        canvas.drawPath(path, paint)

        canvas.setBitmap(null)

        return bitmap
    }

    override fun toString(): String {
        return "GranularRoundedCornersTransformation(tlRoundingRadiusPx=$tlRoundingRadiusPx, trRoundingRadiusPx=$trRoundingRadiusPx, brRoundingRadiusPx=$brRoundingRadiusPx, blRoundingRadiusPx=$blRoundingRadiusPx, strokeColor=$strokeColor, strokeWidth=$strokeWidth)"
    }

    override fun equals(other: Any?): Boolean {
        return other is GranularRoundedCornersTransformation
                && other.tlRoundingRadiusPx == tlRoundingRadiusPx
                && other.trRoundingRadiusPx == trRoundingRadiusPx
                && other.brRoundingRadiusPx == brRoundingRadiusPx
                && other.blRoundingRadiusPx == blRoundingRadiusPx
                && other.strokeColor == strokeColor
                && other.strokeWidth == strokeWidth
    }

    override fun hashCode(): Int {
        return ID.hashCode() +
                tlRoundingRadiusPx.toInt() * 1000000 +
                trRoundingRadiusPx.toInt() * 100000 +
                brRoundingRadiusPx.toInt() * 10000 +
                blRoundingRadiusPx.toInt() * 1000 +
                strokeColor * 100 +
                strokeWidth.toInt() * 10
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        val str = ID +
                tlRoundingRadiusPx +
                trRoundingRadiusPx +
                brRoundingRadiusPx +
                blRoundingRadiusPx +
                strokeColor +
                strokeWidth
        messageDigest.update(str.toByteArray(Key.CHARSET))
    }

}