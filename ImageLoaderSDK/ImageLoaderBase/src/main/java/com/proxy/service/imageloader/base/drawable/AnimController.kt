package com.proxy.service.imageloader.base.drawable

import android.graphics.drawable.Drawable
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.proxy.service.imageloader.base.option.glide.callback.AnimationCallback

/**
 * @author: cangHX
 * @data: 2025/7/7 11:45
 * @desc:
 */
class AnimController<T : Drawable> {

    /**
     * 通过 CsGifDrawable 注册的回调
     * */
    private val callbackList = ArrayList<Animatable2Compat.AnimationCallback>()

    /**
     * 通过图片加载器注册的回调
     * */
    private var animationCallback: AnimationCallback<T>? = null

    private var isAnimStarted = false

    /**
     * 动画状态回调
     * */
    private var animStateChangedCallback: OnAnimStateChangedCallback<T>? = null

    interface OnAnimStateChangedCallback<T> {
        fun onCallAnimStart(
            list: ArrayList<Animatable2Compat.AnimationCallback>,
            animationCallback: AnimationCallback<T>?
        )

        fun onCallAnimEnd(
            list: ArrayList<Animatable2Compat.AnimationCallback>,
            animationCallback: AnimationCallback<T>?
        )
    }

    fun setCallback(callback: OnAnimStateChangedCallback<T>) {
        this.animStateChangedCallback = callback
    }

    fun setAnimationCallback(animationCallback: AnimationCallback<T>?) {
        this.animationCallback = animationCallback
    }

    fun registerAnimationCallback(callback: Animatable2Compat.AnimationCallback) {
        callbackList.add(callback)
    }

    fun unregisterAnimationCallback(callback: Animatable2Compat.AnimationCallback): Boolean {
        return callbackList.remove(callback)
    }

    fun clearAnimationCallbacks() {
        callbackList.clear()
    }


    fun callAnimStart() {
        if (isAnimStarted) {
            return
        }
        isAnimStarted = true

        animStateChangedCallback?.onCallAnimStart(callbackList, animationCallback)
    }

    fun callAnimEnd() {
        if (!isAnimStarted) {
            return
        }
        isAnimStarted = false
        animStateChangedCallback?.onCallAnimEnd(callbackList, animationCallback)
    }

}