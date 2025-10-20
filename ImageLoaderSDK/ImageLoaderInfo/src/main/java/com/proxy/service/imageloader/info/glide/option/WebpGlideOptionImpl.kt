package com.proxy.service.imageloader.info.glide.option

import android.annotation.SuppressLint
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.base.drawable.CsWebpDrawable
import com.proxy.service.imageloader.base.option.glide.IWebpGlideOption
import com.proxy.service.imageloader.base.option.glide.callback.AnimationCallback
import com.proxy.service.imageloader.base.option.base.LoadErrorCallback
import com.proxy.service.imageloader.base.option.glide.format.BitmapTransformation
import com.proxy.service.imageloader.base.option.glide.format.GlideDecodeFormat
import com.proxy.service.imageloader.info.glide.info.WebpInfo
import com.proxy.service.imageloader.info.glide.loader.webp.WebpLoaderImpl
import com.proxy.service.imageloader.info.glide.option.transform.AlphaTransformation
import com.proxy.service.imageloader.info.glide.option.transform.BlurTransformation
import com.proxy.service.imageloader.info.glide.option.transform.ColorFilterTransformation
import com.proxy.service.imageloader.info.glide.option.transform.FitXYTransformation
import com.proxy.service.imageloader.info.glide.option.transform.MaskTransformation
import com.proxy.service.imageloader.info.glide.option.transform.OutTransformation
import com.proxy.service.imageloader.info.glide.option.transform.SaturationTransformation

/**
 * @author: cangHX
 * @data: 2024/5/16 09:55
 * @desc:
 */
class WebpGlideOptionImpl(
    private val info: WebpInfo
) : WebpLoaderImpl(info), IWebpGlideOption {

    override fun setAutoPlay(isAutoPlay: Boolean): IWebpGlideOption {
        info.isAutoPlay = isAutoPlay
        return this
    }

    override fun setLoopCount(count: Int): IWebpGlideOption {
        info.loopCount = if (count < -1) {
            -1
        } else {
            count
        }
        return this
    }

    override fun setLoadErrorCallback(callback: LoadErrorCallback): IWebpGlideOption {
        info.loadErrorCallback = callback
        return this
    }

    override fun setAnimationCallback(callback: AnimationCallback<CsWebpDrawable>): IWebpGlideOption {
        info.animationCallback = callback
        return this
    }

    override fun size(width: Int, height: Int): IWebpGlideOption {
        info.requestOptions = info.requestOptions
            .override(
                width.coerceAtLeast(0),
                height.coerceAtLeast(0)
            )
        return this
    }

    override fun placeholder(placeholderId: Int): IWebpGlideOption {
        info.requestOptions = info.requestOptions
            .placeholder(placeholderId)
        return this
    }

    override fun error(errorId: Int): IWebpGlideOption {
        info.requestOptions = info.requestOptions.error(errorId)
        return this
    }

    override fun format(format: GlideDecodeFormat): IWebpGlideOption {
        if (format == GlideDecodeFormat.ARGB_8888) {
            info.requestOptions = info.requestOptions
                .format(DecodeFormat.PREFER_ARGB_8888)
        } else if (format == GlideDecodeFormat.RGB_565) {
            info.requestOptions = info.requestOptions
                .format(DecodeFormat.PREFER_RGB_565)
        }
        return this
    }

    override fun centerCrop(): IWebpGlideOption {
        val transform = CenterCrop()
        info.transformList.add(transform)
        return this
    }

    override fun centerInside(): IWebpGlideOption {
        val transform = CenterInside()
        info.transformList.add(transform)
        return this
    }

    override fun fitCenter(): IWebpGlideOption {
        val transform = FitCenter()
        info.transformList.add(transform)
        return this
    }

    override fun fitXY(): IWebpGlideOption {
        val transform = FitXYTransformation()
        info.transformList.add(transform)
        return this
    }

    override fun circleCrop(): IWebpGlideOption {
        val transform = CircleCrop()
        info.transformList.add(transform)
        return this
    }

    override fun roundedCorners(roundingRadiusPx: Int): IWebpGlideOption {
        if (roundingRadiusPx > 0) {
            val transform = RoundedCorners(roundingRadiusPx)
            info.transformList.add(transform)
        } else {
            CsLogger.tag(ImageLoaderConstants.TAG).e("roundingRadiusPx must be greater than 0.")
        }
        return this
    }

    override fun roundedCorners(
        tfRoundingRadiusPx: Float,
        trRoundingRadiusPx: Float,
        brRoundingRadiusPx: Float,
        blRoundingRadiusPx: Float
    ): IWebpGlideOption {
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
    override fun blur(radius: Int, scaling: Int): IWebpGlideOption {
        val transform = BlurTransformation(
            radius.coerceAtLeast(0),
            scaling.coerceAtLeast(0)
        )
        info.transformList.add(transform)
        return this
    }

    override fun colorFilter(color: Int): IWebpGlideOption {
        val transform = ColorFilterTransformation(color)
        info.transformList.add(transform)
        return this
    }

    override fun saturation(sat: Float): IWebpGlideOption {
        val saturation = if (sat < 0) {
            0f
        } else {
            sat
        }
        val transform = SaturationTransformation(saturation)
        info.transformList.add(transform)
        return this
    }

    override fun mask(drawableId: Int): IWebpGlideOption {
        val transform = MaskTransformation(drawableId)
        info.transformList.add(transform)
        return this
    }

    override fun alpha(alpha: Int): IWebpGlideOption {
        val transform = AlphaTransformation(alpha.coerceAtLeast(0).coerceAtMost(255))
        info.transformList.add(transform)
        return this
    }

    override fun addTransform(bitmapTransformation: BitmapTransformation): IWebpGlideOption {
        val transform = OutTransformation(bitmapTransformation)
        info.transformList.add(transform)
        return this
    }

}