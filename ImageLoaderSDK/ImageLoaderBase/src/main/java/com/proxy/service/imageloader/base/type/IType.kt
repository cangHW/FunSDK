package com.proxy.service.imageloader.base.type

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.proxy.service.imageloader.base.drawable.CsGifDrawable
import com.proxy.service.imageloader.base.drawable.CsWebpDrawable
import com.proxy.service.imageloader.base.option.glide.IGifGlideOption
import com.proxy.service.imageloader.base.option.glide.IGlideOption
import com.proxy.service.imageloader.base.option.glide.IWebpGlideOption
import com.proxy.service.imageloader.base.request.glide.IGlideRequest
import com.proxy.service.imageloader.base.request.lottie.ILottieRequest

/**
 * 加载转换器
 *
 * @author: cangHX
 * @data: 2024/5/15 21:01
 * @desc:
 */
interface IType<R> : IGlideRequest<IGlideOption<Drawable>, Drawable> {

    /**
     * 转换成 Drawable 加载模式
     * */
    fun asDrawableModel(): IGlideRequest<IGlideOption<Drawable>, Drawable>

    /**
     * 转换成 Bitmap 加载模式
     * */
    fun asBitmapModel(): IGlideRequest<IGlideOption<Bitmap>, Bitmap>

    /**
     * 转换成 Gif 加载模式
     * */
    fun asGifModel(): IGlideRequest<IGifGlideOption, CsGifDrawable>

    /**
     * 转换成 Webp 加载模式
     * */
    fun asWebpModel(): IGlideRequest<IWebpGlideOption, CsWebpDrawable>

    /**
     * 转换成 lottie 加载模式
     * */
    fun asLottieModel(): ILottieRequest
}