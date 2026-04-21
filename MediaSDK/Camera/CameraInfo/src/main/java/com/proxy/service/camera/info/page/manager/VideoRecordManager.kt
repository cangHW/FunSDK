package com.proxy.service.camera.info.page.manager

import android.text.TextUtils
import com.proxy.service.camera.base.callback.loader.VideoRecordCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.base.mode.loader.VideoRecordState
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.camera.info.page.params.VideoRecordParams
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.collections.type.Type

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

        fun create(params: MediaCameraParams): VideoRecordManager {
            return VideoRecordManager(params.videoRecordParams)
        }
    }

    private val statusMap = CsExcellentMap<ICameraRecordController, VideoRecordState>(Type.WEAK)

    fun startOrFinishRecord(cameraRecordController: ICameraRecordController?) {
        if (cameraRecordController == null) {
            return
        }

        val status = statusMap.get(cameraRecordController) ?: VideoRecordState.STATE_IDLE

        if (status == VideoRecordState.STATE_IDLE) {
            start(cameraRecordController)
        } else if (status == VideoRecordState.STATE_STARTING || status == VideoRecordState.STATE_RECORDING) {
            finish(cameraRecordController)
        }
    }


    private fun start(cameraRecordController: ICameraRecordController) {
        val callback = object : VideoRecordCallback {
            override fun onVideoRecordStateChanged(state: VideoRecordState) {
                super.onVideoRecordStateChanged(state)
                statusMap.putSync(cameraRecordController, state)
            }

            override fun onVideoRecordSuccess(filePath: String) {
                params.callback?.onVideoRecordSuccess(filePath)
            }

            override fun onVideoRecordFailed() {
                params.callback?.onVideoRecordFailed()
            }

            override fun onVideoRecordCancel() {
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
    }

    private fun finish(cameraRecordController: ICameraRecordController) {
        cameraRecordController.finishVideoRecording()
    }
}