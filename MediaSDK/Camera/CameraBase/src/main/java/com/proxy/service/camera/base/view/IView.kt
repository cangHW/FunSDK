package com.proxy.service.camera.base.view

import com.proxy.service.camera.base.base.BaseCameraAction
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.base.mode.CameraViewAfMode

/**
 * @author: cangHX
 * @data: 2026/2/5 10:26
 * @desc:
 */
interface IView : BaseCameraAction {

    /**
     * 设置相机模式
     * */
    fun setCameraMode(mode: CameraMode)

    /**
     * 设置相机对焦模式
     * */
    fun setCameraViewAfMode(mode: CameraViewAfMode)

}