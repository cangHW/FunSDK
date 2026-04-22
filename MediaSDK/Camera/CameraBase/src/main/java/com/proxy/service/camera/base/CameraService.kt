package com.proxy.service.camera.base

import com.proxy.service.base.BaseService
import com.proxy.service.camera.base.loader.ICameraLoader
import com.proxy.service.camera.base.loader.info.SupportSize
import com.proxy.service.camera.base.loader.info.VideoSupportInfo
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.loader.SensorOrientationMode
import com.proxy.service.camera.base.mode.loader.VideoPatternMode
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
    fun getSupportedPreviewSizes(mode: CameraFaceMode): List<SupportSize>

    /**
     * 获取摄像头支持的拍照图片尺寸
     * */
    fun getSupportedCaptureSizes(mode: CameraFaceMode): List<SupportSize>

    /**
     * 获取摄像头支持的录制视频尺寸
     * */
    fun getSupportedRecordSizes(mode: CameraFaceMode): List<SupportSize>

    /**
     * 获取推荐的录制视频参数
     *
     * @param pattern   录像模式
     * */
    fun getRecommendRecordSizes(
        mode: CameraFaceMode,
        pattern: VideoPatternMode
    ): List<VideoSupportInfo>

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