package com.proxy.service.webview.base.listener

import android.view.KeyEvent
import android.view.MotionEvent

/**
 * @author: cangHX
 * @data: 2024/10/24 15:27
 * @desc:
 */
interface WebLifecycleCallback {

    /**
     * 进入屏幕
     * */
    fun onAttachedToWindow() {}

    /**
     * 离开屏幕
     * */
    fun onDetachedFromWindow() {}

    /**
     * 手指按下事件
     * */
    fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    /**
     * 手指长按事件
     * */
    fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    /**
     * 手指抬起事件
     * */
    fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    /**
     * 触摸事件
     * */
    fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }
}