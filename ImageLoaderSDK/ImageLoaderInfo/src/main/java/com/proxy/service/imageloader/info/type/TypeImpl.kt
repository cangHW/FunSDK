package com.proxy.service.imageloader.info.type

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.proxy.service.imageloader.base.request.glide.IGlideRequest
import com.proxy.service.imageloader.base.request.lottie.ILottieRequest
import com.proxy.service.imageloader.base.type.IType
import com.proxy.service.imageloader.info.info.glide.GlideInfo
import com.proxy.service.imageloader.info.info.lottie.LottieInfo
import com.proxy.service.imageloader.info.request.glide.GlideRequestImpl
import com.proxy.service.imageloader.info.request.lottie.LottieRequestImpl
import com.proxy.service.imageloader.info.type.type.BitmapType
import com.proxy.service.imageloader.info.type.type.DrawableType

/**
 * @author: cangHX
 * @data: 2024/5/16 09:54
 * @desc:
 */
class TypeImpl(private val info: GlideInfo<Drawable>) :
    GlideRequestImpl<Drawable>(info),
    IType<Drawable> {

    override fun asDrawableModel(): IGlideRequest<Drawable> {
        val drawableInfo = GlideInfo<Drawable>()
        drawableInfo.copy(info)
        drawableInfo.sourceType = DrawableType()
        return GlideRequestImpl(drawableInfo)
    }

    override fun asBitmapModel(): IGlideRequest<Bitmap> {
        val bitmapInfo = GlideInfo<Bitmap>()
        bitmapInfo.copy(info)
        bitmapInfo.sourceType = BitmapType()
        return GlideRequestImpl(bitmapInfo)
    }

    override fun asLottieModel(): ILottieRequest {
        val lottieInfo = LottieInfo()
        lottieInfo.copy(info)
        return LottieRequestImpl(lottieInfo)
    }

}