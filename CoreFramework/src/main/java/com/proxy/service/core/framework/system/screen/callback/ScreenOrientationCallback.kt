package com.proxy.service.core.framework.system.screen.callback

import com.proxy.service.core.framework.system.screen.enums.OrientationEnum

/**
 * @author: cangHX
 * @data: 2025/5/24 18:56
 * @desc:
 */
interface ScreenOrientationCallback {

    /**
     * 横竖屏变化回调
     * */
    fun onOrientationChange(orientation: OrientationEnum)

}