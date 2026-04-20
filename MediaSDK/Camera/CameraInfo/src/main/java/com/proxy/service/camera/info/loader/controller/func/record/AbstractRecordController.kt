package com.proxy.service.camera.info.loader.controller.func.record

import android.media.MediaRecorder
import android.os.Build
import android.view.Surface
import com.proxy.service.camera.base.callback.loader.VideoRecordCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.loader.SensorOrientationMode
import com.proxy.service.camera.info.loader.controller.func.base.BaseSurfaceController
import com.proxy.service.camera.info.utils.FileUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.service.media.CsMediaCamera
import java.io.File

/**
 * @author: cangHX
 * @data: 2026/4/20 14:12
 * @desc:
 */
abstract class AbstractRecordController : BaseSurfaceController(), ICameraRecordController {

    protected data class RecordBean(
        val isSavePhotoAlbum: Boolean,
        val localFile: File,
        val callback: VideoRecordCallback?
    )

    companion object {
        private const val TAG = "${CameraConstants.TAG}RecordController"
    }

    protected var recordSurface: Surface? = null
    protected var mediaRecorder: MediaRecorder? = null

    protected var recordBean: RecordBean? = null


    override fun getTag(): String {
        return TAG
    }


    override fun getSurface(): Surface? {
        createMediaRecorder()
        return recordSurface
    }

    override fun resetSurface() {
        if (recordSurface != null) {
            recordSurface = null
            funSurfaceChangedCallback?.onSurfaceChanged()
        }
        try {
            mediaRecorder?.reset()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).w(throwable)
        }
    }

    override fun createSurface(): Surface? {
        val bean = recordBean ?: return null

        try {
            val recorder = createMediaRecorder() ?: return null
            recorder.reset()
            recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            recorder.setVideoSize(width, height)
            recorder.setVideoFrameRate(30)
            recorder.setVideoEncodingBitRate(width * height * 5)
            recorder.setOrientationHint(calculateRotation())
            recorder.setOutputFile(bean.localFile.absolutePath)
            recorder.prepare()
            CsLogger.tag(TAG).i("prepareRecorder success.")
            return recorder.surface
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable, "prepareRecorder failed.")
            resetSurface()
        }
        return null
    }

    override fun destroy() {
        CsLogger.tag(TAG).i("destroy.")
        try {
            mediaRecorder?.release()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).w(throwable)
        } finally {
            mediaRecorder = null
            recordSurface = null
        }
    }


    private fun calculateRotation(): Int {
        val cameraFaceMode = funParamsController?.getCameraFaceMode()
        val orientation = if (cameraFaceMode == null) {
            SensorOrientationMode.ORIENTATION_0
        } else {
            CsMediaCamera.getSensorOrientation(cameraFaceMode)
                ?: SensorOrientationMode.ORIENTATION_0
        }
        val rotationDegrees = CsScreenUtils.getScreenRotation().degree * 90
        val sign = if (cameraFaceMode == CameraFaceMode.FaceFront) 1 else -1
        return (orientation.degree + rotationDegrees * sign + 360) % 360
    }

    private fun createMediaRecorder(): MediaRecorder? {
        if (mediaRecorder == null) {
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(CsContextManager.getApplication())
            } else {
                MediaRecorder()
            }
        }
        return mediaRecorder
    }
}