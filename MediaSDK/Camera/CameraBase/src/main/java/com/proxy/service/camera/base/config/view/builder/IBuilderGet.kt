package com.proxy.service.camera.base.config.view.builder

import com.proxy.service.camera.base.config.view.UserSize
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.base.mode.CameraViewAfMode
import com.proxy.service.camera.base.mode.ViewMode

/**
 * @author: cangHX
 * @data: 2026/2/6 14:03
 * @desc:
 */
interface IBuilderGet {

    /**
     * 获取全部用户设置的尺寸
     * */
    fun getAllUserSize(): List<UserSize>

    /**
     * 获取默认摄像头
     * */
    fun getCameraFaceMode(): CameraFaceMode

    /**
     * 获取 view 模式
     * */
    fun getViewMode(): ViewMode

    /**
     * 获取相机模式
     * */
    fun getCameraMode(): CameraMode

    /**
     * 获取相机对焦模式
     * */
    fun getCameraViewAfMode(): CameraViewAfMode
}