package com.proxy.service.core.framework.ui.monitor.visible.callback

/**
 * @author: cangHX
 * @data: 2024/12/4 20:02
 * @desc:
 */
interface VisibleMonitorCallback {

    /**
     * view 显示
     */
    fun onShow(tag: Any?)

    /**
     * view 隐藏
     */
    fun onGone(tag: Any?)

}