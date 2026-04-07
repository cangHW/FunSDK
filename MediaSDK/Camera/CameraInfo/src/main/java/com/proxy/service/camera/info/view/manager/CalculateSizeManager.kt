package com.proxy.service.camera.info.view.manager

import android.util.Size
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraFunMode
import com.proxy.service.camera.info.utils.CameraUtils
import com.proxy.service.core.service.media.CsMediaCamera

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
    fun setUserPreviewSize(funMode: CameraFunMode?, faceMode: CameraFaceMode?, size: Size) {
        calculatePreviewSizeMap[createUserKey(funMode, faceMode)] = size
    }

    /**
     * 设置来自用户的产物尺寸
     * */
    fun setUserOutSize(funMode: CameraFunMode?, faceMode: CameraFaceMode?, size: Size) {
        calculateOutSizeMap[createUserKey(funMode, faceMode)] = size
    }


    /**
     * 获取预览尺寸
     * */
    fun getPreviewSize(
        funMode: CameraFunMode?,
        faceMode: CameraFaceMode,
        width: Int,
        height: Int
    ): Size? {
        return getCalculateSize(calculatePreviewSizeMap, funMode, faceMode, width, height)
    }

    /**
     * 获取产物尺寸
     * */
    fun getOutSize(
        funMode: CameraFunMode,
        faceMode: CameraFaceMode,
        width: Int,
        height: Int
    ): Size? {
        return getCalculateSize(calculateOutSizeMap, funMode, faceMode, width, height)
    }


    private fun getCalculateSize(
        map: HashMap<String, Size>,
        funMode: CameraFunMode?,
        faceMode: CameraFaceMode,
        width: Int,
        height: Int
    ): Size? {
        var tempKey = createUserKey(funMode, faceMode)
        var size = map[tempKey]
        if (size != null) {
            return size
        }

        tempKey = createUserKey(null, faceMode)
        size = map[tempKey]
        if (size != null) {
            return size
        }

        tempKey = createUserKey(funMode, null)
        size = map[tempKey]
        if (size != null) {
            return size
        }

        tempKey = createUserKey(null, null)
        size = map[tempKey]
        if (size != null) {
            return size
        }


        val key = createKey(funMode, faceMode, width, height)
        size = map[key]
        if (size != null) {
            return size
        }

        size = createCalculateSize(funMode, faceMode, width, height)
        if (size != null) {
            map[key] = size
        }
        return size
    }


    private fun createUserKey(funMode: CameraFunMode?, faceMode: CameraFaceMode?): String {
        val cameraName = funMode?.getModeName() ?: "-"
        val cameraId = faceMode?.getCameraId() ?: "-"
        return "${cameraName}_${cameraId}"
    }

    private fun createKey(
        funMode: CameraFunMode?,
        faceMode: CameraFaceMode,
        width: Int,
        height: Int
    ): String {
        val cameraName = funMode?.getModeName() ?: ""
        val cameraId = faceMode.getCameraId() ?: ""
        return "${cameraName}_${cameraId}_${width}_${height}"
    }

    /**
     * 获取计算后的camera尺寸
     * */
    private fun createCalculateSize(
        funMode: CameraFunMode?,
        faceMode: CameraFaceMode,
        width: Int,
        height: Int
    ): Size? {
        val supportSizes = when (funMode) {
            CameraFunMode.CAPTURE -> {
                CsMediaCamera.getSupportedCaptureSizes(faceMode) ?: ArrayList()
            }

            CameraFunMode.RECORD -> {
                CsMediaCamera.getSupportedRecordSizes(faceMode) ?: ArrayList()
            }

            else -> {
                CsMediaCamera.getSupportedPreviewSizes(faceMode) ?: ArrayList()
            }
        }
        return CameraUtils.calculateSize(supportSizes, width, height)
    }
}