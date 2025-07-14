package com.proxy.service.core.framework.data.span.custom

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.span.enums.ImageAlign

/**
 * 重置图片大小并加载 span
 *
 * @author: cangHX
 * @data: 2025/7/14 14:42
 * @desc:
 */
class CustomImageResizeSpan : CustomImageSpan {

    companion object{
        private const val TAG = "${CoreConfig.TAG}Span_ImageResize"
    }

    private var imageSizeWidthPx: Int? = null
    private var imageSizeHeightPx: Int? = null
    private var imageSizeKeepAspectRatio: Boolean = true

    private var imageMaxSizeWidthPx: Int? = null
    private var imageMaxSizeHeightPx: Int? = null
    private var imageMaxSizeKeepAspectRatio: Boolean = true

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

    fun setImageSize(
        widthPx: Int?,
        heightPx: Int?,
        keepAspectRatio: Boolean
    ) {
        this.imageSizeWidthPx = widthPx
        this.imageSizeHeightPx = heightPx
        this.imageSizeKeepAspectRatio = keepAspectRatio
    }

    fun setImageMaxSize(
        maxWidthPx: Int?,
        maxHeightPx: Int?,
        keepAspectRatio: Boolean
    ) {
        this.imageMaxSizeWidthPx = maxWidthPx
        this.imageMaxSizeHeightPx = maxHeightPx
        this.imageMaxSizeKeepAspectRatio = keepAspectRatio
    }

    private var tempDrawable: Drawable? = null

    override fun getDrawable(): Drawable? {
        val drawable = super.getDrawable() ?: return null
        if (drawable != tempDrawable) {
            val tempSizeWidthPx = imageSizeWidthPx
            val tempSizeHeightPx = imageSizeHeightPx

            val tempMaxSizeWidthPx = imageMaxSizeWidthPx
            val tempMaxSizeHeightPx = imageMaxSizeHeightPx

            if (tempSizeWidthPx != null && tempSizeHeightPx != null) {
                adjustDrawableSize(
                    drawable,
                    tempSizeWidthPx,
                    tempSizeHeightPx,
                    imageSizeKeepAspectRatio
                )
            } else if (tempMaxSizeWidthPx != null && tempMaxSizeHeightPx != null) {
                adjustDrawableMaxSize(
                    drawable,
                    tempMaxSizeWidthPx,
                    tempMaxSizeHeightPx,
                    imageMaxSizeKeepAspectRatio
                )
            }

            tempDrawable = drawable
        }
        return drawable
    }


    private fun adjustDrawableSize(
        drawable: Drawable,
        widthPx: Int,
        heightPx: Int,
        keepAspectRatio: Boolean
    ) {
        val originalWidth = drawable.intrinsicWidth
        val originalHeight = drawable.intrinsicHeight

        if (originalWidth <= 0 || originalHeight <= 0) {
            CsLogger.tag(TAG).e("The width and height of the drawable are less than or equal to 0")
            return
        }

        if (keepAspectRatio) {
            val aspectRatio = originalWidth.toFloat() / originalHeight.toFloat()
            if (widthPx / heightPx.toFloat() > aspectRatio) {
                val adjustedWidth = (heightPx * aspectRatio).toInt()
                drawable.setBounds(0, 0, adjustedWidth, heightPx)
            } else {
                val adjustedHeight = (widthPx / aspectRatio).toInt()
                drawable.setBounds(0, 0, widthPx, adjustedHeight)
            }
        } else {
            drawable.setBounds(0, 0, widthPx, heightPx)
        }
    }


    private fun adjustDrawableMaxSize(
        drawable: Drawable,
        maxWidthPx: Int,
        maxHeightPx: Int,
        keepAspectRatio: Boolean
    ) {
        val originalWidth = drawable.intrinsicWidth
        val originalHeight = drawable.intrinsicHeight

        if (originalWidth <= 0 || originalHeight <= 0) {
            CsLogger.tag(TAG).e("The width and height of the drawable are less than or equal to 0")
            return
        }

        if (keepAspectRatio) {
            val aspectRatio = originalWidth.toFloat() / originalHeight.toFloat()
            if (originalWidth > maxWidthPx || originalHeight > maxHeightPx) {
                if (maxWidthPx / maxHeightPx.toFloat() > aspectRatio) {
                    val adjustedWidth = (maxHeightPx * aspectRatio).toInt()
                    drawable.setBounds(0, 0, adjustedWidth, maxHeightPx)
                } else {
                    val adjustedHeight = (maxWidthPx / aspectRatio).toInt()
                    drawable.setBounds(0, 0, maxWidthPx, adjustedHeight)
                }
            } else {
                drawable.setBounds(0, 0, originalWidth, originalHeight)
            }
        } else {
            val adjustedWidth = Math.min(originalWidth, maxWidthPx)
            val adjustedHeight = Math.min(originalHeight, maxHeightPx)
            drawable.setBounds(0, 0, adjustedWidth, adjustedHeight)
        }
    }

}