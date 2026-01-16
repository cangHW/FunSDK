package com.proxy.service.core.framework.system.screen.bar.impl

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.system.screen.bar.IBarStatus

/**
 * @author: cangHX
 * @data: 2026/1/15 21:47
 * @desc: 默认
 */
open class DefaultBarStatus : IBarStatus {

    protected val TAG = "${CoreConfig.TAG}Bar"

    override fun setStatusBarModel(window: Window, isDarkModel: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                if (isDarkModel) {
                    it.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else {
                    it.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            val systemUiVisibility = decorView.systemUiVisibility
            if (isDarkModel) {
                decorView.systemUiVisibility =
                    systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                decorView.systemUiVisibility =
                    systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    /**
     * 设置状态栏透明
     * */
    override fun setStatusBarTransparent(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val option =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            val vis = window.decorView.systemUiVisibility
            window.decorView.systemUiVisibility = option or vis
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /**
     * 设置导航栏透明
     * */
    override fun setNavigationBarTransparent(window: Window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (window.attributes.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION == 0) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }
        val decorView = window.decorView
        val vis = decorView.systemUiVisibility
        val option =
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = vis or option
    }

}