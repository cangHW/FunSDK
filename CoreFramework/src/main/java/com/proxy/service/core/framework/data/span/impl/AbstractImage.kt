package com.proxy.service.core.framework.data.span.impl

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.proxy.service.core.framework.data.span.builder.IImageBuilder
import com.proxy.service.core.framework.data.span.custom.CustomImageResizeSpan
import com.proxy.service.core.framework.data.span.enums.ImageAlign

/**
 * @author: cangHX
 * @data: 2025/7/14 10:09
 * @desc:
 */
abstract class AbstractImage : AbstractSpace(), IImageBuilder {

    companion object {
        private val DEFAULT_IMAGE_ALIGN = ImageAlign.ALIGN_BOTTOM
    }

    protected var imageBitmap: Bitmap? = null
    protected var imageDrawable: Drawable? = null
    protected var imageUri: Uri? = null
    protected var imageResourceId: Int? = null

    private var alignImage = DEFAULT_IMAGE_ALIGN

    private var imageSizeWidthPx: Int? = null
    private var imageSizeHeightPx: Int? = null
    private var imageSizeKeepAspectRatio: Boolean = true

    private var imageMaxSizeWidthPx: Int? = null
    private var imageMaxSizeHeightPx: Int? = null
    private var imageMaxSizeKeepAspectRatio: Boolean = true

    override fun setImageAlign(align: ImageAlign): IImageBuilder {
        this.alignImage = align
        return this
    }

    override fun setImageSize(
        widthPx: Int,
        heightPx: Int,
        keepAspectRatio: Boolean
    ): IImageBuilder {
        this.imageSizeWidthPx = Math.max(widthPx, 1)
        this.imageSizeHeightPx = Math.max(heightPx, 1)
        this.imageSizeKeepAspectRatio = keepAspectRatio
        return this
    }

    override fun setImageMaxSize(
        maxWidthPx: Int,
        maxHeightPx: Int,
        keepAspectRatio: Boolean
    ): IImageBuilder {
        this.imageMaxSizeWidthPx = Math.max(maxWidthPx, 1)
        this.imageMaxSizeHeightPx = Math.max(maxHeightPx, 1)
        this.imageMaxSizeKeepAspectRatio = keepAspectRatio
        return this
    }

    override fun applyLast() {
        super.applyLast()
        if (mType == TYPE_IMAGE) {
            val start = mBuilder.length
            mBuilder.append("<img>")
            val end = mBuilder.length

            val tempBitmap = imageBitmap
            val tempDrawable = imageDrawable
            val tempUri = imageUri
            val tempResourceId = imageResourceId

            var imageSpan: CustomImageResizeSpan? = null

            if (tempBitmap != null) {
                imageSpan = CustomImageResizeSpan(tempBitmap, alignImage)
            } else if (tempDrawable != null) {
                imageSpan = CustomImageResizeSpan(tempDrawable, alignImage)
            } else if (tempUri != null) {
                imageSpan = CustomImageResizeSpan(tempUri, alignImage)
            } else if (tempResourceId != null) {
                imageSpan = CustomImageResizeSpan(tempResourceId, alignImage)
            }

            imageSpan?.setImageSize(
                imageSizeWidthPx,
                imageSizeHeightPx,
                imageSizeKeepAspectRatio
            )

            imageSpan?.setImageMaxSize(
                imageMaxSizeWidthPx,
                imageMaxSizeHeightPx,
                imageMaxSizeKeepAspectRatio
            )

            if (imageSpan != null) {
                mBuilder.setSpan(imageSpan, start, end, flag)
            }
        }
        imageBitmap = null
        imageDrawable = null
        imageUri = null
        imageResourceId = null

        alignImage = DEFAULT_IMAGE_ALIGN

        imageSizeWidthPx = null
        imageSizeHeightPx = null
        imageSizeKeepAspectRatio = true

        imageMaxSizeWidthPx = null
        imageMaxSizeHeightPx = null
        imageMaxSizeKeepAspectRatio = true
    }
}