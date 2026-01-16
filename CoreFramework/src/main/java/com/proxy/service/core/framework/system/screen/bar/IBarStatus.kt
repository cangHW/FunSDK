package com.proxy.service.core.framework.system.screen.bar

import android.view.Window

/**
 * @author: cangHX
 * @data: 2026/1/15 22:00
 * @desc:
 */
interface IBarStatus {

    /**
     * 设置状态栏模式
     * */
    fun setStatusBarModel(window: Window, isDarkModel: Boolean)

    /**
     * 设置状态栏透明
     * */
    fun setStatusBarTransparent(window: Window)

    /**
     * 设置导航栏透明
     * */
    fun setNavigationBarTransparent(window: Window)
}