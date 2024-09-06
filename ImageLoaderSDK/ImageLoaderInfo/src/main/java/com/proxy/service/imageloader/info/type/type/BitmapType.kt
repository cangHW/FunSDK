package com.proxy.service.imageloader.info.type.type

import android.graphics.Bitmap
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.proxy.service.imageloader.info.type.type.BaseSourceType

/**
 * @author: cangHX
 * @data: 2024/5/16 14:39
 * @desc:
 */
class BitmapType: BaseSourceType<Bitmap>() {
    override fun load(requestManager: RequestManager?): RequestBuilder<Bitmap>? {
        return requestManager?.asBitmap()
    }
}