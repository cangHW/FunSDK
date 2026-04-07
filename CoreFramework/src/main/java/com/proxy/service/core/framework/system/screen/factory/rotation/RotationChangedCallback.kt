package com.proxy.service.core.framework.system.screen.factory.rotation

/**
 * @author: cangHX
 * @data: 2026/4/2 10:44
 * @desc:
 */
interface RotationChangedCallback {

    /**
     * 旋转角度 [0 - 360], 如果小于 0 代表获取失败
     * */
    fun onRotationChanged(orientation: Int)
}