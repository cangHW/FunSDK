package com.proxy.service.camera.base.config.view.builder

import android.util.Size
import com.proxy.service.camera.base.CameraService
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
     * 设置相机不同模式的预览尺寸, 来源 [CameraService.getSupportedPreviewSizes]
     *
     * @param mode      相机模式, 若为 null, 则代表全部模式都满足
     * @param faceMode  相机方向, 若为 null, 则代表全部方向都满足
     * @param size      预览尺寸
     * */
    fun setCameraPreviewSize(mode: CameraMode?, faceMode: CameraFaceMode?, size: Size): IBuilder

    /**
     * 设置相机不同模式的产物尺寸, 来源 [CameraService.getSupportedPreviewSizes]、[CameraService.getSupportedPreviewSizes] 等
     *
     * @param mode      相机模式, 若为 null, 则代表全部模式都满足
     * @param faceMode  相机方向, 若为 null, 则代表全部方向都满足
     * @param size      预览尺寸
     * */
    fun setCameraOutSize(mode: CameraMode?, faceMode: CameraFaceMode?, size: Size): IBuilder

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