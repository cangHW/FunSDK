package com.proxy.service.imageloader.base.drawable

import android.graphics.Bitmap
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.proxy.service.imageloader.base.option.glide.callback.AnimationCallback
import java.nio.ByteBuffer

/**
 * @author: cangHX
 * @data: 2025/7/6 22:27
 * @desc:
 */
abstract class CsWebpDrawable(
    animatable2Compat:Animatable2Compat
) : Drawable(), Animatable, Animatable2Compat {

    companion object {
        /**
         * 无限循环播放
         * */
        const val LOOP_FOREVER: Int = -1

        /**
         * 使用 Webp 动画内部的循环设置
         * */
        const val LOOP_INTRINSIC: Int = 0
    }

    protected val controller = AnimController<CsWebpDrawable>()

    private val animStateChangedCallback =
        object : AnimController.OnAnimStateChangedCallback<CsWebpDrawable> {
            override fun onCallAnimStart(
                list: ArrayList<Animatable2Compat.AnimationCallback>,
                animationCallback: AnimationCallback<CsWebpDrawable>?
            ) {
                animationCallback?.onAnimationStart(this@CsWebpDrawable)
                list.forEach {
                    it.onAnimationStart(this@CsWebpDrawable)
                }
            }

            override fun onCallAnimEnd(
                list: ArrayList<Animatable2Compat.AnimationCallback>,
                animationCallback: AnimationCallback<CsWebpDrawable>?
            ) {
                animationCallback?.onAnimationEnd(this@CsWebpDrawable)
                list.forEach {
                    it.onAnimationEnd(this@CsWebpDrawable)
                }
            }
        }

    private val animationCallback = object : Animatable2Compat.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
            super.onAnimationEnd(drawable)
            controller.callAnimEnd()
        }
    }

    init {
        controller.setCallback(animStateChangedCallback)
        animatable2Compat.registerAnimationCallback(animationCallback)
    }

    fun setAnimationCallback(callback: AnimationCallback<CsWebpDrawable>?) {
        controller.setAnimationCallback(callback)
    }

    override fun registerAnimationCallback(callback: Animatable2Compat.AnimationCallback) {
        controller.registerAnimationCallback(callback)
    }

    override fun unregisterAnimationCallback(callback: Animatable2Compat.AnimationCallback): Boolean {
        return controller.unregisterAnimationCallback(callback)
    }

    override fun clearAnimationCallbacks() {
        controller.clearAnimationCallbacks()
    }


    /**
     * Webp 动画的内存占用大小（单位：字节）
     * */
    abstract fun getSize(): Int

    /**
     * Webp 动画的第一帧图像
     * */
    abstract fun getFirstFrame(): Bitmap?

    /**
     * Webp 动画的原始数据
     * */
    abstract fun getBuffer(): ByteBuffer?

    /**
     * Webp 动画的总帧数
     * */
    abstract fun getFrameCount(): Int

    /**
     * 当前播放的帧索引（从 0 开始）
     * */
    abstract fun getFrameIndex(): Int

    /**
     * 从第一帧开始播放 Webp 动画
     * */
    open fun startFromFirstFrame() {
        controller.callAnimStart()
    }

    /**
     * 释放 Webp 动画的资源，避免内存泄漏
     * */
    abstract fun recycle()

    /**
     * 设置 Webp 动画的循环次数
     * */
    abstract fun setLoopCount(loopCount: Int)

    override fun start() {
        controller.callAnimStart()
    }

    override fun stop() {
        controller.callAnimEnd()
    }

}