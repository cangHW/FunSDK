package com.proxy.service.camera.info.view.view

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * @author: cangHX
 * @data: 2026/4/15 11:04
 * @desc:
 */
class CustomSurfaceView : SurfaceView, SurfaceHolder.Callback {

    interface OnSurfaceViewChangedListener {
        fun surfaceCreated(holder: SurfaceHolder, width: Int, height: Int)
        fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int)
        fun surfaceLayoutChanged(holder: SurfaceHolder, width: Int, height: Int)
        fun surfaceDestroyed(holder: SurfaceHolder)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        holder.addCallback(this)
    }

    private var surfaceHolder: SurfaceHolder? = null
    private var onSurfaceViewChangedListener: OnSurfaceViewChangedListener? = null

    fun setOnSurfaceViewChangedListener(listener: OnSurfaceViewChangedListener) {
        this.onSurfaceViewChangedListener = listener
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        surfaceHolder?.let {
            onSurfaceViewChangedListener?.surfaceLayoutChanged(it, right - left, bottom - top)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceHolder = holder
        onSurfaceViewChangedListener?.surfaceCreated(holder, width, height)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        surfaceHolder = holder
        onSurfaceViewChangedListener?.surfaceChanged(holder, format, width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        surfaceHolder = null
        onSurfaceViewChangedListener?.surfaceDestroyed(holder)
    }

}