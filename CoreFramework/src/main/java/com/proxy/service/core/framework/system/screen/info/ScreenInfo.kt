package com.proxy.service.core.framework.system.screen.info

/**
 * @author: cangHX
 * @data: 2024/10/21 14:12
 * @desc:
 */
class ScreenInfo {

    /**
     * 屏幕宽度
     * */
    var screenWidth: Int = 0

    /**
     * 屏幕高度
     * */
    var screenHeight: Int = 0

    /**
     * 状态栏高度
     * */
    var statusBarHeight: Int = 0

    /**
     * 导航栏高度
     * */
    var navigationBarHeight: Int = 0

    /**
     * 标题栏高度
     * */
    var actionBarHeight: Int = 0

    /**
     * 屏幕像素密度
     * */
    var dpi: Int = 0

    /**
     * 是否是竖屏
     * */
    var isPortrait: Boolean = true

    override fun toString(): String {
        return "ScreenInfo(screenWidth=$screenWidth, screenHeight=$screenHeight, statusBarHeight=$statusBarHeight, navigationBarHeight=$navigationBarHeight, actionBarHeight=$actionBarHeight, dpi=$dpi, isPortrait=$isPortrait)"
    }
}