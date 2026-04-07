package com.proxy.service.core.framework.system.screen.callback

import com.proxy.service.core.framework.system.screen.enums.RotationEnum

/**
 * @author: cangHX
 * @data: 2025/5/23 17:50
 * @desc:
 */
interface ScreenRotationCallback {

    /**
     * 屏幕旋转时回调
     * */
    fun onRotation(rotation: RotationEnum)

}