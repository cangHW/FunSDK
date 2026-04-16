package com.proxy.service.camera.base.loader.camera

import com.proxy.service.camera.base.mode.loader.CameraFaceMode

/**
 * @author: cangHX
 * @data: 2026/3/30 15:06
 * @desc:
 */
interface ICameraActionGet {

    /**
     * 获取当前开启的摄像头
     * */
    fun getOpenedCameraMode(): CameraFaceMode?

}