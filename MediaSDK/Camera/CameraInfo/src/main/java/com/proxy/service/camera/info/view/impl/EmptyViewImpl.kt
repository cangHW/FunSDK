package com.proxy.service.camera.info.view.impl

import com.proxy.service.camera.base.callback.TakePictureCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraViewAfMode
import com.proxy.service.camera.base.view.IView
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/2/5 14:45
 * @desc:
 */
class EmptyViewImpl: IView {

    companion object{
        private const val TAG = "${CameraConstants.TAG}EmptyView"
    }

    override fun setCameraViewAfMode(mode: CameraViewAfMode) {

    }

    override fun capturePhoto(isSavePhotoAlbum: Boolean, callback: TakePictureCallback?) {
        CsLogger.tag(TAG).i("capturePhoto. isSavePhotoAlbum=$isSavePhotoAlbum")
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