package com.proxy.service.imageloader.info.option.glide

import android.annotation.SuppressLint
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.proxy.service.core.framework.log.CsLogger
import com.proxy.service.imageloader.base.option.glide.BitmapTransformation
import com.proxy.service.imageloader.base.option.glide.IGlideOption
import com.proxy.service.imageloader.info.config.Config
import com.proxy.service.imageloader.info.info.glide.GlideInfo
import com.proxy.service.imageloader.info.loader.glide.GlideLoaderImpl
import com.proxy.service.imageloader.info.option.glide.transform.AlphaTransformation
import com.proxy.service.imageloader.info.option.glide.transform.BlurTransformation
import com.proxy.service.imageloader.info.option.glide.transform.ColorFilterTransformation
import com.proxy.service.imageloader.info.option.glide.transform.FitXYTransformation
import com.proxy.service.imageloader.info.option.glide.transform.GrayscaleTransformation
import com.proxy.service.imageloader.info.option.glide.transform.MaskTransformation
import com.proxy.service.imageloader.info.option.glide.transform.OutTransformation

/**
 * @author: cangHX
 * @data: 2024/5/16 09:55
 * @desc:
 */
open class GlideOptionImpl<R>(private val info: GlideInfo<R>) :
    GlideLoaderImpl<R>(info),
    IGlideOption<R> {

    private var requestOptions = info.requestOptions

    override fun size(width: Int, height: Int): IGlideOption<R> {
        requestOptions = requestOptions.override(width.coerceAtLeast(0), height.coerceAtLeast(0))
        info.requestOptions = requestOptions
        return this
    }

    override fun placeholder(placeholderId: Int): IGlideOption<R> {
        requestOptions = requestOptions.placeholder(placeholderId)
        info.requestOptions = requestOptions
        return this
    }

    override fun error(errorId: Int): IGlideOption<R> {
        requestOptions = requestOptions.error(errorId)
        info.requestOptions = requestOptions
        return this
    }

    override fun centerCrop(): IGlideOption<R> {
        val transform = CenterCrop()
        info.transformList.add(transform)
        return this
    }

    override fun centerInside(): IGlideOption<R> {
        val transform = CenterInside()
        info.transformList.add(transform)
        return this
    }

    override fun fitCenter(): IGlideOption<R> {
        val transform = FitCenter()
        info.transformList.add(transform)
        return this
    }

    override fun fitXY(): IGlideOption<R> {
        val transform = FitXYTransformation()
        info.transformList.add(transform)
        return this
    }

    override fun circleCrop(): IGlideOption<R> {
        val transform = CircleCrop()
        info.transformList.add(transform)
        return this
    }

    override fun roundedCorners(roundingRadiusPx: Int): IGlideOption<R> {
        if (roundingRadiusPx > 0) {
            val transform = RoundedCorners(roundingRadiusPx)
            info.transformList.add(transform)
        } else {
            CsLogger.tag(Config.TAG).e("roundingRadiusPx must be greater than 0.")
        }
        return this
    }

    override fun roundedCorners(
        tfRoundingRadiusPx: Float,
        trRoundingRadiusPx: Float,
        brRoundingRadiusPx: Float,
        blRoundingRadiusPx: Float
    ): IGlideOption<R> {
        val transform = GranularRoundedCorners(
            tfRoundingRadiusPx.coerceAtLeast(0f),
            trRoundingRadiusPx.coerceAtLeast(0f),
            brRoundingRadiusPx.coerceAtLeast(0f),
            blRoundingRadiusPx.coerceAtLeast(0f)
        )
        info.transformList.add(transform)
        return this
    }

    @SuppressLint("CheckResult")
    override fun blur(radius: Int, scaling: Int): IGlideOption<R> {
        val transform = BlurTransformation(
            radius.coerceAtLeast(0),
            scaling.coerceAtLeast(0)
        )
        info.transformList.add(transform)
        return this
    }

    override fun colorFilter(color: Int): IGlideOption<R> {
        val transform = ColorFilterTransformation(color)
        info.transformList.add(transform)
        return this
    }

    override fun grayscale(): IGlideOption<R> {
        val transform = GrayscaleTransformation()
        info.transformList.add(transform)
        return this
    }

    override fun mask(drawableId: Int): IGlideOption<R> {
        val transform = MaskTransformation(drawableId)
        info.transformList.add(transform)
        return this
    }

    override fun alpha(alpha: Int): IGlideOption<R> {
        val transform = AlphaTransformation(alpha.coerceAtLeast(0).coerceAtMost(255))
        info.transformList.add(transform)
        return this
    }

    override fun addTransform(bitmapTransformation: BitmapTransformation): IGlideOption<R> {
        val transform = OutTransformation(bitmapTransformation)
        info.transformList.add(transform)
        return this
    }

}