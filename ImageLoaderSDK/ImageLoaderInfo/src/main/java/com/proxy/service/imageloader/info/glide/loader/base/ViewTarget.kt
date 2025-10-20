package com.proxy.service.imageloader.info.glide.loader.base

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition

/**
 * @author: cangHX
 * @data: 2025/7/6 17:31
 * @desc:
 */
abstract class ViewTarget<R : Drawable>(
    private val isAutoPlay: Boolean,
    view: ImageView
) : ViewTarget<ImageView, R>(view), Transition.ViewAdapter {

    private var animatable: Animatable? = null

    override fun getCurrentDrawable(): Drawable? {
        return view.drawable
    }

    override fun setDrawable(drawable: Drawable?) {
        view.setImageDrawable(drawable)
    }

    override fun onLoadStarted(placeholder: Drawable?) {
        super.onLoadStarted(placeholder)
        setResourceInternal(null)
        setDrawable(placeholder)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        super.onLoadFailed(errorDrawable)
        setResourceInternal(null)
        setDrawable(errorDrawable)
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        super.onLoadCleared(placeholder)
        animatable?.stop()
        setResourceInternal(null)
        setDrawable(placeholder)
    }

    override fun onResourceReady(resource: R, transition: Transition<in R>?) {
        setResourceInternal(resource)
    }

    private var isPlaying = false

    override fun onStart() {
        if (isAutoPlay || isPlaying) {
            animatable?.start()
        }
    }

    override fun onStop() {
        isPlaying = animatable?.isRunning ?: false
        animatable?.stop()
    }

    private fun setResourceInternal(resource: R?) {
        val anim = setResource(resource)
        maybeUpdateAnimatable(anim)
    }

    private fun maybeUpdateAnimatable(anim: Animatable?) {
        animatable?.stop()
        animatable = anim
        if (isAutoPlay) {
            animatable?.start()
        }
    }

    protected abstract fun setResource(resource: R?): Animatable?

}