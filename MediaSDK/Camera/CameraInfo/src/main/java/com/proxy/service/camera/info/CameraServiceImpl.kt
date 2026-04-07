package com.proxy.service.camera.info

import android.util.Size
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.camera.base.CameraService
import com.proxy.service.camera.base.config.page.PageConfig
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.ICameraLoader
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.SensorOrientationMode
import com.proxy.service.camera.base.page.ICameraPageLoader
import com.proxy.service.camera.base.view.ICameraViewLoader
import com.proxy.service.camera.info.loader.CameraLoaderImpl
import com.proxy.service.camera.info.loader.factory.CameraFactory
import com.proxy.service.camera.info.page.CameraPageLoaderImpl
import com.proxy.service.camera.info.view.CameraViewLoaderImpl
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/2/4 16:17
 * @desc:
 */
@CloudApiService(serviceTag = "service/camera")
class CameraServiceImpl : CameraService {

    private val tag = "${CameraConstants.TAG}Service"

    override fun getCameraFaceBackId(): String? {
        return CameraFactory.getCameraFaceBackId()
    }

    override fun getCameraFaceFrontId(): String? {
        return CameraFactory.getCameraFaceFrontId()
    }

    override fun getSupportedPreviewSizes(mode: CameraFaceMode): List<Size> {
        val cameraId = mode.getCameraId()
        if (cameraId.isNullOrEmpty()) {
            CsLogger.tag(tag)
                .e("当前摄像头模式不支持. cameraId=$cameraId, desc=${mode.getCameraDesc()}")
            return arrayListOf()
        }

        return CameraFactory.getSupportedPreviewSizes(cameraId) ?: arrayListOf()
    }

    override fun getSupportedCaptureSizes(mode: CameraFaceMode): List<Size> {
        val cameraId = mode.getCameraId()
        if (cameraId.isNullOrEmpty()) {
            CsLogger.tag(tag)
                .e("当前摄像头模式不支持. cameraId=$cameraId, desc=${mode.getCameraDesc()}")
            return arrayListOf()
        }

        return CameraFactory.getSupportedCaptureSizes(cameraId) ?: arrayListOf()
    }

    override fun getSupportedRecordSizes(mode: CameraFaceMode): List<Size> {
        val cameraId = mode.getCameraId()
        if (cameraId.isNullOrEmpty()) {
            CsLogger.tag(tag)
                .e("当前摄像头模式不支持. cameraId=$cameraId, desc=${mode.getCameraDesc()}")
            return arrayListOf()
        }

        return CameraFactory.getSupportedRecordSizes(cameraId) ?: arrayListOf()
    }

    override fun getSensorOrientation(mode: CameraFaceMode): SensorOrientationMode {
        val cameraId = mode.getCameraId()
        if (cameraId.isNullOrEmpty()) {
            CsLogger.tag(tag)
                .e("当前摄像头模式不支持. cameraId=$cameraId, desc=${mode.getCameraDesc()}")
            return SensorOrientationMode.ORIENTATION_0
        }

        val so = CameraFactory.getSensorOrientation(cameraId)
        return SensorOrientationMode.valueOf(so) ?: return SensorOrientationMode.ORIENTATION_0
    }

    override fun createLoader(): ICameraLoader {
        return CameraLoaderImpl()
    }

    override fun createViewLoader(): ICameraViewLoader {
        return CameraViewLoaderImpl()
    }

    override fun createPageLoader(): ICameraPageLoader {
        return CameraPageLoaderImpl()
    }
}