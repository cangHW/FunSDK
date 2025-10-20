package com.proxy.service.imageloader.info

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
import com.proxy.service.imageloader.base.request.pag.IPagRequest
import com.proxy.service.imageloader.base.type.IType
import com.proxy.service.imageloader.info.glide.info.GifInfo
import com.proxy.service.imageloader.info.glide.info.GlideInfo
import com.proxy.service.imageloader.info.glide.info.WebpInfo
import com.proxy.service.imageloader.info.lottie.info.LottieInfo
import com.proxy.service.imageloader.info.pag.info.PagInfo
import com.proxy.service.imageloader.info.glide.request.GifGlideRequestImpl
import com.proxy.service.imageloader.info.glide.request.GlideRequestImpl
import com.proxy.service.imageloader.info.glide.request.WebpGlideRequestImpl
import com.proxy.service.imageloader.info.lottie.request.LottieRequestImpl
import com.proxy.service.imageloader.info.pag.request.PagRequestImpl
import com.proxy.service.imageloader.info.glide.type.BitmapType
import com.proxy.service.imageloader.info.glide.type.DrawableType
import com.proxy.service.imageloader.info.glide.type.GifDrawableType

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

    override fun asPagModel(): IPagRequest {
        val pagInfo = PagInfo()
        pagInfo.copy(info)
        return PagRequestImpl(pagInfo)
    }

}