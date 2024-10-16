package com.proxy.service.core.framework.app.context.callback

/**
 * @author: cangHX
 * @data: 2024/7/2 11:32
 * @desc:
 */
interface OnAppShowStatusChangedCallback {

    /**
     * 应用进入前台
     * */
    fun onAppForeground()

    /**
     * 应用进入后台
     * */
    fun onAppBackground()

}