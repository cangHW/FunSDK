package com.proxy.service.webview.base.listener

import android.view.KeyEvent
import android.view.MotionEvent
import com.proxy.service.webview.base.web.IWeb

/**
 * @author: cangHX
 * @date: 2024/10/24 15:27
 * @desc:
 */
interface WebEventCallback {

    /**
     * 进入屏幕
     * */
    fun onAttachedToWindow(iWeb: IWeb?) {}

    /**
     * 离开屏幕
     * */
    fun onDetachedFromWindow(iWeb: IWeb?) {}

    /**
     * 销毁
     * */
    fun onDestroy(iWeb: IWeb?) {}

    /**
     * 手指按下事件
     * */
    fun onKeyDown(iWeb: IWeb?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    /**
     * 手指长按事件
     * */
    fun onKeyLongPress(iWeb: IWeb?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    /**
     * 手指抬起事件
     * */
    fun onKeyUp(iWeb: IWeb?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    /**
     * 触摸事件
     * */
    fun onTouchEvent(iWeb: IWeb?, event: MotionEvent?): Boolean {
        return false
    }
}