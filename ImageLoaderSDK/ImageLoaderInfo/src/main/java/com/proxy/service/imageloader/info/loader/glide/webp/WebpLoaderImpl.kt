package com.proxy.service.imageloader.info.loader.glide.webp

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.proxy.service.imageloader.base.drawable.CsWebpDrawable
import com.proxy.service.imageloader.base.loader.glide.IGlideLoader
import com.proxy.service.imageloader.base.target.CsCustomTarget
import com.proxy.service.imageloader.info.drawable.RealWebpDrawable
import com.proxy.service.imageloader.info.info.glide.WebpInfo
import com.proxy.service.imageloader.info.loader.glide.base.ViewTarget

/**
 * @author: cangHX
 * @data: 2024/5/16 20:51
 * @desc:
 */
open class WebpLoaderImpl(
    private val info: WebpInfo
) : IGlideLoader<CsWebpDrawable> {

    override fun into(imageView: ImageView?) {
        if (imageView == null) {
            return
        }
        info.getRequestBuilder()
            ?.into(object : ViewTarget<Drawable>(info.isAutoPlay, imageView) {
                override fun setResource(resource: Drawable?): Animatable? {
                    if (resource is WebpDrawable) {
                        val webpDrawable = RealWebpDrawable(resource)
                        webpDrawable.setAnimationCallback(info.animationCallback)
                        webpDrawable.setLoopCount(info.loopCount)
                        view.setImageDrawable(webpDrawable)
                        return webpDrawable
                    }
                    return null
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    info.errorCallback?.onAnimationError()
                }
            })
    }

    override fun into(target: CsCustomTarget<CsWebpDrawable>?) {
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
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {
                if (resource is WebpDrawable) {
                    val webpDrawable = RealWebpDrawable(resource)
                    webpDrawable.setAnimationCallback(info.animationCallback)
                    webpDrawable.setLoopCount(info.loopCount)
                    target.onResourceReady(webpDrawable)
                    if (info.isAutoPlay) {
                        webpDrawable.start()
                    }
                }
            }

            //资源清除
            override fun onLoadCleared(placeholder: Drawable?) {
                target.onLoadCleared(placeholder)
            }
        })
    }

    private abstract class CustomTargetImpl(
        target: CsCustomTarget<CsWebpDrawable>
    ) : CustomTarget<Drawable>(target.getWidth(), target.getHeight())
}