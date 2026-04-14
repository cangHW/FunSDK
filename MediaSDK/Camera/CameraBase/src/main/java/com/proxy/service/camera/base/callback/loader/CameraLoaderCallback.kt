package com.proxy.service.camera.base.callback.loader

import android.hardware.camera2.CameraDevice
import com.proxy.service.camera.base.mode.CameraErrorMode

/**
 * @author: cangHX
 * @data: 2026/3/18 20:14
 * @desc:
 */
interface CameraLoaderCallback {

    /**
     * 相机开启
     * */
    fun onCameraOpened(cameraId: String?, camera: CameraDevice)

    /**
     * 相机关闭
     * */
    fun onCameraClosed(cameraId: String?, camera: CameraDevice)

    /**
     * 断开链接
     * */
    fun onCameraDisconnected(cameraId: String?, camera: CameraDevice)

    /**
     * 出现异常
     * */
    fun onCameraError(cameraId: String?, camera: CameraDevice, error: CameraErrorMode)
}