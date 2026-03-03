package com.proxy.service.camera.base.config.loader.builder

import com.proxy.service.camera.base.config.loader.LoaderConfig
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.CameraMode

/**
 * @author: cangHX
 * @data: 2026/2/5 10:58
 * @desc:
 */
interface IBuilder {

    /**
     * 设置相机模式, 默认为空
     * */
    fun setCameraMode(mode: CameraMode): IBuilder

    /**
     * 设置相机对焦模式, 默认 [CameraConstants.DEFAULT_CAMERA_AF_MODE]
     * */
    fun setCameraAfMode(mode: CameraAfMode): IBuilder

    /**
     * 构建配置
     * */
    fun build(): LoaderConfig
}