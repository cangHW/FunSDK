package com.proxy.service.imageloader.base.option.glide.format

import android.graphics.Bitmap

/**
 * @author: cangHX
 * @data: 2024/5/18 13:56
 * @desc:
 */
interface BitmapPoolCallback {

    fun getBitmap(width: Int, height: Int, config: Bitmap.Config): Bitmap

}