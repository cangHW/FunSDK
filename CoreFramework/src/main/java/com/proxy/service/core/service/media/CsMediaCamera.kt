package com.proxy.service.core.service.media

import android.util.Size
import com.proxy.service.api.CloudSystem
import com.proxy.service.camera.base.CameraService
import com.proxy.service.camera.base.config.loader.LoaderConfig
import com.proxy.service.camera.base.config.page.PageConfig
import com.proxy.service.camera.base.config.view.ViewConfig
import com.proxy.service.camera.base.loader.ICameraLoader
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.SensorOrientationMode
import com.proxy.service.camera.base.page.ICameraPageLoader
import com.proxy.service.camera.base.view.ICameraViewLoader
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * 相机 加载框架入口
 *
 * @author: cangHX
 * @data: 2026/2/26 16:20
 * @desc:
 */
object CsMediaCamera {

    private const val TAG = "${CoreConfig.TAG}MediaCamera"

    private var service: CameraService? = null

    private fun getService(): CameraService? {
        if (service == null) {
            service = CloudSystem.getService(CameraService::class.java)
        }
        if (service == null) {
            CsLogger.tag(TAG)
                .e("Please check to see if it is referenced. <io.github.canghw:Service-Media-Camera:xxx>")
        }
        return service
    }


    /**
     * 获取摄像头支持的预览尺寸
     * */
    fun getSupportedPreviewSizes(mode: CameraFaceMode): List<Size>? {
        return getService()?.getSupportedPreviewSizes(mode)
    }

    /**
     * 获取摄像头支持的拍照图片尺寸
     * */
    fun getSupportedCaptureSizes(mode: CameraFaceMode): List<Size>? {
        return getService()?.getSupportedCaptureSizes(mode)
    }

    /**
     * 获取摄像头支持的录制视频尺寸
     * */
    fun getSupportedRecordSizes(mode: CameraFaceMode): List<Size>? {
        return getService()?.getSupportedRecordSizes(mode)
    }

    /**
     * 获取摄像头传感器角度
     * */
    fun getSensorOrientation(mode: CameraFaceMode): SensorOrientationMode? {
        return getService()?.getSensorOrientation(mode)
    }

    /**
     * 创建相机加载器
     * */
    fun createLoader(config: LoaderConfig): ICameraLoader? {
        return getService()?.createLoader(config)
    }

    /**
     * 创建相机view
     * */
    fun createViewLoader(config: ViewConfig): ICameraViewLoader? {
        return getService()?.createViewLoader(config)
    }

    /**
     * 创建相机页面
     * */
    fun createPageLoader(config: PageConfig): ICameraPageLoader? {
        return getService()?.createPageLoader(config)
    }

}