package com.proxy.service.core.framework.data.span.custom

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.span.controller.image.IImageSize
import com.proxy.service.core.framework.data.span.enums.ImageAlign

/**
 * 重置图片大小并加载 span
 *
 * @author: cangHX
 * @data: 2025/7/14 14:42
 * @desc:
 */
class CustomImageResizeSpan : CustomImageSpan {

    companion object {
        private const val TAG = "${CoreConfig.TAG}Span_ImageResize"
    }

    private var imageSize: IImageSize? = null

    constructor(bitmap: Bitmap, verticalAlignment: ImageAlign) : super(bitmap, verticalAlignment)

    constructor(drawable: Drawable, verticalAlignment: ImageAlign) : super(
        drawable,
        verticalAlignment
    )

    constructor(uri: Uri, verticalAlignment: ImageAlign) : super(uri, verticalAlignment)

    constructor(resourceId: Int, verticalAlignment: ImageAlign) : super(
        resourceId,
        verticalAlignment
    )

    fun setImageSize(imageSize: IImageSize?) {
        this.imageSize = imageSize
    }

    private var tempDrawable: Drawable? = null

    override fun getDrawable(): Drawable? {
        val drawable = super.getDrawable() ?: return null
        if (drawable != tempDrawable) {
            val originalWidth = drawable.intrinsicWidth
            val originalHeight = drawable.intrinsicHeight

            imageSize?.let {
                if (it.setDrawableSize(originalWidth, originalHeight)) {
                    drawable.setBounds(0, 0, it.getFinalWidth(), it.getFinalHeight())
                }
            }

            tempDrawable = drawable
        }
        return drawable
    }

}