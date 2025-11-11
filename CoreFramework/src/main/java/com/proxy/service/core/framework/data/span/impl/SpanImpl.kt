package com.proxy.service.core.framework.data.span.impl

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.proxy.service.core.framework.data.span.builder.IImageBuilder
import com.proxy.service.core.framework.data.span.builder.ISpaceBuilder
import com.proxy.service.core.framework.data.span.builder.ITextBuilder
import com.proxy.service.core.framework.data.span.builder.base.IAppendBuilder

/**
 * @author: cangHX
 * @data: 2025/7/14 10:07
 * @desc:
 */
class SpanImpl : AbstractText(), IAppendBuilder {

    override fun appendText(text: CharSequence): ITextBuilder {
        applyLast()
        this.mType = TYPE_TEXT
        this.text = text
        return this
    }

    override fun appendImage(bitmap: Bitmap): IImageBuilder {
        applyLast()
        this.mType = TYPE_IMAGE
        this.imageBitmap = bitmap
        return this
    }

    override fun appendImage(drawable: Drawable): IImageBuilder {
        applyLast()
        this.mType = TYPE_IMAGE
        this.imageDrawable = drawable
        return this
    }

    override fun appendImage(uri: Uri): IImageBuilder {
        applyLast()
        this.mType = TYPE_IMAGE
        this.imageUri = uri
        return this
    }

    override fun appendImage(resourceId: Int): IImageBuilder {
        applyLast()
        this.mType = TYPE_IMAGE
        this.imageResourceId = resourceId
        return this
    }

    override fun appendSpace(widthPx: Int): ISpaceBuilder {
        applyLast()
        this.mType = TYPE_SPACE
        this.spaceWidth = widthPx
        return this
    }

    override fun appendLineEnd(): IAppendBuilder {
        applyLast()
        this.mType = TYPE_NONE
        mBuilder.append(System.lineSeparator())
        return this
    }

    override fun applyLast() {
        super.applyLast()
        this.mType = TYPE_NONE
    }
}