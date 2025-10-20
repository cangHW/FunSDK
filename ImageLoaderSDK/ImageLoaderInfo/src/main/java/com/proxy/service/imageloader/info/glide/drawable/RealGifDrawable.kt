package com.proxy.service.imageloader.info.glide.drawable

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.proxy.service.imageloader.base.drawable.CsGifDrawable
import java.nio.ByteBuffer

/**
 * @author: cangHX
 * @data: 2025/7/6 13:51
 * @desc:
 */
class RealGifDrawable(
    private val gifDrawable: GifDrawable
) : CsGifDrawable(gifDrawable) {

    private val callbackImpl = object : Callback {
        override fun invalidateDrawable(who: Drawable) {
            callback?.invalidateDrawable(this@RealGifDrawable)
        }

        override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
            callback?.scheduleDrawable(this@RealGifDrawable, what, `when`)
        }

        override fun unscheduleDrawable(who: Drawable, what: Runnable) {
            callback?.unscheduleDrawable(this@RealGifDrawable, what)
        }
    }

    init {
        gifDrawable.callback = callbackImpl
    }

    override fun setBounds(bounds: Rect) {
        super.setBounds(bounds)
        gifDrawable.bounds = bounds
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        gifDrawable.setBounds(left, top, right, bottom)
    }

    override fun getDirtyBounds(): Rect {
        return gifDrawable.dirtyBounds
    }

    override fun setChangingConfigurations(configs: Int) {
        super.setChangingConfigurations(configs)
        gifDrawable.changingConfigurations = configs
    }

    override fun getChangingConfigurations(): Int {
        return gifDrawable.changingConfigurations
    }

    override fun getSize(): Int {
        return gifDrawable.size
    }

    override fun getFirstFrame(): Bitmap? {
        return gifDrawable.firstFrame
    }

    override fun getBuffer(): ByteBuffer? {
        return gifDrawable.buffer
    }

    override fun getFrameCount(): Int {
        return gifDrawable.frameCount
    }

    override fun getFrameIndex(): Int {
        return gifDrawable.frameIndex
    }

    override fun startFromFirstFrame() {
        super.startFromFirstFrame()
        gifDrawable.startFromFirstFrame()
    }

    override fun start() {
        super.start()
        gifDrawable.start()
    }

    override fun stop() {
        super.stop()
        gifDrawable.stop()
    }

    override fun setVisible(visible: Boolean, restart: Boolean): Boolean {
        return gifDrawable.setVisible(visible, restart)
    }

    override fun getIntrinsicWidth(): Int {
        return gifDrawable.intrinsicWidth
    }

    override fun getIntrinsicHeight(): Int {
        return gifDrawable.intrinsicHeight
    }

    override fun isRunning(): Boolean {
        return gifDrawable.isRunning
    }

    override fun draw(canvas: Canvas) {
        gifDrawable.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        gifDrawable.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        gifDrawable.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return gifDrawable.opacity
    }

    override fun getConstantState(): ConstantState? {
        return gifDrawable.constantState
    }

    override fun recycle() {
        gifDrawable.recycle()
    }

    override fun setLoopCount(loopCount: Int) {
        gifDrawable.setLoopCount(loopCount)
    }
}