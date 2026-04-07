package com.proxy.service.camera.info.view.impl

import com.proxy.service.camera.base.callback.loader.VideoRecordCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
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

    override fun setVideoRecordSize(width: Int, height: Int) {
        CsLogger.tag(TAG).i("setVideoRecordSize. width. width=$width, height=$height")
    }

    override fun startVideoRecording(isSavePhotoAlbum: Boolean, callback: VideoRecordCallback?) {
        CsLogger.tag(TAG).i("startVideoRecording. ")
    }

    override fun finishVideoRecording() {
        CsLogger.tag(TAG).i("finishVideoRecording. ")
    }
}