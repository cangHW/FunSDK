package com.proxy.service.camera.info.loader.controller.func.record

import android.hardware.camera2.CameraDevice
import android.media.MediaRecorder
import com.proxy.service.camera.base.callback.loader.VideoRecordCallback
import com.proxy.service.camera.base.mode.loader.VideoRecordState
import com.proxy.service.camera.info.utils.FileUtils
import com.proxy.service.camera.info.utils.ThreadUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.media.CsFileMediaUtils
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.config.MimeType
import java.io.File

/**
 * @author: cangHX
 * @data: 2026/3/23 18:35
 * @desc:
 */
class RecordControllerImpl : AbstractRecordController() {

    companion object {
        private const val MIN_RECORD_TIME = 1 * 1000L
    }

    private val stateLock = Any()

    private var recordState: VideoRecordState = VideoRecordState.STATE_IDLE
    private var pendingStop: Boolean = false

    override fun setSurfaceSize(width: Int, height: Int) {
        synchronized(stateLock) {
            if (recordState != VideoRecordState.STATE_IDLE) {
                CsLogger.tag(getTag()).w("Can not change size while recording.")
                return
            }
        }
        super.setSurfaceSize(width, height)
    }


    override fun startVideoRecording(callback: VideoRecordCallback?) {
        CsLogger.tag(getTag()).i("startVideoRecording.")

        synchronized(stateLock) {
            if (recordState != VideoRecordState.STATE_IDLE) {
                CsLogger.tag(getTag()).w("Already recording.")
                ThreadUtils.postMain {
                    callback?.onVideoRecordFailed()
                }
                return
            }
            recordBean = RecordBean(false, FileUtils.getVideoRecordFile(), callback, 0)
            updateState(recordBean, VideoRecordState.STATE_STARTING)
        }

        requestVideoRecord()
    }

    override fun startVideoRecordingToLocal(filePath: String, callback: VideoRecordCallback?) {
        CsLogger.tag(getTag()).i("startVideoRecordingToLocal. filePath=$filePath")

        synchronized(stateLock) {
            if (!CsFileUtils.createFile(filePath)) {
                CsLogger.tag(getTag()).w("filePath can not be use.")
                ThreadUtils.postMain {
                    callback?.onVideoRecordFailed()
                }
                return
            }

            if (recordState != VideoRecordState.STATE_IDLE) {
                CsLogger.tag(getTag()).w("Already recording.")
                ThreadUtils.postMain {
                    callback?.onVideoRecordFailed()
                }
                return
            }

            recordBean = RecordBean(false, File(filePath), callback, 0)
            updateState(recordBean, VideoRecordState.STATE_STARTING)
        }

        requestVideoRecord()
    }

    override fun startVideoRecordingToAlbum(callback: VideoRecordCallback?) {
        CsLogger.tag(getTag()).i("startVideoRecordingToAlbum.")

        synchronized(stateLock) {
            if (recordState != VideoRecordState.STATE_IDLE) {
                CsLogger.tag(getTag()).w("Already recording.")
                ThreadUtils.postMain {
                    callback?.onVideoRecordFailed()
                }
                return
            }

            recordBean = RecordBean(true, FileUtils.getVideoRecordFile(), callback, 0)
            updateState(recordBean, VideoRecordState.STATE_STARTING)
        }

        requestVideoRecord()
    }


