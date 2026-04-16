package com.proxy.service.camera.base.view

import com.proxy.service.camera.base.CameraService
import com.proxy.service.camera.base.loader.camera.ICameraAction
import com.proxy.service.camera.base.loader.camera.ICameraFun
import com.proxy.service.camera.base.mode.view.CameraViewAfMode

/**
 * @author: cangHX
 * @data: 2026/2/5 10:26
 * @desc:
 */
interface ICameraView : ICameraAction, ICameraFun {

    /**
     * 设置预览尺寸, 针对当前相机模式与方向有效,
     * 来源 [CameraService.getSupportedPreviewSizes]、
     * [CameraService.getSupportedCaptureSizes]、
     * [CameraService.getSupportedRecordSizes]
     * */
    fun setPreviewSize(width: Int, height: Int)

    /**
     * 设置相机对焦模式
     * */
    fun setCameraViewAfMode(mode: CameraViewAfMode)


}