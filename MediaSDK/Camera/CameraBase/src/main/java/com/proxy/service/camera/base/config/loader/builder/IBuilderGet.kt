package com.proxy.service.camera.base.config.loader.builder

import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.CameraMode

/**
 * @author: cangHX
 * @data: 2026/2/5 10:58
 * @desc:
 */
interface IBuilderGet {

    /**
     * 获取相机模式
     * */
    fun getCameraMode(): CameraMode?

    /**
     * 获取相机对焦模式
     * */
    fun getCameraAfMode(): CameraAfMode

}