package com.proxy.service.core.framework.system.screen.callback

import com.proxy.service.core.framework.system.screen.enums.RotationEnum

/**
 * @author: cangHX
 * @data: 2026/4/2 11:41
 * @desc:
 */
interface SensorRotationCallback {

    /**
     * 传感器监测到旋转时回调, 不代表 activity 等实际页面的旋转情况
     *
     * @param orientation   真实角度
     * @param rotation      换算角度
     * */
    fun onRotation(orientation: Int, rotation: RotationEnum)

}