package com.proxy.service.core.service.media

import android.graphics.Rect
import com.proxy.service.api.CloudSystem
import com.proxy.service.camera.base.CameraService
import com.proxy.service.camera.base.loader.ICameraLoader
import com.proxy.service.camera.base.loader.info.SupportSize
import com.proxy.service.camera.base.loader.info.VideoSupportInfo
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.loader.SensorOrientationMode
import com.proxy.service.camera.base.mode.loader.VideoPatternMode
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
     * 获取后置摄像头 ID
     * */
    fun getCameraFaceBackId(): String? {
        return getService()?.getCameraFaceBackId()
    }

    /**
     * 获取前置摄像头 ID
     * */
    fun getCameraFaceFrontId(): String? {
        return getService()?.getCameraFaceFrontId()
    }

    /**
     * 获取摄像头支持的预览尺寸
     * */
    fun getSupportedPreviewSizes(mode: CameraFaceMode): List<SupportSize>? {
        return getService()?.getSupportedPreviewSizes(mode)
    }

    /**
     * 获取摄像头支持的拍照图片尺寸
     * */
    fun getSupportedCaptureSizes(mode: CameraFaceMode): List<SupportSize>? {
        return getService()?.getSupportedCaptureSizes(mode)
    }

    /**
     * 获取摄像头支持的录制视频尺寸
     * */
    fun getSupportedRecordSizes(mode: CameraFaceMode): List<SupportSize>? {
        return getService()?.getSupportedRecordSizes(mode)
    }

    /**
     * 获取推荐的录制视频参数
     *
     * @param pattern   录像模式
     * */
    fun getRecommendRecordInfos(
        mode: CameraFaceMode,
        pattern: VideoPatternMode
    ): List<VideoSupportInfo>? {
        return getService()?.getRecommendRecordInfos(mode, pattern)
    }

    /**
     * 获取摄像头传感器旋转角度
     * */
    fun getSensorOrientation(mode: CameraFaceMode): SensorOrientationMode? {
        return getService()?.getSensorOrientation(mode)
    }

    /**
     * 获取摄像头传感器实际产生有效图像数据的像素区域
     * */
    fun getSensorActiveArraySize(mode: CameraFaceMode): Rect? {
        return getService()?.getSensorActiveArraySize(mode)
    }

    /**
     * 创建相机加载器
     * */
    fun createLoader(): ICameraLoader? {
        return getService()?.createLoader()
    }

    /**
     * 创建相机view
     * */
    fun createViewLoader(): ICameraViewLoader? {
        return getService()?.createViewLoader()
    }

    /**
     * 创建相机页面
     * */
    fun createPageLoader(): ICameraPageLoader? {
        return getService()?.createPageLoader()
    }

}