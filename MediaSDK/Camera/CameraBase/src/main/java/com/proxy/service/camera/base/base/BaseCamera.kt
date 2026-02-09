package com.proxy.service.camera.base.base

import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.af.FocusAreaInfo

/**
 * @author: cangHX
 * @data: 2026/2/5 17:42
 * @desc:
 */
interface BaseCamera {

    /**
     * 开启相机
     * */
    fun openCamera(mode: CameraFaceMode)

    /**
     * 暂停预览
     * */
    fun pausePreview()

    /**
     * 恢复预览
     * */
    fun resumePreview()

    /**
     * 释放相机
     * */
    fun releaseCamera()
}