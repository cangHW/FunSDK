package com.proxy.service.camera.info.view.manager

import android.util.Size
import com.proxy.service.camera.base.CameraService
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.info.utils.CameraUtils

/**
 * @author: cangHX
 * @data: 2026/3/3 17:11
 * @desc:
 */
class CalculateSizeManager {

    companion object {
        fun create(): CalculateSizeManager {
            return CalculateSizeManager()
        }
    }

    private val calculatePreviewSizeMap = HashMap<String, Size>()
    private val calculateOutSizeMap = HashMap<String, Size>()

    /**
     * 设置来自用户的预览尺寸
     * */
    fun setUserPreviewSize(mode: CameraMode?, faceMode: CameraFaceMode?, size: Size) {
        calculatePreviewSizeMap.put(createUserKey(mode, faceMode), size)
    }

    /**
     * 设置来自用户的产物尺寸
     * */
    fun setUserOutSize(mode: CameraMode?, faceMode: CameraFaceMode?, size: Size) {
        calculateOutSizeMap.put(createUserKey(mode, faceMode), size)
    }


    /**
     * 获取预览尺寸
     * */
    fun getPreviewSize(
        service: CameraService,
        mode: CameraMode,
        faceMode: CameraFaceMode,
        width: Int,
        height: Int
    ): Size? {
        return getCalculateSize(service, calculatePreviewSizeMap, mode, faceMode, width, height)
    }

    /**
     * 获取产物尺寸
     * */
    fun getOutSize(
        service: CameraService,
        mode: CameraMode,
        faceMode: CameraFaceMode,
        width: Int,
        height: Int
    ): Size? {
        return getCalculateSize(service, calculateOutSizeMap, mode, faceMode, width, height)
    }


    private fun getCalculateSize(
        service: CameraService,
        map: HashMap<String, Size>,
        mode: CameraMode,
        faceMode: CameraFaceMode,
        width: Int,
        height: Int
    ): Size? {
        var tempKey = createUserKey(mode, faceMode)
        var size = map.get(tempKey)
        if (size != null) {
            return size
        }

        tempKey = createUserKey(null, faceMode)
        size = map.get(tempKey)
        if (size != null) {
            return size
        }

        tempKey = createUserKey(mode, null)
        size = map.get(tempKey)
        if (size != null) {
            return size
        }

        tempKey = createUserKey(null, null)
        size = map.get(tempKey)
        if (size != null) {
            return size
        }


        val key = createKey(mode, faceMode, width, height)
        size = map.get(key)
        if (size != null) {
            return size
        }

        size = createCalculateSize(service, mode, faceMode, width, height)
        if (size != null) {
            map.put(key, size)
        }
        return size
    }


    private fun createUserKey(mode: CameraMode?, faceMode: CameraFaceMode?): String {
        val cameraName = mode?.getModeName() ?: "-"
        val cameraId = faceMode?.getCameraId() ?: "-"
        return "${cameraName}_${cameraId}"
    }

    private fun createKey(
        mode: CameraMode,
        faceMode: CameraFaceMode,
        width: Int,
        height: Int
    ): String {
        val cameraName = mode.getModeName()
        val cameraId = faceMode.getCameraId() ?: ""
        return "${cameraName}_${cameraId}_${width}_${height}"
    }

    /**
     * 获取计算后的camera尺寸
     * */
    private fun createCalculateSize(
        service: CameraService,
        mode: CameraMode,
        faceMode: CameraFaceMode,
        width: Int,
        height: Int
    ): Size? {
        val supportSizes = if (mode == CameraMode.CAPTURE) {
            service.getSupportedCaptureSizes(faceMode)
        } else {
            service.getSupportedRecordSizes(faceMode)
        }
        return CameraUtils.calculatePreviewSize(supportSizes, width, height)
    }
}