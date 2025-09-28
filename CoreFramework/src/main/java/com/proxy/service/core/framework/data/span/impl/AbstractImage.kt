package com.proxy.service.core.framework.data.span.impl

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import com.proxy.service.core.framework.data.span.builder.IImageBuilder
import com.proxy.service.core.framework.data.span.controller.image.IImageSize
import com.proxy.service.core.framework.data.span.controller.image.ImageMaxSizeController
import com.proxy.service.core.framework.data.span.controller.image.ImageHeightController
import com.proxy.service.core.framework.data.span.controller.image.ImageWidthController
import com.proxy.service.core.framework.data.span.controller.image.ImageSizeController
import com.proxy.service.core.framework.data.span.custom.CustomClickableSpan
import com.proxy.service.core.framework.data.span.custom.CustomImageResizeSpan
import com.proxy.service.core.framework.data.span.enums.ImageAlign

/**
 * @author: cangHX
 * @data: 2025/7/14 10:09
 * @desc:
 */
abstract class AbstractImage : AbstractSpace(), IImageBuilder {

    companion object {
        private val DEFAULT_IMAGE_ALIGN = ImageAlign.ALIGN_BASELINE
    }

    /**
     * 图片资源
     * */
    protected var imageBitmap: Bitmap? = null
    protected var imageDrawable: Drawable? = null
    protected var imageUri: Uri? = null
    protected var imageResourceId: Int? = null

    /**
     * 图片对齐方式
     * */
    private var alignImage = DEFAULT_IMAGE_ALIGN

    /**
     * 图片大小
     * */
    private var imageSize: IImageSize? = null

    /**
     * 点击事件
     * */
    private var imageListener: View.OnClickListener? = null

    override fun setImageAlign(align: ImageAlign): IImageBuilder {
        this.alignImage = align
        return this
    }

    override fun setImageSizeOnlyWidth(widthPx: Int, keepAspectRatio: Boolean): IImageBuilder {
        imageSize = ImageWidthController(Math.max(widthPx, 1), keepAspectRatio)
        return this
    }

    override fun setImageSizeOnlyHeight(heightPx: Int, keepAspectRatio: Boolean): IImageBuilder {
        imageSize = ImageHeightController(Math.max(heightPx, 1), keepAspectRatio)
        return this
    }

    override fun setImageSize(
        widthPx: Int,
        heightPx: Int,
        keepAspectRatio: Boolean
    ): IImageBuilder {
        imageSize = ImageSizeController(
            Math.max(widthPx, 1),
            Math.max(heightPx, 1),
            keepAspectRatio
        )
        return this
    }

    override fun setImageMaxSize(
        maxWidthPx: Int,
        maxHeightPx: Int,
        keepAspectRatio: Boolean
    ): IImageBuilder {
        imageSize = ImageMaxSizeController(
            Math.max(maxWidthPx, 1),
            Math.max(maxHeightPx, 1),
            keepAspectRatio
        )
        return this
    }

    override fun setImageClick(listener: View.OnClickListener): IImageBuilder {
        this.imageListener = listener
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

            if (imageSpan != null) {
                imageSpan.setImageSize(imageSize)
                mBuilder.setSpan(imageSpan, start, end, flag)
            }

            if (imageListener != null) {
                isHasClick = true
                mBuilder.setSpan(CustomClickableSpan(imageListener), start, end, flag)
            }
        }

        imageBitmap = null
        imageDrawable = null
        imageUri = null
        imageResourceId = null

        alignImage = DEFAULT_IMAGE_ALIGN
        imageSize = null

        imageListener = null
    }
}