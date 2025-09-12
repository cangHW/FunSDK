package com.proxy.service.imageloader.info.loader.glide.gif

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.proxy.service.imageloader.base.drawable.CsGifDrawable
import com.proxy.service.imageloader.base.loader.glide.IGlideLoader
import com.proxy.service.imageloader.base.target.CsCustomTarget
import com.proxy.service.imageloader.info.drawable.RealGifDrawable
import com.proxy.service.imageloader.info.info.glide.GifInfo
import com.proxy.service.imageloader.info.loader.glide.base.ViewTarget

/**
 * @author: cangHX
 * @data: 2024/5/16 20:51
 * @desc:
 */
open class GifLoaderImpl(
    private val info: GifInfo<GifDrawable>
) : IGlideLoader<CsGifDrawable> {

    override fun into(imageView: ImageView?) {
        if (imageView == null) {
            return
        }
        info.getRequestBuilder()
            ?.into(object : ViewTarget<GifDrawable>(info.isAutoPlay, imageView) {
                override fun setResource(resource: GifDrawable?): Animatable? {
                    val gifDrawable: RealGifDrawable? = resource?.let {
                        RealGifDrawable(resource)
                    }
                    gifDrawable?.setAnimationCallback(info.animationCallback)
                    gifDrawable?.setLoopCount(info.loopCount)
                    view.setImageDrawable(gifDrawable)
                    return gifDrawable
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    info.errorCallback?.onAnimationError()
                }
            })
    }

    override fun into(target: CsCustomTarget<CsGifDrawable>?) {
        if (target == null) {
            return
        }
        info.getRequestBuilder()?.into(object : CustomTargetImpl(target) {
            override fun onStart() {
                super.onStart()
                target.onStart()
            }

            override fun onStop() {
                super.onStop()
                target.onStop()
            }

            override fun onDestroy() {
                super.onDestroy()
                target.onDestroy()
            }

            override fun onLoadStarted(placeholder: Drawable?) {
                super.onLoadStarted(placeholder)
                target.onLoadStarted(placeholder)
            }

            //加载失败
            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                target.onLoadFailed(errorDrawable)
            }

            //加载完成
            override fun onResourceReady(
                resource: GifDrawable,
                transition: Transition<in GifDrawable>?
            ) {
                val gifDrawable = RealGifDrawable(resource)
                gifDrawable.setAnimationCallback(info.animationCallback)
                gifDrawable.setLoopCount(info.loopCount)
                target.onResourceReady(gifDrawable)
                if (info.isAutoPlay) {
                    gifDrawable.start()
                }
            }

            //资源清除
            override fun onLoadCleared(placeholder: Drawable?) {
                target.onLoadCleared(placeholder)
            }
        })
    }

    private abstract class CustomTargetImpl(
        target: CsCustomTarget<CsGifDrawable>
    ) : CustomTarget<GifDrawable>(target.getWidth(), target.getHeight())
}