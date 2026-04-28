package com.proxy.service.camera.info.loader.controller.func.record

import android.Manifest
import android.media.MediaRecorder
import android.os.Build
import android.view.Surface
import com.proxy.service.camera.base.callback.loader.VideoRecordCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.base.mode.loader.AudioEncoderMode
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.loader.SensorOrientationMode
import com.proxy.service.camera.base.mode.loader.VideoEncoderMode
import com.proxy.service.camera.base.mode.loader.VideoRecordState
import com.proxy.service.camera.info.loader.controller.func.base.BaseSurfaceController
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.service.media.CsMediaCamera
import com.proxy.service.core.service.permission.CsPermission
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
        val callback: VideoRecordCallback?,
        var startTime: Long
    )

    companion object {
        private const val TAG = "${CameraConstants.TAG}RecordController"
    }


    private var videoEncoderMode: VideoEncoderMode = CameraConstants.DEFAULT_VIDEO_ENCODER_MODE
    private var videoFrameRate: Int = CameraConstants.DEFAULT_VIDEO_FRAME_RATE
    private var videoEncodingBitRate: Int = CameraConstants.DEFAULT_VIDEO_ENCODING_BIT_RATE

    private var audioEnabled: Boolean = CameraConstants.DEFAULT_AUDIO_ENABLED
    private var audioEncoderMode: AudioEncoderMode = CameraConstants.DEFAULT_AUDIO_ENCODER_MODE
    private var audioSamplingRate: Int = CameraConstants.DEFAULT_AUDIO_SAMPLING_RATE
    private var audioEncodingBitRate: Int = CameraConstants.DEFAULT_AUDIO_ENCODING_BIT_RATE
    private var audioChannels: Int = CameraConstants.DEFAULT_AUDIO_CHANNELS


    @Volatile
    protected var recordSurface: Surface? = null

    @Volatile
    protected var mediaRecorder: MediaRecorder? = null

    @Volatile
    protected var recordBean: RecordBean? = null


    override fun getTag(): String {
        return TAG
    }


    override fun setVideoEncoder(mode: VideoEncoderMode) {
        CsLogger.tag(TAG).i("setVideoEncoder. mode=$mode")
        this.videoEncoderMode = mode
    }

    override fun setVideoFrameRate(rate: Int) {
        CsLogger.tag(TAG).i("setVideoFrameRate. rate=$rate")
        this.videoFrameRate = rate
    }

    override fun setVideoEncodingBitRate(bitRate: Int) {
        CsLogger.tag(TAG).i("setVideoEncodingBitRate. bitRate=$bitRate")
        this.videoEncodingBitRate = bitRate
    }


    override fun setAudioEnabled(enabled: Boolean) {
        CsLogger.tag(TAG).i("setAudioEnabled. enabled=$enabled")
        this.audioEnabled = enabled
    }

    override fun setAudioEncoder(mode: AudioEncoderMode) {
        CsLogger.tag(TAG).i("setAudioEncoder. mode=$mode")
        this.audioEncoderMode = mode
    }

    override fun setAudioSamplingRate(samplingRate: Int) {
        CsLogger.tag(TAG).i("setAudioSamplingRate. samplingRate=$samplingRate")
        this.audioSamplingRate = samplingRate
    }

    override fun setAudioEncodingBitRate(bitRate: Int) {
        CsLogger.tag(TAG).i("setAudioEncodingBitRate. bitRate=$bitRate")
        this.audioEncodingBitRate = bitRate
    }

    override fun setAudioChannels(channels: Int) {
        CsLogger.tag(TAG).i("setAudioChannels. channels=$channels")
        this.audioChannels = channels
    }


    override fun getSurface(): Surface? {
        CsLogger.tag(TAG).i("getSurface. recordSurface=$recordSurface")
        createMediaRecorder()
        return recordSurface
    }

    override fun resetSurface() {
        CsLogger.tag(TAG).i("resetSurface.")
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
        CsLogger.tag(TAG).i("createSurface. recordBean=$recordBean")
        val bean = recordBean ?: return null
        var isAudioEnabled = audioEnabled
        if (audioEnabled && !CsPermission.isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
            CsLogger.tag(TAG)
                .e("The recording permission is lacking, so the sound cannot be recorded.")
            isAudioEnabled = false
        }
        try {
            val recorder = createMediaRecorder() ?: return null
            recorder.reset()
            if (isAudioEnabled) {
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            }
            recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setVideoSize(surfaceWidth, surfaceHeight)

            recorder.setVideoEncoder(videoEncoderMode.encoder)
            recorder.setVideoFrameRate(videoFrameRate)
            recorder.setVideoEncodingBitRate(videoEncodingBitRate)

            if (isAudioEnabled) {
                recorder.setAudioEncoder(audioEncoderMode.encoder)
                recorder.setAudioSamplingRate(audioSamplingRate)
                recorder.setAudioEncodingBitRate(audioEncodingBitRate)
                recorder.setAudioChannels(audioChannels)
            }


            if (surfaceOrientationDegrees != null) {
                recorder.setOrientationHint(surfaceOrientationDegrees!!)
            } else {
                recorder.setOrientationHint(calculateRotation())
            }
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

    override fun abort() {
        super.abort()
        val state = getVideoRecordState()
        if (state == VideoRecordState.STATE_STARTING || state == VideoRecordState.STATE_RECORDING) {
            finishVideoRecording()
        }
    }

    override fun destroy() {
        CsLogger.tag(TAG).i("destroy.")
        try {
            mediaRecorder?.stop()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).w(throwable)
        }
        try {
            mediaRecorder?.release()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).w(throwable)
        }

        mediaRecorder = null
        recordSurface = null
        recordBean = null
    }


    private fun calculateRotation(): Int {
        val cameraFaceMode = funParamsController?.getCameraFaceModeFromFunController()
        val orientation = if (cameraFaceMode == null) {
            SensorOrientationMode.ORIENTATION_0
        } else {
            CsMediaCamera.getSensorOrientation(cameraFaceMode)
                ?: SensorOrientationMode.ORIENTATION_0
        }
        val rotationDegrees = CsScreenUtils.getScreenRotation().degree * 90
        val sign = if (cameraFaceMode == CameraFaceMode.FaceFront) 1 else -1
        val rotation = (orientation.degree + rotationDegrees * sign + 360) % 360

        CsLogger.tag(TAG).i("calculateRotation. rotation=$rotation")
        return rotation
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