package com.proxy.service.widget.info.dialog.window.view

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 * @author: cangHX
 * @data: 2025/11/27 17:22
 * @desc:
 */
class DialogRootView : FrameLayout {

    interface OnRootViewTouchEventCallback {
        fun dispatchTouchEvent(ev: MotionEvent?)
    }

    interface OnRootViewKeyEventCallback {
        fun dispatchKeyEvent(event: KeyEvent?): Boolean
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var touchEventCallback: OnRootViewTouchEventCallback? = null
    private var keyEventCallback: OnRootViewKeyEventCallback? = null

    fun setOnRootViewTouchEventCallback(callback: OnRootViewTouchEventCallback) {
        this.touchEventCallback = callback
    }

    fun setOnRootViewKeyEventCallback(callback: OnRootViewKeyEventCallback) {
        this.keyEventCallback = callback
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        touchEventCallback?.dispatchTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (keyEventCallback?.dispatchKeyEvent(event) == true) {
            return true
        }
        return super.dispatchKeyEvent(event)
    }
}