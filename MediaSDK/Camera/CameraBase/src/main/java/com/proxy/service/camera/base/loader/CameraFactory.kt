package com.proxy.service.camera.base.loader

import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.MediaRecorder
import android.util.Size
import androidx.appcompat.app.AppCompatActivity.CAMERA_SERVICE
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/2/5 14:50
 * @desc:
 */
object CameraFactory {

    private const val TAG = "${CameraConstants.TAG}Factory"

    private val manager by lazy {
        CsContextManager.getApplication().getSystemService(CAMERA_SERVICE) as? CameraManager?
    }

    private var faceBackId: String? = null
    private var faceFrontId: String? = null

    private val supportedPreviewSizesMap = HashMap<String, List<Size>>()
    private val supportedCaptureSizesMap = HashMap<String, List<Size>>()
    private val supportedRecordSizesMap = HashMap<String, List<Size>>()

    /**
     * 相机管理类
     * */
    fun getCameraManager(): CameraManager? {
        return manager
    }

    /**
     * 后置摄像头 ID
     * */
    fun getCameraFaceBackId(): String? {
        if (faceBackId == null) {
            checkSupportCameraId()
        }
        return faceBackId
    }

    /**
     * 前置摄像头 ID
     * */
    fun getCameraFaceFrontId(): String? {
        if (faceFrontId == null) {
            checkSupportCameraId()
        }
        return faceFrontId
    }

    /**
     * 获取摄像头支持的预览尺寸
     * */
    fun getSupportedPreviewSizes(cameraId: String): List<Size>? {
        val size = supportedPreviewSizesMap.get(cameraId)
        if (size != null) {
            return size
        }
        val characteristics = manager?.getCameraCharacteristics(cameraId)
        val map = characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val supportedSizes = map?.getOutputSizes(SurfaceTexture::class.java)
        val list = supportedSizes?.toList()
        list?.let {
            supportedPreviewSizesMap.put(cameraId, it)
        }
        return list
    }

    /**
     * 获取摄像头支持的拍照尺寸
     * */
    fun getSupportedCaptureSizes(cameraId: String): List<Size>? {
        val size = supportedCaptureSizesMap.get(cameraId)
        if (size != null) {
            return size
        }
        val characteristics = manager?.getCameraCharacteristics(cameraId)
        val map = characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val supportedSizes = map?.getOutputSizes(ImageFormat.JPEG)
        val list = supportedSizes?.toList()
        list?.let {
            supportedCaptureSizesMap.put(cameraId, it)
        }
        return list
    }

    /**
     * 获取摄像头支持的视频尺寸
     * */
    fun getSupportedRecordSizes(cameraId: String): List<Size>? {
        val size = supportedRecordSizesMap.get(cameraId)
        if (size != null) {
            return size
        }
        val characteristics = manager?.getCameraCharacteristics(cameraId)
        val map = characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val supportedSizes = map?.getOutputSizes(MediaRecorder::class.java)
        val list = supportedSizes?.toList()
        list?.let {
            supportedRecordSizesMap.put(cameraId, it)
        }
        return list
    }

    /**
     * 获取摄像头传感器角度
     * */
    fun getSensorOrientation(cameraId: String): Int {
        val characteristics = manager?.getCameraCharacteristics(cameraId)
        return characteristics?.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0
    }


    private fun checkSupportCameraId() {
        try {
            val cameraIdList = manager?.cameraIdList
            if (cameraIdList.isNullOrEmpty()) {
                faceFrontId = ""
                faceBackId = ""
                return
            }
            for (cameraId in cameraIdList) {
                val characteristics = manager?.getCameraCharacteristics(cameraId)
                val lensFacing = characteristics?.get(CameraCharacteristics.LENS_FACING)

                if (lensFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                    faceFrontId = cameraId
                } else if (lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    faceBackId = cameraId
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }
}