package com.proxy.service.core.framework.data.span.builder

import androidx.annotation.IntRange
import com.proxy.service.core.framework.data.span.builder.base.IAppendBuilder
import com.proxy.service.core.framework.data.span.enums.ImageAlign

/**
 * @author: cangHX
 * @data: 2025/7/11 17:11
 * @desc:
 */
interface IImageBuilder : IAppendBuilder {

    /**
     * 设置对齐方式
     * */
    fun setImageAlign(align: ImageAlign): IImageBuilder

    /**
     * 设置图片尺寸, 图片会被放大或缩小到对应尺寸
     *
     * @param keepAspectRatio   是否保持宽高比例
     * */
    fun setImageSize(
        @IntRange(from = 1) widthPx: Int,
        @IntRange(from = 1) heightPx: Int,
        keepAspectRatio: Boolean = true
    ): IImageBuilder

    /**
     * 设置图片最大尺寸, 如果图片宽高大于最大尺寸，则图片会被缩小到对应大小，如果图片宽高小于最大尺寸，则不变
     *
     * @param keepAspectRatio   是否保持宽高比例
     * */
    fun setImageMaxSize(
        @IntRange(from = 1) maxWidthPx: Int,
        @IntRange(from = 1) maxHeightPx: Int,
        keepAspectRatio: Boolean = true
    ): IImageBuilder
}