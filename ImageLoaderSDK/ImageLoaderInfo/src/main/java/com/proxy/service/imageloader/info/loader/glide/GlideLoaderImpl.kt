package com.proxy.service.imageloader.info.loader.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.proxy.service.imageloader.base.loader.glide.IGlideLoader
import com.proxy.service.imageloader.base.target.ITarget
import com.proxy.service.imageloader.info.info.glide.GlideInfo

/**
 * @author: cangHX
 * @data: 2024/5/16 20:51
 * @desc:
 */
open class GlideLoaderImpl<R> constructor(private val info: GlideInfo<R>) : IGlideLoader<R> {
    override fun into(imageView: ImageView?) {
        imageView?.let {
            info.getRequestBuilder()?.into(it)
        }
    }

    override fun into(target: ITarget<R>?) {
        target?.let {
            info.getRequestBuilder()?.into(object : CustomTarget<R>() {
                override fun onStart() {
                    super.onStart()
                    target.onStart()
                }

                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                    target.onLoadStarted(placeholder)
                }

                //加载完成
                override fun onResourceReady(resource: R & Any, transition: Transition<in R>?) {
                    target.onResourceReady(resource = resource)
                }


                //资源清除
                override fun onLoadCleared(placeholder: Drawable?) {
                    target.onLoadCleared(placeholder)
                }

                //加载失败
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    target.onLoadFailed(errorDrawable)
                }

                override fun onStop() {
                    super.onStop()
                    target.onStop()
                }

                override fun onDestroy() {
                    super.onDestroy()
                    target.onDestroy()
                }
            })
        }
    }
}