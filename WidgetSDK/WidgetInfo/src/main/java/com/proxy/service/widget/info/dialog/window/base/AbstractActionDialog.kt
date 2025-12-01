package com.proxy.service.widget.info.dialog.window.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.proxy.service.widget.info.dialog.window.info.DialogConfig
import com.proxy.service.widget.info.dialog.window.info.ScreenOrientation
import com.proxy.service.widget.info.dialog.window.listener.OnDialogDismissListener
import com.proxy.service.widget.info.dialog.window.manager.WManager

/**
 * @author: cangHX
 * @data: 2025/11/27 16:47
 * @desc:
 */
abstract class AbstractActionDialog : AbstractConfigDialog(), IDialogAction {

    protected var params: WindowManager.LayoutParams = WManager.createParams()

    protected var context: Context? = null

    protected var rootView: ViewGroup? = null
    protected var rootContentView: View? = null

    protected var dialogDismissListener: OnDialogDismissListener? = null

    /**
     * 更新 view
     * */
    fun updateView(config: DialogConfig) {
        rootView?.let {
            setConfigToParams(config)
            WManager.updateViewLayout(it, params)
        }
    }

    /**
     * 触摸事件
     * */
    open fun onTouchEvent(event: MotionEvent?) {

    }

    /**
     * 按键事件
     * */
    open fun onKeyDown(event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed()
            return true
        }
        return false
    }

    /**
     * back 键触发
     * */
    open fun onBackPressed() {
        dismiss()
    }


    protected fun setConfigToParams(config: DialogConfig?) {
        config?.focusable?.let {
            if (it) {
                params.flags = WManager.getDefaultFlag()
            } else {
                params.flags = WManager.getDefaultFlag() or
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            }
        }

        config?.screenOrientation?.let {
            when (it) {
                ScreenOrientation.ORIENTATION_LANDSCAPE -> {
                    params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }

                ScreenOrientation.ORIENTATION_PORTRAIT -> {
                    params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }

                else -> {
                    params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
            }
        }

        config?.gravity?.let {
            params.gravity = it.gravity
        }
        config?.marginVertical?.let {
            params.x = it
        }
        config?.marginHorizontal?.let {
            params.y = it
        }
        config?.width?.let {
            params.width = it
        }
        config?.height?.let {
            params.height = it
        }
        params.title = dialogName
    }

}