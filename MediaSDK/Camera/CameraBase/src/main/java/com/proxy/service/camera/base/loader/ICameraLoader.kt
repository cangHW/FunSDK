package com.proxy.service.camera.base.loader

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.callback.CameraLoaderCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.camera.ICameraController
import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.CameraFaceMode

/**
 * @author: cangHX
 * @data: 2026/3/19 14:33
 * @desc:
 */
interface ICameraLoader {

    /**
     * 设置相机加载回调
     * */
    fun setCameraLoaderCallback(callback: CameraLoaderCallback): ICameraLoader

    /**
     * 绑定生命周期
     * */
    fun setLifecycleOwner(owner: LifecycleOwner): ICameraLoader

    /**
     * 设置相机对焦模式, 默认 [CameraConstants.DEFAULT_CAMERA_AF_MODE]
     * */
    fun setCameraAfMode(mode: CameraAfMode): ICameraLoader

    /**
     * 创建并开启相机
     * */
    fun createAndOpenCamera(mode: CameraFaceMode): ICameraController

    /**
     * 创建相机相机
     * */
    fun createCamera(): ICameraController
}