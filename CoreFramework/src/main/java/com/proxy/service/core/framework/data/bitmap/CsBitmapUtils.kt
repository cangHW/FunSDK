package com.proxy.service.core.framework.data.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.WorkerThread
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max


/**
 * bitmap 操作相关工具
 *
 * @author: cangHX
 * @data: 2024/4/28 17:22
 * @desc:
 */
object CsBitmapUtils {

    private const val TAG = "${Constants.TAG}Bitmap"

    /**
     * drawable 转 bitmap
     *
     * @param drawable  待转换 Drawable
     * @param config    bitmap 清晰度
     * */
    fun toBitmap(drawable: Drawable?, config: Bitmap.Config? = null): Bitmap? {
        if (drawable == null) {
            CsLogger.tag(TAG).d("Drawable is null.")
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            return null
        }

        val cf =
            config ?: if (drawable.alpha < 255) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565

        return Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, cf).apply {
            Canvas(this).apply {
                drawable.setBounds(0, 0, width, height)
                drawable.draw(this)
            }
        }
    }

    /**
     * bitmap 转 ByteArray
     *
     * @param bitmap    待转换 bitmap
     * @param format    位图格式, 默认为 png
     * @param quality   压缩率, 默认不压缩
     * */
    @WorkerThread
    fun toBytes(
        bitmap: Bitmap?,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 100
    ): ByteArray? {
        if (bitmap == null) {
            CsLogger.tag(TAG).d("Bitmap is null.")
            return null
        }
        val bs = ByteArrayOutputStream()
        bitmap.compress(
            format, if (quality > 100) {
                100
            } else if (quality < 0) {
                0
            } else {
                quality
            }, bs
        )
        return bs.toByteArray()
    }

    /**
     * 保存 bitmap
     *
     * @param bitmap    待保存 bitmap
     * @param file      保存位置
     * @param format    位图格式, 默认为 png
     * @param quality   压缩率, 默认不压缩
     *
     * @return 成功与否
     * */
    @WorkerThread
    fun saveBitmap(
        bitmap: Bitmap?,
        file: File,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 100
    ): Boolean {
        if (bitmap == null) {
            CsLogger.tag(TAG).d("Bitmap is null.")
            return false
        }
        try {
            FileOutputStream(file).use { fos ->
                bitmap.compress(format, quality, fos)
                fos.flush()
            }
            return true
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable, "保存 bitmap 失败")
        }
        return false
    }

    /**
     * 修改 bitmap 尺寸
     *
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @param isAdjust  是否保持纵横比
     * */
    @WorkerThread
    fun resizeBitmap(
        bitmap: Bitmap?,
        maxWidth: Int,
        maxHeight: Int,
        isAdjust: Boolean
    ): Bitmap? {
        if (bitmap == null) {
            return null
        }
        if (maxWidth <= 0 || maxHeight <= 0) {
            return bitmap
        }

        if (bitmap.width < maxWidth && bitmap.height < maxHeight) {
            return bitmap
        }

        var sx = maxWidth.toFloat() / bitmap.width
        var sy = maxHeight.toFloat() / bitmap.height

        if (isAdjust) {
            val scale = max(sx.toDouble(), sy.toDouble()).toFloat()
            sy = scale
            sx = sy
        }

        val matrix = Matrix()
        matrix.postScale(sx, sy)

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * 修改 bitmap 大小
     *
     * @param maxKb  最大 size
     * */
    @WorkerThread
    fun resizeBitmap(bitmap: Bitmap, maxKb: Int): Bitmap {
        val baos = ByteArrayOutputStream()

        var quality = 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)

        if (baos.size() / 1024 <= maxKb) {
            return BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size())
        }

        var minQuality = 0
        var maxQuality = 100

        while (minQuality < maxQuality) {
            quality = (minQuality + maxQuality) / 2
            baos.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
            val sizeInKb = baos.size() / 1024
            if (sizeInKb > maxKb) {
                maxQuality = quality - 1
            } else {
                minQuality = quality + 1
            }
        }

        if (baos.size() / 1024 > maxKb) {
            baos.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, maxQuality, baos)
        }

        val compressedData = baos.toByteArray()
        return BitmapFactory.decodeByteArray(compressedData, 0, compressedData.size)
    }


}