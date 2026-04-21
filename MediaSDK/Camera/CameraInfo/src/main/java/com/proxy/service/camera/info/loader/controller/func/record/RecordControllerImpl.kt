package com.proxy.service.camera.info.loader.controller.func.record

import android.hardware.camera2.CameraDevice
import android.media.MediaRecorder
import com.proxy.service.camera.base.callback.loader.VideoRecordCallback
import com.proxy.service.camera.base.mode.loader.VideoRecordState
import com.proxy.service.camera.info.utils.FileUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.media.CsFileMediaUtils
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.config.MimeType
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.controller.ITaskDisposable
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import java.io.File
import java.util.concurrent.TimeUnit

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
    private var stopTask: ITaskDisposable? = null

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
                return
            }

            recordBean = RecordBean(false, FileUtils.getVideoRecordFile(), callback, 0)
            requestVideoRecord()
        }
    }

    override fun startVideoRecordingToLocal(filePath: String, callback: VideoRecordCallback?) {
        CsLogger.tag(getTag()).i("startVideoRecordingToLocal. filePath=$filePath")

        synchronized(stateLock) {
            if (recordState != VideoRecordState.STATE_IDLE) {
                CsLogger.tag(getTag()).w("Already recording.")
                return
            }

            recordBean = RecordBean(false, File(filePath), callback, 0)

            if (!CsFileUtils.createFile(recordBean?.localFile)) {
                callFailed(recordBean)
                return
            }

            requestVideoRecord()
        }
    }

    override fun startVideoRecordingToAlbum(callback: VideoRecordCallback?) {
        CsLogger.tag(getTag()).i("startVideoRecordingToAlbum.")

        synchronized(stateLock) {
            if (recordState != VideoRecordState.STATE_IDLE) {
                CsLogger.tag(getTag()).w("Already recording.")
                return
            }

            recordBean = RecordBean(true, FileUtils.getVideoRecordFile(), callback, 0)
            requestVideoRecord()
        }
    }


    override fun finishVideoRecording() {
        CsLogger.tag(getTag()).i("finishVideoRecording.")

        synchronized(stateLock) {
            val recorder = mediaRecorder
            if (recorder == null) {
                CsLogger.tag(getTag()).e("VideoRecord reader is null. do startPreview first")
                return
            }

            val bean = recordBean
            if (recordState == VideoRecordState.STATE_IDLE || bean == null) {
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

            recordState = VideoRecordState.STATE_STOPPING
            cancelStopTaskLocked()

            val elapsed = (System.currentTimeMillis() - bean.startTime).coerceAtLeast(0L)
            val delay = (MIN_RECORD_TIME - elapsed).coerceAtLeast(0L)
            if (delay <= 0L) {
                finishVideoRecord(bean, recorder)
                return
            }

            stopTask = CsTask.delay(delay, TimeUnit.MILLISECONDS)
                ?.mainThread()
                ?.doOnNext(object : IConsumer<Long> {
                    override fun accept(value: Long) {
                        synchronized(stateLock) {
                            finishVideoRecord(bean, recorder)
                        }
                    }
                })?.start()
        }
    }


    override fun destroy() {
        synchronized(stateLock) {
            if (recordState != VideoRecordState.STATE_IDLE) {
                finishVideoRecording()
            }
        }
        super.destroy()
    }


    private fun finishVideoRecord(bean: RecordBean, recorder: MediaRecorder) {
        if (recordState != VideoRecordState.STATE_STOPPING) {
            resetRecordRuntimeState()
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

    private fun requestVideoRecord() {
        recordState = VideoRecordState.STATE_STARTING
        pendingStop = false
        cancelStopTaskLocked()

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
                            val bean = recordBean
                            if (bean == null) {
                                CsLogger.tag(getTag()).w("RecordBean is null when start.")
                                resetRecordRuntimeState()
                                return@refreshPreview
                            }

                            if (pendingStop) {
                                CsLogger.tag(getTag()).w("RecordBean is cancel.")
                                callCancel(bean)
                                return@refreshPreview
                            }

                            pendingStop = false
                            recordState = VideoRecordState.STATE_RECORDING

                            recorder.start()
                            bean.startTime = System.currentTimeMillis()

                            CsLogger.tag(getTag()).i("startVideoRecording success.")
                        }
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


    private fun callSuccess(recordBean: RecordBean?, filePath: String) {
        resetSurface()

        this.recordBean = null
        resetRecordRuntimeState()

        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                try {
                    recordBean?.callback?.onVideoRecordSuccess(filePath)
                } catch (throwable: Throwable) {
                    CsLogger.tag(getTag()).e(throwable)
                }
                return ""
            }
        })?.start()
    }

    private fun callFailed(recordBean: RecordBean?) {
        resetSurface()

        this.recordBean = null
        resetRecordRuntimeState()

        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                try {
                    recordBean?.callback?.onVideoRecordFailed()
                } catch (throwable: Throwable) {
                    CsLogger.tag(getTag()).e(throwable)
                } finally {
                    CsFileUtils.delete(recordBean?.localFile)
                }
                return ""
            }
        })?.start()
    }

    private fun callCancel(recordBean: RecordBean?) {
        resetSurface()

        this.recordBean = null
        resetRecordRuntimeState()

        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                try {
                    recordBean?.callback?.onVideoRecordCancel()
                } catch (throwable: Throwable) {
                    CsLogger.tag(getTag()).e(throwable)
                } finally {
                    CsFileUtils.delete(recordBean?.localFile)
                }
                return ""
            }
        })?.start()
    }

    private fun resetRecordRuntimeState() {
        recordState = VideoRecordState.STATE_IDLE
        pendingStop = false
        cancelStopTaskLocked()
    }

    private fun cancelStopTaskLocked() {
        stopTask?.dispose()
        stopTask = null
    }
}