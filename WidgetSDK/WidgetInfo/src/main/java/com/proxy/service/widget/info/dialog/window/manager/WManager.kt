package com.proxy.service.widget.info.dialog.window.manager

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.PixelFormat
import android.view.ViewGroup
import android.view.WindowManager
import com.proxy.service.core.framework.app.context.CsContextManager

/**
 * @author: cangHX
 * @data: 2025/11/27 17:55
 * @desc:
 */
object WManager {

    @Volatile
    private var windowManager: WindowManager? = null

    fun getDefaultFlag(): Int {
        return WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
    }

    fun createParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            getDefaultFlag() or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    }

    fun addView(viewGroup: ViewGroup, params: WindowManager.LayoutParams) {
        getWindowManager()?.addView(viewGroup, params)
    }

    fun updateViewLayout(viewGroup: ViewGroup, params: WindowManager.LayoutParams) {
        getWindowManager()?.updateViewLayout(viewGroup, params)
    }

    fun removeView(viewGroup: ViewGroup, params: WindowManager.LayoutParams) {
        params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        updateViewLayout(viewGroup, params)
        getWindowManager()?.removeView(viewGroup)
    }


    private fun getWindowManager(): WindowManager? {
        if (windowManager != null) {
            return windowManager
        }
        val context = CsContextManager.getApplication()
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager?
        return windowManager
    }

}