package com.proxy.service.camera.info.loader.factory

import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity.CAMERA_SERVICE
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.info.SupportSize
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

    private val supportedPreviewSizesMap = HashMap<String, List<SupportSize>>()
    private val supportedCaptureSizesMap = HashMap<String, List<SupportSize>>()
    private val supportedRecordSizesMap = HashMap<String, List<SupportSize>>()

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
    fun getSupportedPreviewSizes(cameraId: String): List<SupportSize>? {
        val size = supportedPreviewSizesMap[cameraId]
        if (size != null) {
            return size
        }
        val characteristics = manager?.getCameraCharacteristics(cameraId)
        val map = characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val supportedSizes = map?.getOutputSizes(SurfaceTexture::class.java)

        val sList = ArrayList<SupportSize>()
        supportedSizes?.forEach {
            sList.add(SupportSize.create(it.width, it.height))
        }
        if (sList.isEmpty()){
            return null
        }
        supportedPreviewSizesMap.put(cameraId, sList)
        return sList
    }

    /**
     * 获取摄像头支持的拍照尺寸
     * */
    fun getSupportedCaptureSizes(cameraId: String): List<SupportSize>? {
        val size = supportedCaptureSizesMap[cameraId]
        if (size != null) {
            return size
        }
        val characteristics = manager?.getCameraCharacteristics(cameraId)
        val map = characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val supportedSizes = map?.getOutputSizes(ImageFormat.JPEG)

        val sList = ArrayList<SupportSize>()
        supportedSizes?.forEach {
            sList.add(SupportSize.create(it.width, it.height))
        }
        if (sList.isEmpty()){
            return null
        }
        supportedCaptureSizesMap.put(cameraId, sList)
        return sList
    }

    /**
     * 获取摄像头支持的视频尺寸
     * */
    fun getSupportedRecordSizes(cameraId: String): List<SupportSize>? {
        val size = supportedRecordSizesMap[cameraId]
        if (size != null) {
            return size
        }
        val characteristics = manager?.getCameraCharacteristics(cameraId)
        val map = characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val supportedSizes = map?.getOutputSizes(MediaRecorder::class.java)

        val sList = ArrayList<SupportSize>()
        supportedSizes?.forEach {
            sList.add(SupportSize.create(it.width, it.height))
        }
        if (sList.isEmpty()){
            return null
        }
        supportedRecordSizesMap.put(cameraId, sList)
        return sList
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