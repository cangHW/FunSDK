package com.proxy.service.core.framework.bitmap

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.WorkerThread
import com.proxy.service.core.framework.log.CsLogger
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * @author: cangHX
 * @data: 2024/4/28 17:22
 * @desc:
 */
object CsBitmapUtils {

    /**
     * drawable 转 bitmap
     * @param drawable  待转换 Drawable
     * @param config    bitmap 清晰度
     * */
    fun toBitmap(drawable: Drawable?, config: Bitmap.Config? = null): Bitmap? {
        if (drawable == null) {
            CsLogger.d("Drawable is null.")
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
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
            CsLogger.d("Bitmap is null.")
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
            CsLogger.d("Bitmap is null.")
            return false
        }
        try {
            FileOutputStream(file).use { fos ->
                bitmap.compress(format, quality, fos)
                fos.flush()
            }
            return true
        } catch (throwable: Throwable) {
            CsLogger.e(throwable, "保存 bitmap 失败")
        }
        return false
    }
}