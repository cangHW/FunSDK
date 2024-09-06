package com.proxy.service.imageloader.base.type

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.proxy.service.imageloader.base.request.glide.IGlideRequest
import com.proxy.service.imageloader.base.request.lottie.ILottieRequest

/**
 * 加载转换器
 *
 * @author: cangHX
 * @data: 2024/5/15 21:01
 * @desc:
 */
interface IType<R> : IGlideRequest<Drawable> {

    /**
     * 转换成 Drawable 加载模式
     * */
    fun asDrawableModel(): IGlideRequest<Drawable>

    /**
     * 转换成 Bitmap 加载模式
     * */
    fun asBitmapModel(): IGlideRequest<Bitmap>

    /**
     * 转换成 lottie 加载模式
     * */
    fun asLottieModel(): ILottieRequest
}