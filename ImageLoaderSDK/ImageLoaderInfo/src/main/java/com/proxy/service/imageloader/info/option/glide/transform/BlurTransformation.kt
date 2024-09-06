package com.proxy.service.imageloader.info.option.glide.transform

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import java.security.MessageDigest
import kotlin.math.abs


/**
 * @author: cangHX
 * @data: 2024/5/18 11:30
 * @desc:
 */
class BlurTransformation(val radius: Int = 25, val scaling: Int = 1) :
    BaseBitmapTransformation() {

    companion object {
        private const val VERSION = 1
        private const val ID = "com.proxy.service.imageloader.info.option.glide.transform.BlurTransformation.$VERSION"
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
        val scaledWidth = width / scaling
        val scaledHeight = height / scaling

        var bitmap: Bitmap = pool[scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888]

        setCanvasBitmapDensity(toTransform, bitmap)

        val canvas = Canvas(bitmap)
        canvas.scale(1 / scaling.toFloat(), 1 / scaling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        bitmap = fastBlur(bitmap, radius)
        return bitmap
    }

    override fun toString(): String {
        return "BlurTransformation(radius=$radius, sampling=$scaling)"
    }

    override fun equals(other: Any?): Boolean {
        return other is BlurTransformation && other.radius == radius && other.scaling == scaling
    }

    override fun hashCode(): Int {
        return ID.hashCode() + radius * 1000 + scaling * 10
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + radius + scaling).toByteArray(Key.CHARSET))
    }

    private fun fastBlur(bitmap: Bitmap, radius: Int): Bitmap {
        if (radius < 1) {
            return bitmap
        }

        val w = bitmap.width
        val h = bitmap.height

        val pix = IntArray(w * h)
        bitmap.getPixels(pix, 0, w, 0, 0, w, h)

        val wm = w - 1
        val hm = h - 1
        val wh = w * h
        val div = radius + radius + 1

        val r = IntArray(wh)
        val g = IntArray(wh)
        val b = IntArray(wh)

        var p: Int
        var yp: Int
        var yi = 0
        var yw = 0
        val vmin = IntArray(w.coerceAtLeast(h))

        var divsum = div + 1 shr 1
        divsum *= divsum
        val dv = IntArray(256 * divsum)
        for (i in dv.indices) {
            dv[i] = i / divsum
        }

        val stack = Array(div) {
            IntArray(
                3
            )
        }
        var stackpointer: Int
        var stackstart: Int
        var sir: IntArray
        var rbs: Int
        val r1 = radius + 1

        var rsum: Int
        var gsum: Int
        var bsum: Int
        var routsum: Int
        var goutsum: Int
        var boutsum: Int
        var rinsum: Int
        var ginsum: Int
        var binsum: Int

        for (y in 0 until h) {
            rsum = 0
            gsum = 0
            bsum = 0
            routsum = 0
            goutsum = 0
            boutsum = 0
            rinsum = 0
            ginsum = 0
            binsum = 0

            for (i in -radius .. radius){
                p = pix[yi + wm.coerceAtMost(i.coerceAtLeast(0))]
                sir = stack[i + radius]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rbs = r1 - abs(i)
                rsum += sir[0] * rbs
                gsum += sir[1] * rbs
                bsum += sir[2] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
            }

            stackpointer = radius

            for (x in 0 until w){
                r[yi] = dv[rsum]
                g[yi] = dv[gsum]
                b[yi] = dv[bsum]

                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum

                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]

                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm)
                }
                p = pix[yw + vmin[x]]

                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff

                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]

                rsum += rinsum
                gsum += ginsum
                bsum += binsum

                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer % div]

                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]

                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]

                yi++
            }
            yw += w
        }

        for (x in 0 until w){
            rsum = 0
            gsum = 0
            bsum = 0
            routsum = 0
            goutsum = 0
            boutsum = 0
            rinsum = 0
            ginsum = 0
            binsum = 0
            yp = -radius * w

            for (i in -radius .. radius){
                yi = 0.coerceAtLeast(yp) + x

                sir = stack[i + radius]

                sir[0] = r[yi]
                sir[1] = g[yi]
                sir[2] = b[yi]

                rbs = r1 - abs(i)

                rsum += r[yi] * rbs
                gsum += g[yi] * rbs
                bsum += b[yi] * rbs

                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }

                if (i < hm) {
                    yp += w
                }
            }
            yi = x
            stackpointer = radius

            for (y in 0 until h){
                pix[yi] = -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]

                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum

                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]

                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]

                if (x == 0) {
                    vmin[y] = (y + r1).coerceAtMost(hm) * w
                }
                p = x + vmin[y]

                sir[0] = r[p]
                sir[1] = g[p]
                sir[2] = b[p]

                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]

                rsum += rinsum
                gsum += ginsum
                bsum += binsum

                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer]

                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]

                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]

                yi += w
            }

        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h)

        return bitmap
    }

}