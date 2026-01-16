package com.proxy.service.core.framework.system.screen

import android.app.Activity
import android.view.Window
import com.proxy.service.core.framework.system.screen.bar.BarStatusFactory

/**
 * 导航栏、状态栏工具
 *
 * @author: cangHX
 * @data: 2024/6/4 10:28
 * @desc:
 */
object CsBarUtils {

    /**
     * 设置透明状态栏
     * @param isDarkModel   是否是黑夜模式, 黑夜模式下状态栏字体颜色为白色
     * */
    fun setStatusBarTransparent(activity: Activity, isDarkModel: Boolean = false) {
        BarStatusFactory.getBarStatus().setStatusBarTransparent(activity.window)
        BarStatusFactory.getBarStatus().setStatusBarModel(activity.window, isDarkModel)
    }

    /**
     * 设置透明状态栏
     * @param isDarkModel   是否是黑夜模式, 黑夜模式下状态栏字体颜色为白色
     * */
    fun setStatusBarTransparent(window: Window, isDarkModel: Boolean = false) {
        BarStatusFactory.getBarStatus().setStatusBarTransparent(window)
        BarStatusFactory.getBarStatus().setStatusBarModel(window, isDarkModel)
    }

    /**
     * 设置透明导航栏
     * */
    fun setNavigationBarTransparent(activity: Activity) {
        BarStatusFactory.getBarStatus().setNavigationBarTransparent(activity.window)
    }

    /**
     * 设置透明导航栏
     * */
    fun setNavigationBarTransparent(window: Window) {
        BarStatusFactory.getBarStatus().setNavigationBarTransparent(window)
    }

}