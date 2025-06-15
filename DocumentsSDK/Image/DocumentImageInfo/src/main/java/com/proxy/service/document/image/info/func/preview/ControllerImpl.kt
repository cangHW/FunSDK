package com.proxy.service.document.image.info.func.preview

import android.graphics.Bitmap
import android.graphics.Matrix
import com.proxy.service.document.image.base.loader.base.IController
import com.proxy.service.document.image.info.drawable.ActionDrawable

/**
 * @author: cangHX
 * @data: 2025/5/30 15:03
 * @desc:
 */
class ControllerImpl : IController {

    private var drawable: ActionDrawable? = null

    fun setDrawable(drawable: ActionDrawable) {
        this.drawable = drawable
    }

    override fun getBitmap(): Bitmap? {
        return drawable?.getBitmap()
    }

    override fun getMatrix(): Matrix? {
        return drawable?.getMatrix()
    }

    override fun getCurrentScale(): Float {
        return drawable?.getCurrentScale() ?: 1f
    }

    override fun setScale(scale: Float) {
        drawable?.setScale(scale)
    }

    override fun setScale(scale: Float, focusX: Float, focusY: Float) {
        drawable?.setScale(scale, focusX, focusY)
    }
}