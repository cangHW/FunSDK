package com.proxy.service.camera.base.callback

/**
 * @author: cangHX
 * @data: 2026/3/18 20:14
 * @desc:
 */
interface CameraLoaderCallback {

    /**
     * 相机开启
     * */
    fun onCameraOpened(cameraId: String?)

    /**
     * 相机关闭
     * */
    fun onCameraClosed(cameraId: String?)

    /**
     * 断开链接
     * */
    fun onCameraDisconnected(cameraId: String?)

    /**
     * 出现异常
     * */
    fun onCameraError(cameraId: String?, errorCode: Int)
}