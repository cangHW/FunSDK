package com.proxy.service.camera.base.config.view.builder

import com.proxy.service.camera.base.config.view.ViewConfig
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.base.mode.CameraViewAfMode
import com.proxy.service.camera.base.mode.ViewMode

/**
 * @author: cangHX
 * @data: 2026/2/6 14:03
 * @desc:
 */
interface IBuilder {

    /**
     * 设置默认摄像头, 默认 [CameraConstants.DEFAULT_CAMERA_FACE_MODE]
     * */
    fun setCameraFaceMode(mode: CameraFaceMode): IBuilder

    /**
     * 设置 view 模式, 默认 [CameraConstants.DEFAULT_VIEW_MODE]
     * */
    fun setViewMode(mode: ViewMode): IBuilder

    /**
     * 设置相机模式, 默认 [CameraConstants.DEFAULT_CAMERA_MODE]
     * */
    fun setCameraMode(mode: CameraMode): IBuilder

    /**
     * 设置相机对焦模式, 默认 [CameraConstants.DEFAULT_CAMERA_VIEW_AF_MODE]
     * */
    fun setCameraViewAfMode(mode: CameraViewAfMode): IBuilder

    /**
     * 构建配置
     * */
    fun build(): ViewConfig

}