package com.proxy.service.camera.info.loader.controller.func.record

import com.proxy.service.camera.base.callback.loader.VideoRecordCallback
import com.proxy.service.camera.info.utils.FileUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.media.CsFileMediaUtils
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.config.MimeType
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.File

/**
 * @author: cangHX
 * @data: 2026/3/23 18:35
 * @desc:
 */
class RecordControllerImpl : AbstractRecordController() {

    override fun setSurfaceSize(width: Int, height: Int) {
        if (recordBean != null) {
            CsLogger.tag(getTag()).w("Can not change size while recording.")
            return
        }
        super.setSurfaceSize(width, height)
    }


    override fun startVideoRecording(callback: VideoRecordCallback?) {
        CsLogger.tag(getTag()).i("startVideoRecording.")

        if (recordBean != null) {
            CsLogger.tag(getTag()).w("Already recording.")
            return
        }

        recordBean = RecordBean(false, FileUtils.getVideoRecordFile(), callback)
        requestVideoRecord()
    }

    override fun startVideoRecordingToLocal(filePath: String, callback: VideoRecordCallback?) {
        CsLogger.tag(getTag()).i("startVideoRecordingToLocal. filePath=$filePath")

        if (recordBean != null) {
            CsLogger.tag(getTag()).w("Already recording.")
            return
        }

        recordBean = RecordBean(false, File(filePath), callback)

        if (!CsFileUtils.createFile(recordBean?.localFile)) {
            callFailed(recordBean)
            return
        }

        requestVideoRecord()
    }

    override fun startVideoRecordingToAlbum(callback: VideoRecordCallback?) {
        CsLogger.tag(getTag()).i("startVideoRecordingToAlbum.")

        if (recordBean != null) {
            CsLogger.tag(getTag()).w("Already recording.")
            return
        }

        recordBean = RecordBean(true, FileUtils.getVideoRecordFile(), callback)
        requestVideoRecord()
    }


    override fun finishVideoRecording() {
        CsLogger.tag(getTag()).i("finishVideoRecording.")
        val recorder = mediaRecorder
        if (recorder == null) {
            CsLogger.tag(getTag()).e("VideoRecord reader is null. do startPreview first")
            return
        }

        val bean = recordBean
        if (bean == null) {
            CsLogger.tag(getTag()).w("VideoRecord is not start.")
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
        if (recordBean != null) {
            finishVideoRecording()
        }
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
            callback.resumePreview {
                if (!it) {
                    callFailed(recordBean)
                    return@resumePreview
                }

                try {
                    recorder.start()
                    CsLogger.tag(getTag()).i("startVideoRecording success.")
                } catch (throwable: Throwable) {
                    CsLogger.tag(getTag()).e(throwable, "startVideoRecording failed.")
                    callFailed(recordBean)
                }
            }
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
}