package com.proxy.service.imageloader.info.type

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.proxy.service.imageloader.base.drawable.CsGifDrawable
import com.proxy.service.imageloader.base.drawable.CsWebpDrawable
import com.proxy.service.imageloader.base.option.glide.IGifGlideOption
import com.proxy.service.imageloader.base.option.glide.IGlideOption
import com.proxy.service.imageloader.base.option.glide.IWebpGlideOption
import com.proxy.service.imageloader.base.request.glide.IGlideRequest
import com.proxy.service.imageloader.base.request.lottie.ILottieRequest
import com.proxy.service.imageloader.base.type.IType
import com.proxy.service.imageloader.info.info.glide.GifInfo
import com.proxy.service.imageloader.info.info.glide.GlideInfo
import com.proxy.service.imageloader.info.info.glide.WebpInfo
import com.proxy.service.imageloader.info.info.lottie.LottieInfo
import com.proxy.service.imageloader.info.request.glide.GifGlideRequestImpl
import com.proxy.service.imageloader.info.request.glide.GlideRequestImpl
import com.proxy.service.imageloader.info.request.glide.WebpGlideRequestImpl
import com.proxy.service.imageloader.info.request.lottie.LottieRequestImpl
import com.proxy.service.imageloader.info.type.type.BitmapType
import com.proxy.service.imageloader.info.type.type.DrawableType
import com.proxy.service.imageloader.info.type.type.GifDrawableType

/**
 * @author: cangHX
 * @data: 2024/5/16 09:54
 * @desc:
 */
class TypeImpl(
    private val info: GlideInfo<Drawable>
) : GlideRequestImpl<Drawable>(info), IType<Drawable> {

    override fun asDrawableModel(): IGlideRequest<IGlideOption<Drawable>, Drawable> {
        val drawableInfo = GlideInfo<Drawable>()
        drawableInfo.copy(info)
        drawableInfo.sourceType = DrawableType()
        return GlideRequestImpl(drawableInfo)
    }

    override fun asBitmapModel(): IGlideRequest<IGlideOption<Bitmap>, Bitmap> {
        val bitmapInfo = GlideInfo<Bitmap>()
        bitmapInfo.copy(info)
        bitmapInfo.sourceType = BitmapType()
        return GlideRequestImpl(bitmapInfo)
    }

    override fun asGifModel(): IGlideRequest<IGifGlideOption, CsGifDrawable> {
        val gifInfo = GifInfo<GifDrawable>()
        gifInfo.copy(info)
        gifInfo.sourceType = GifDrawableType()
        return GifGlideRequestImpl(gifInfo)
    }

    override fun asWebpModel(): IGlideRequest<IWebpGlideOption, CsWebpDrawable> {
        val webpInfo = WebpInfo()
        webpInfo.copy(info)
        webpInfo.sourceType = DrawableType()
        return WebpGlideRequestImpl(webpInfo)
    }

    override fun asLottieModel(): ILottieRequest {
        val lottieInfo = LottieInfo()
        lottieInfo.copy(info)
        return LottieRequestImpl(lottieInfo)
    }

}