    override fun finishVideoRecording() {
        CsLogger.tag(getTag()).i("finishVideoRecording.")

        val recorder = mediaRecorder
        val bean = recordBean

        if (recorder == null) {
            CsLogger.tag(getTag()).e("VideoRecord reader is null. do startPreview first")
            return
        }

        if (bean == null) {
            CsLogger.tag(getTag()).w("VideoRecord is not start.")
            return
        }

        synchronized(stateLock) {
            if (recordState == VideoRecordState.STATE_IDLE) {
                CsLogger.tag(getTag()).w("VideoRecord is not start.")
                return
            }

            if (recordState == VideoRecordState.STATE_STARTING) {
                pendingStop = true
                CsLogger.tag(getTag()).i("VideoRecord is starting, mark pending stop.")
                return
            }

            if (recordState == VideoRecordState.STATE_STOPPING) {
                CsLogger.tag(getTag()).w("VideoRecord is stopping.")
                return
            }

            updateState(recordBean, VideoRecordState.STATE_STOPPING)
        }


        if (bean.startTime == 0L || (System.currentTimeMillis() - bean.startTime) <= MIN_RECORD_TIME) {
            callCancel(bean)
            return
        }

        try {
            recorder.stop()
            CsLogger.tag(getTag()).i("finishVideoRecording success.")

            if (bean.isSavePhotoAlbum) {
                saveVideoToAlbum(bean.localFile)
            } else {
                callSuccess(bean, bean.localFile.absolutePath)
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(getTag()).e(throwable, "finishVideoRecording failed.")
            callFailed(bean)
        }
    }


    override fun destroy() {
        cancelInternalByLifecycle()
        super.destroy()
    }


    private fun requestVideoRecord() {
        val recorder = mediaRecorder
        if (recorder == null) {
            CsLogger.tag(getTag()).e("VideoRecord reader is null. do startPreview first")
            callFailed(recordBean)
            return
        }

        val surface = createSurface()
        if (surface == null) {
            CsLogger.tag(getTag()).e("VideoRecord create surface error.")
            callFailed(recordBean)
            return
        }
        this.recordSurface = surface

        val callback = funSurfaceChangedCallback
        if (callback == null) {
            CsLogger.tag(getTag()).e("unknown error.")
            callFailed(recordBean)
            return
        }

        try {
            callback.refreshPreview(
                templateType = CameraDevice.TEMPLATE_RECORD,
                tempSurfaces = listOf(surface),
                success = {
                    try {
                        synchronized(stateLock) {
                            if (recordBean == null) {
                                CsLogger.tag(getTag()).w("RecordBean has unknown error.")
                                updateState(recordBean, VideoRecordState.STATE_IDLE)
                                return@refreshPreview
                            }

                            if (pendingStop) {
                                CsLogger.tag(getTag()).w("RecordBean is cancel.")
                                callCancel(recordBean)
                                return@refreshPreview
                            }

                            recorder.start()
                            recordBean?.startTime = System.currentTimeMillis()

                            updateState(recordBean, VideoRecordState.STATE_RECORDING)
                        }
                        CsLogger.tag(getTag()).i("startVideoRecording success.")
                    } catch (throwable: Throwable) {
                        CsLogger.tag(getTag()).e(throwable, "startVideoRecording failed.")
                        callFailed(recordBean)
                    }
                },
                failed = {
                    callFailed(recordBean)
                }
            )
        } catch (throwable: Throwable) {
            CsLogger.tag(getTag()).e(throwable, "startVideoRecording failed.")
            callFailed(recordBean)
        }
    }

    private fun saveVideoToAlbum(file: File) {
        CsFileMediaUtils.getVideoManager()
            .setSourceFile(file)
            .setMimeType(MimeType.VIDEO_MP4)
            .setDisplayName(file.nameWithoutExtension)
            .insert(object : InsertCallback {
                override fun onSuccess(path: String) {
                    CsLogger.tag(getTag()).i("saveVideoToAlbum success. path=$path")
                    CsFileUtils.delete(file)
                    callSuccess(recordBean, path)
                }

                override fun onFailed() {
                    CsLogger.tag(getTag()).e("saveVideoToAlbum failed.")
                    callFailed(recordBean)
                }
            })
    }


    private fun updateState(recordBean: RecordBean?, state: VideoRecordState) {
        when (state) {
            VideoRecordState.STATE_IDLE -> {
                this.recordBean = null
                pendingStop = false
            }

            VideoRecordState.STATE_STARTING -> {
                pendingStop = false
            }

            VideoRecordState.STATE_RECORDING -> {}
            VideoRecordState.STATE_STOPPING -> {}
        }

        recordState = state

        ThreadUtils.postMain {
            try {
                recordBean?.callback?.onVideoRecordStateChanged(state)
            } catch (throwable: Throwable) {
                CsLogger.tag(getTag()).e(throwable)
            }
        }
    }

    private fun callSuccess(recordBean: RecordBean?, filePath: String) {
        resetSurface()
        updateState(recordBean, VideoRecordState.STATE_IDLE)

        ThreadUtils.postMain {
            try {
                recordBean?.callback?.onVideoRecordSuccess(filePath)
            } catch (throwable: Throwable) {
                CsLogger.tag(getTag()).e(throwable)
            }
        }
    }

    private fun callFailed(recordBean: RecordBean?) {
        resetSurface()
        updateState(recordBean, VideoRecordState.STATE_IDLE)

        ThreadUtils.postMain {
            try {
                recordBean?.callback?.onVideoRecordFailed()
            } catch (throwable: Throwable) {
                CsLogger.tag(getTag()).e(throwable)
            } finally {
                CsFileUtils.delete(recordBean?.localFile)
            }
        }
    }

    private fun callCancel(recordBean: RecordBean?) {
        resetSurface()
        updateState(recordBean, VideoRecordState.STATE_IDLE)

        ThreadUtils.postMain {
            try {
                recordBean?.callback?.onVideoRecordCancel()
            } catch (throwable: Throwable) {
                CsLogger.tag(getTag()).e(throwable)
            } finally {
                CsFileUtils.delete(recordBean?.localFile)
            }
        }
    }

    private fun cancelInternalByLifecycle() {
        val snapshot: DestroySnapshot = synchronized(stateLock) {
            if (recordState == VideoRecordState.STATE_IDLE) {
                return
            }
            val bean = recordBean
            val recorder = mediaRecorder
            val canStop = bean != null && bean.startTime > 0L && recorder != null
            recordState = VideoRecordState.STATE_STOPPING
            pendingStop = false
            DestroySnapshot(
                recordBean = bean,
                recorder = recorder,
                shouldStopRecorder = canStop
            )
        }

        if (snapshot.shouldStopRecorder) {
            try {
                snapshot.recorder?.stop()
            } catch (throwable: Throwable) {
                CsLogger.tag(getTag()).w(throwable)
            }
        }

        val callbackBean = synchronized(stateLock) {
            val bean = snapshot.recordBean
            recordBean = null
            recordState = VideoRecordState.STATE_IDLE
            pendingStop = false
            bean
        }

        callbackBean?.let { bean ->
            CsFileUtils.delete(bean.localFile)
            ThreadUtils.postMain {
                try {
                    bean.callback?.onVideoRecordStateChanged(VideoRecordState.STATE_IDLE)
                    bean.callback?.onVideoRecordCancel()
                } catch (throwable: Throwable) {
                    CsLogger.tag(getTag()).w(throwable)
                }
            }
        }
    }

    private data class DestroySnapshot(
        val recordBean: RecordBean?,
        val recorder: MediaRecorder?,
        val shouldStopRecorder: Boolean
    )
}