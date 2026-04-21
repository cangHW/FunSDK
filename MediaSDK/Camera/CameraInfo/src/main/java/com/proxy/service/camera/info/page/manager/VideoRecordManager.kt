package com.proxy.service.camera.info.page.manager

import android.text.TextUtils
import com.proxy.service.camera.base.callback.loader.VideoRecordCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.camera.info.page.params.VideoRecordParams
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.collections.type.Type
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.controller.ITaskDisposable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2026/4/20 17:18
 * @desc:
 */
class VideoRecordManager private constructor(
    private val params: VideoRecordParams
) {

    companion object {
        private const val TAG = "${CameraConstants.TAG}Page_VideoRecordManager"

        private const val MAX_FINISH_TIME = 3 * 1000L

        private const val TYPE_IDLE = 0
        private const val TYPE_START = 1
        private const val TYPE_FINISH = 2

        fun create(params: MediaCameraParams): VideoRecordManager {
            return VideoRecordManager(params.videoRecordParams)
        }
    }

    private val statusMap = CsExcellentMap<ICameraRecordController, Int>(Type.WEAK)

    private var taskDisposable: ITaskDisposable? = null


    fun startOrFinishRecord(cameraRecordController: ICameraRecordController?) {
        if (cameraRecordController == null) {
            return
        }

        val status = statusMap.get(cameraRecordController) ?: TYPE_IDLE

        if (status == TYPE_IDLE) {
            start(cameraRecordController)
        } else if (status == TYPE_START) {
            finish(cameraRecordController)
        }
    }


    private fun start(cameraRecordController: ICameraRecordController) {
        val callback = object : VideoRecordCallback {
            override fun onVideoRecordSuccess(filePath: String) {
                statusMap.putSync(cameraRecordController, TYPE_IDLE)
                taskDisposable?.dispose()
                taskDisposable = null
                params.callback?.onVideoRecordSuccess(filePath)
            }

            override fun onVideoRecordFailed() {
                statusMap.putSync(cameraRecordController, TYPE_IDLE)
                taskDisposable?.dispose()
                taskDisposable = null
                params.callback?.onVideoRecordFailed()
            }

            override fun onVideoRecordCancel() {
                statusMap.putSync(cameraRecordController, TYPE_IDLE)
                taskDisposable?.dispose()
                taskDisposable = null
                params.callback?.onVideoRecordCancel()
            }
        }

        if (params.isSaveAlbum) {
            cameraRecordController.startVideoRecordingToAlbum(callback)
        } else if (TextUtils.isEmpty(params.filePath)) {
            cameraRecordController.startVideoRecording(callback)
        } else {
            cameraRecordController.startVideoRecordingToLocal(params.filePath ?: "", callback)
        }
        statusMap.putSync(cameraRecordController, TYPE_START)
    }

    private fun finish(cameraRecordController: ICameraRecordController) {
        cameraRecordController.finishVideoRecording()
        statusMap.putSync(cameraRecordController, TYPE_FINISH)

        taskDisposable = CsTask.delay(MAX_FINISH_TIME, TimeUnit.MILLISECONDS)
            ?.doOnNext(object : IConsumer<Long> {
                override fun accept(value: Long) {
                    statusMap.runInTransaction {
                        val status = statusMap.get(cameraRecordController)
                        if (status == TYPE_FINISH) {
                            statusMap.putSync(cameraRecordController, TYPE_IDLE)
                        }
                    }
                }
            })?.start()
    }
}