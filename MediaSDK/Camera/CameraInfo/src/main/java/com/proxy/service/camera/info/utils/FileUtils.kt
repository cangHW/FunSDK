package com.proxy.service.camera.info.utils

import com.proxy.service.core.framework.app.context.CsContextManager
import java.io.File

/**
 * @author: cangHX
 * @data: 2026/3/25 10:06
 * @desc:
 */
object FileUtils {

    private fun getRootDir(): File? {
        return CsContextManager.getApplication().getExternalFilesDir("camera")
    }

    fun getPictureCaptureFile(): File {
        return File(getRootDir(), createPictureCaptureFileName())
    }

    fun createPictureCaptureFileName(): String {
        return "photo_${System.currentTimeMillis()}.jpg"
    }

    fun getVideoRecordFile(): File {
        return File(getRootDir(), createVideoRecordFileName())
    }

    fun createVideoRecordFileName(): String {
        return "video_${System.currentTimeMillis()}.mp4"
    }
}