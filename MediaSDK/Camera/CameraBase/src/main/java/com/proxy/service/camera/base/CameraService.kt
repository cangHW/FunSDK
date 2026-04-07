package com.proxy.service.camera.base

import android.util.Size
import com.proxy.service.base.BaseService
import com.proxy.service.camera.base.loader.ICameraLoader
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.SensorOrientationMode
import com.proxy.service.camera.base.page.ICameraPageLoader
import com.proxy.service.camera.base.view.ICameraViewLoader

/**
 * @author: cangHX
 * @data: 2026/2/4 11:11
 * @desc:
 */
interface CameraService : BaseService {

    /**
     * 获取后置摄像头 ID
     * */
    fun getCameraFaceBackId(): String?

    /**
     * 获取前置摄像头 ID
     * */
    fun getCameraFaceFrontId(): String?

    /**
     * 获取摄像头支持的预览尺寸
     * */
    fun getSupportedPreviewSizes(mode: CameraFaceMode): List<Size>

    /**
     * 获取摄像头支持的拍照图片尺寸
     * */
    fun getSupportedCaptureSizes(mode: CameraFaceMode): List<Size>

    /**
     * 获取摄像头支持的录制视频尺寸
     * */
    fun getSupportedRecordSizes(mode: CameraFaceMode): List<Size>

    /**
     * 获取摄像头传感器角度
     * */
    fun getSensorOrientation(mode: CameraFaceMode): SensorOrientationMode

    /**
     * 创建相机加载器
     * */
    fun createLoader(): ICameraLoader

    /**
     * 创建相机view
     * */
    fun createViewLoader(): ICameraViewLoader

    /**
     * 创建相机页面
     * */
    fun createPageLoader(): ICameraPageLoader
}