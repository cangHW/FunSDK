package com.proxy.service.camera.info.view.impl

import com.proxy.service.camera.base.callback.loader.VideoRecordCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.base.mode.loader.VideoEncoderMode
import com.proxy.service.camera.base.mode.loader.VideoRecordState
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/3/30 10:38
 * @desc:
 */
class EmptyCameraRecordControllerImpl: ICameraRecordController {

    companion object{
        private const val TAG = "${CameraConstants.TAG}EmptyRecord"
    }

    override fun setSurfaceSize(width: Int, height: Int) {
        CsLogger.tag(TAG).i("setSurfaceSize. width. width=$width, height=$height")
    }

    override fun setOrientationHint(degrees: Int) {
        CsLogger.tag(TAG).i("setOrientationHint. degrees=${degrees}")
    }

    override fun setVideoEncoder(mode: VideoEncoderMode) {
        CsLogger.tag(TAG).i("setVideoEncoder. mode=${mode}")
    }

    override fun setVideoFrameRate(rate: Int) {
        CsLogger.tag(TAG).i("setVideoFrameRate. rate=$rate")
    }

    override fun setVideoEncodingBitRate(bitRate: Int) {
        CsLogger.tag(TAG).i("setVideoEncodingBitRate. bitRate=$bitRate")
    }

    override fun getVideoRecordState(): VideoRecordState {
        CsLogger.tag(TAG).i("setSurfaceSize.")
        return VideoRecordState.STATE_IDLE
    }

    override fun startVideoRecording(callback: VideoRecordCallback?) {
        CsLogger.tag(TAG).i("startVideoRecording. callback=$callback")
    }

    override fun startVideoRecordingToLocal(filePath: String, callback: VideoRecordCallback?) {
        CsLogger.tag(TAG).i("startVideoRecordingToLocal. filePath=$filePath")
    }

    override fun startVideoRecordingToAlbum(callback: VideoRecordCallback?) {
        CsLogger.tag(TAG).i("startVideoRecordingToAlbum. callback=$callback")
    }

    override fun finishVideoRecording() {
        CsLogger.tag(TAG).i("finishVideoRecording. ")
    }
}