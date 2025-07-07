package com.proxy.service.imageloader.base.drawable

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.proxy.service.imageloader.base.drawable.CsGifDrawable
import com.proxy.service.imageloader.base.option.glide.callback.AnimationCallback

/**
 * @author: cangHX
 * @data: 2025/7/7 11:24
 * @desc:
 */
abstract class BaseDrawable<T : Drawable> : Drawable(), Animatable, Animatable2Compat {

    private val callbackList = ArrayList<Animatable2Compat.AnimationCallback>()
    private var animationCallback: AnimationCallback<T>? = null

    private var isAnimStarted = false

    fun setAnimationCallback(animationCallback: AnimationCallback<T>?) {
        this.animationCallback = animationCallback
    }

    override fun registerAnimationCallback(callback: Animatable2Compat.AnimationCallback) {
        callbackList.add(callback)
    }

    override fun unregisterAnimationCallback(callback: Animatable2Compat.AnimationCallback): Boolean {
        return callbackList.remove(callback)
    }

    override fun clearAnimationCallbacks() {
        callbackList.clear()
    }

    protected fun callAnimStart() {
        if (isAnimStarted){
            return
        }
        isAnimStarted = true
        onCallAnimStart(animationCallback)
    }

    protected fun callAnimEnd() {
        if (!isAnimStarted){
            return
        }
        isAnimStarted = false
        onCallAnimEnd(animationCallback)
    }

    protected open fun onCallAnimStart(animationCallback: AnimationCallback<T>?) {
        callbackList.forEach {
            it.onAnimationStart(this)
        }
    }

    protected open fun onCallAnimEnd(animationCallback: AnimationCallback<T>?) {
        callbackList.forEach {
            it.onAnimationEnd(this)
        }
    }

}