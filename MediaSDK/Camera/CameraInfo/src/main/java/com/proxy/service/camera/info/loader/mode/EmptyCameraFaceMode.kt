package com.proxy.service.camera.info.loader.mode

import com.proxy.service.camera.base.mode.CameraFaceMode

/**
 * @author: cangHX
 * @data: 2026/2/7 13:17
 * @desc:
 */
object EmptyCameraFaceMode: CameraFaceMode.Custom() {
    override fun getCameraId(): String? {
        return null
    }

    override fun getCameraDesc(): String {
        return "默认空相机"
    }
}