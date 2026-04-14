package com.proxy.service.camera.info.page.cache

import android.util.Size
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.sp.CsSpManager
import com.proxy.service.core.service.media.CsMediaCamera

/**
 * @author: cangHX
 * @data: 2026/4/12 15:40
 * @desc:
 */
object CameraSettingCache {

    private const val TAG = "${CameraConstants.TAG}CameraSetting"

    private const val SEPARATOR = "*"
    private const val SP_NAME = "CsCoreCamera"

    private const val KEY_PICTURE_CAPTURE = "key_picture_capture_"
    private const val KEY_VIDEO_RECORD = "key_video_record_"


    fun getPreviewSize(mode: CameraFaceMode): Size{
        return createDefaultPreviewSize(mode)
    }


    fun getPictureCaptureSize(mode: CameraFaceMode): Size {
        try {
            val sizeString = CsSpManager.name(SP_NAME)
                .getString(createKey(KEY_PICTURE_CAPTURE, mode), null)
            if (sizeString != null) {
                val sizeSplit = sizeString.split(SEPARATOR)
                return Size(sizeSplit[0].toInt(), sizeSplit[1].toInt())
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return createDefaultPictureCaptureSize(mode)
    }

    fun savaPictureCaptureSize(mode: CameraFaceMode, size: Size) {
        CsSpManager.name(SP_NAME)
            .put(createKey(KEY_PICTURE_CAPTURE, mode), "${size.width}${SEPARATOR}${size.height}")
    }


    fun getVideoRecordSize(mode: CameraFaceMode): Size{
        try {
            val sizeString = CsSpManager.name(SP_NAME)
                .getString(createKey(KEY_VIDEO_RECORD, mode), null)
            if (sizeString != null) {
                val sizeSplit = sizeString.split(SEPARATOR)
                return Size(sizeSplit[0].toInt(), sizeSplit[1].toInt())
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return createDefaultVideoRecordSize(mode)
    }

    fun savaVideoRecordSize(mode: CameraFaceMode, size: Size) {
        CsSpManager.name(SP_NAME)
            .put(createKey(KEY_VIDEO_RECORD, mode), "${size.width}${SEPARATOR}${size.height}")
    }


    private fun createDefaultPreviewSize(mode: CameraFaceMode): Size {
        val sizes = CsMediaCamera.getSupportedCaptureSizes(mode)
        if (sizes.isNullOrEmpty()) {
            return Size(2560, 1080)
        }
        val size = sizes[0]
        savaPictureCaptureSize(mode, size)
        return size
    }

    private fun createDefaultPictureCaptureSize(mode: CameraFaceMode): Size {
        val sizes = CsMediaCamera.getSupportedCaptureSizes(mode)
        if (sizes.isNullOrEmpty()) {
            return Size(2560, 1080)
        }
        val size = sizes[0]
        savaPictureCaptureSize(mode, size)
        return size
    }

    private fun createDefaultVideoRecordSize(mode: CameraFaceMode): Size {
        val sizes = CsMediaCamera.getSupportedRecordSizes(mode)
        if (sizes.isNullOrEmpty()) {
            return Size(2560, 1080)
        }
        val size = sizes[0]
        savaVideoRecordSize(mode, size)
        return size
    }

    private fun createKey(prefix: String, mode: CameraFaceMode): String {
        return "$prefix${mode.getCameraId()}"
    }
}