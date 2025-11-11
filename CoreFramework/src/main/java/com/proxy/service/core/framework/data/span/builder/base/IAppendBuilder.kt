package com.proxy.service.core.framework.data.span.builder.base

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import com.proxy.service.core.framework.data.span.builder.IImageBuilder
import com.proxy.service.core.framework.data.span.builder.ISpaceBuilder
import com.proxy.service.core.framework.data.span.builder.ITextBuilder

/**
 * @author: cangHX
 * @data: 2025/7/11 17:11
 * @desc:
 */
interface IAppendBuilder : IBaseBuilder {

    /**
     * 追加普通文本
     * */
    fun appendText(text: CharSequence): ITextBuilder

    /*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

    /**
     * 追加图片
     * */
    fun appendImage(bitmap: Bitmap): IImageBuilder

    /**
     * 追加图片
     * */
    fun appendImage(drawable: Drawable): IImageBuilder

    /**
     * 追加图片
     * */
    fun appendImage(uri: Uri): IImageBuilder

    /**
     * 追加图片
     * */
    fun appendImage(@DrawableRes resourceId: Int): IImageBuilder

    /*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

    /**
     * 追加空格
     * */
    fun appendSpace(@IntRange(from = 0) widthPx: Int): ISpaceBuilder

    /*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

    /**
     * 换行
     * */
    fun appendLineEnd(): IAppendBuilder
}