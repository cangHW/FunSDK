package com.proxy.service.camera.info.view.impl

import com.proxy.service.camera.base.callback.TakePictureCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.base.mode.CameraViewAfMode
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/2/5 14:45
 * @desc:
 */
class EmptyCameraViewImpl: ICameraView {

    companion object{
        private const val TAG = "${CameraConstants.TAG}EmptyView"
    }

    override fun setPictureCaptureSize(width: Int, height: Int) {
        CsLogger.tag(TAG).i("setPictureCaptureSize. width=$width, height=$height")
    }

    override fun setPreviewSize(width: Int, height: Int) {
        CsLogger.tag(TAG).i("setPreviewSize. width=$width, height=$height")
    }

    override fun setCameraMode(mode: CameraMode) {
        CsLogger.tag(TAG).i("setCameraMode. mode=$mode")
    }

    override fun setCameraViewAfMode(mode: CameraViewAfMode) {
        CsLogger.tag(TAG).i("setCameraViewAfMode. mode=$mode")
    }

    override fun startPictureCapture(isSavePhotoAlbum: Boolean, callback: TakePictureCallback?) {
        CsLogger.tag(TAG).i("startPictureCapture. isSavePhotoAlbum=$isSavePhotoAlbum")
    }

    override fun openCamera(mode: CameraFaceMode) {
        CsLogger.tag(TAG).i("openCamera.")
    }

    override fun pausePreview() {
        CsLogger.tag(TAG).i("pausePreview.")
    }

    override fun resumePreview() {
        CsLogger.tag(TAG).i("resumePreview.")
    }

    override fun releaseCamera() {
        CsLogger.tag(TAG).i("releaseCamera.")
    }
}