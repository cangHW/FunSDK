package com.proxy.service.imageloader.info.option.glide

import android.annotation.SuppressLint
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.base.drawable.CsGifDrawable
import com.proxy.service.imageloader.base.option.glide.IGifGlideOption
import com.proxy.service.imageloader.base.option.glide.callback.AnimationCallback
import com.proxy.service.imageloader.base.option.glide.callback.LoadErrorCallback
import com.proxy.service.imageloader.base.option.glide.format.BitmapTransformation
import com.proxy.service.imageloader.base.option.glide.format.GlideDecodeFormat
import com.proxy.service.imageloader.info.info.glide.GifInfo
import com.proxy.service.imageloader.info.loader.glide.gif.GifLoaderImpl
import com.proxy.service.imageloader.info.option.glide.transform.AlphaTransformation
import com.proxy.service.imageloader.info.option.glide.transform.BlurTransformation
import com.proxy.service.imageloader.info.option.glide.transform.ColorFilterTransformation
import com.proxy.service.imageloader.info.option.glide.transform.FitXYTransformation
import com.proxy.service.imageloader.info.option.glide.transform.MaskTransformation
import com.proxy.service.imageloader.info.option.glide.transform.OutTransformation
import com.proxy.service.imageloader.info.option.glide.transform.SaturationTransformation

/**
 * @author: cangHX
 * @data: 2024/5/16 09:55
 * @desc:
 */
class GifGlideOptionImpl(
    private val info: GifInfo<GifDrawable>
) : GifLoaderImpl(info), IGifGlideOption {

    override fun setAutoPlay(isAutoPlay: Boolean): IGifGlideOption {
        info.isAutoPlay = isAutoPlay
        return this
    }

    override fun setLoopCount(count: Int): IGifGlideOption {
        info.loopCount = if (count < -1) {
            -1
        } else {
            count
        }
        return this
    }

    override fun setAnimationErrorCallback(callback: LoadErrorCallback): IGifGlideOption {
        info.errorCallback = callback
        return this
    }

    override fun setAnimationCallback(callback: AnimationCallback<CsGifDrawable>): IGifGlideOption {
        info.animationCallback = callback
        return this
    }

    override fun size(width: Int, height: Int): IGifGlideOption {
        info.requestOptions = info.requestOptions
            .override(
                width.coerceAtLeast(0),
                height.coerceAtLeast(0)
            )
        return this
    }

    override fun placeholder(placeholderId: Int): IGifGlideOption {
        info.requestOptions = info.requestOptions
            .placeholder(placeholderId)
        return this
    }

    override fun error(errorId: Int): IGifGlideOption {
        info.requestOptions = info.requestOptions.error(errorId)
        return this
    }

    override fun format(format: GlideDecodeFormat): IGifGlideOption {
        if (format == GlideDecodeFormat.ARGB_8888) {
            info.requestOptions = info.requestOptions
                .format(DecodeFormat.PREFER_ARGB_8888)
        } else if (format == GlideDecodeFormat.RGB_565) {
            info.requestOptions = info.requestOptions
                .format(DecodeFormat.PREFER_RGB_565)
        }
        return this
    }

    override fun centerCrop(): IGifGlideOption {
        val transform = CenterCrop()
        info.transformList.add(transform)
        return this
    }

    override fun centerInside(): IGifGlideOption {
        val transform = CenterInside()
        info.transformList.add(transform)
        return this
    }

    override fun fitCenter(): IGifGlideOption {
        val transform = FitCenter()
        info.transformList.add(transform)
        return this
    }

    override fun fitXY(): IGifGlideOption {
        val transform = FitXYTransformation()
        info.transformList.add(transform)
        return this
    }

    override fun circleCrop(): IGifGlideOption {
        val transform = CircleCrop()
        info.transformList.add(transform)
        return this
    }

    override fun roundedCorners(roundingRadiusPx: Int): IGifGlideOption {
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
    ): IGifGlideOption {
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
    override fun blur(radius: Int, scaling: Int): IGifGlideOption {
        val transform = BlurTransformation(
            radius.coerceAtLeast(0),
            scaling.coerceAtLeast(0)
        )
        info.transformList.add(transform)
        return this
    }

    override fun colorFilter(color: Int): IGifGlideOption {
        val transform = ColorFilterTransformation(color)
        info.transformList.add(transform)
        return this
    }

    override fun saturation(sat: Float): IGifGlideOption {
        val saturation = if (sat < 0) {
            0f
        } else {
            sat
        }
        val transform = SaturationTransformation(saturation)
        info.transformList.add(transform)
        return this
    }

    override fun mask(drawableId: Int): IGifGlideOption {
        val transform = MaskTransformation(drawableId)
        info.transformList.add(transform)
        return this
    }

    override fun alpha(alpha: Int): IGifGlideOption {
        val transform = AlphaTransformation(alpha.coerceAtLeast(0).coerceAtMost(255))
        info.transformList.add(transform)
        return this
    }

    override fun addTransform(bitmapTransformation: BitmapTransformation): IGifGlideOption {
        val transform = OutTransformation(bitmapTransformation)
        info.transformList.add(transform)
        return this
    }

}