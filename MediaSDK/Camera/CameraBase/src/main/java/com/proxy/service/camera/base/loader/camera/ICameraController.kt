package com.proxy.service.camera.base.loader.camera

import com.proxy.service.camera.base.mode.CameraAfMode

/**
 * @author: cangHX
 * @data: 2026/3/19 19:07
 * @desc:
 */
interface ICameraController : ICameraFun, ICameraPreview, ICameraActionGet, ICameraFunGet {

    /**
     * 设置相机对焦模式
     * */
    fun setCameraAfMode(mode: CameraAfMode)

}