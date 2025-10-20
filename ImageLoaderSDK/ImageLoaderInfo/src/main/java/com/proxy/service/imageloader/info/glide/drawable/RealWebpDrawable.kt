package com.proxy.service.imageloader.info.glide.drawable

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.proxy.service.imageloader.base.drawable.CsWebpDrawable
import java.nio.ByteBuffer

/**
 * @author: cangHX
 * @data: 2025/7/6 13:51
 * @desc:
 */
class RealWebpDrawable(
    private val webpDrawable: WebpDrawable
) : CsWebpDrawable(webpDrawable) {

    private val callbackImpl = object : Callback {
        override fun invalidateDrawable(who: Drawable) {
            callback?.invalidateDrawable(this@RealWebpDrawable)
        }

        override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
            callback?.scheduleDrawable(this@RealWebpDrawable, what, `when`)
        }

        override fun unscheduleDrawable(who: Drawable, what: Runnable) {
            callback?.unscheduleDrawable(this@RealWebpDrawable, what)
        }
    }

    init {
        webpDrawable.callback = callbackImpl
    }

    override fun setBounds(bounds: Rect) {
        super.setBounds(bounds)
        webpDrawable.bounds = bounds
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        webpDrawable.setBounds(left, top, right, bottom)
    }

    override fun getDirtyBounds(): Rect {
        return webpDrawable.dirtyBounds
    }

    override fun setChangingConfigurations(configs: Int) {
        super.setChangingConfigurations(configs)
        webpDrawable.changingConfigurations = configs
    }

    override fun getChangingConfigurations(): Int {
        return webpDrawable.changingConfigurations
    }

    override fun getSize(): Int {
        return webpDrawable.size
    }

    override fun getFirstFrame(): Bitmap? {
        return webpDrawable.firstFrame
    }

    override fun getBuffer(): ByteBuffer? {
        return webpDrawable.buffer
    }

    override fun getFrameCount(): Int {
        return webpDrawable.frameCount
    }

    override fun getFrameIndex(): Int {
        return webpDrawable.frameIndex
    }

    override fun startFromFirstFrame() {
        super.startFromFirstFrame()
        webpDrawable.startFromFirstFrame()
    }

    override fun start() {
        super.start()
        webpDrawable.start()
    }

    override fun stop() {
        super.stop()
        webpDrawable.stop()
    }

    override fun setVisible(visible: Boolean, restart: Boolean): Boolean {
        return webpDrawable.setVisible(visible, restart)
    }

    override fun getIntrinsicWidth(): Int {
        return webpDrawable.intrinsicWidth
    }

    override fun getIntrinsicHeight(): Int {
        return webpDrawable.intrinsicHeight
    }

    override fun isRunning(): Boolean {
        return webpDrawable.isRunning
    }

    override fun draw(canvas: Canvas) {
        webpDrawable.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        webpDrawable.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        webpDrawable.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return webpDrawable.opacity
    }

    override fun getConstantState(): ConstantState? {
        return webpDrawable.constantState
    }

    override fun recycle() {
        webpDrawable.recycle()
    }

    override fun setLoopCount(loopCount: Int) {
        webpDrawable.setLoopCount(loopCount)
    }
}