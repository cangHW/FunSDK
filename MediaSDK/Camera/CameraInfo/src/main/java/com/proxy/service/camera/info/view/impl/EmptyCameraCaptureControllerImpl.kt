package com.proxy.service.camera.info.view.impl

import com.proxy.service.camera.base.callback.loader.PictureCaptureByteCallback
import com.proxy.service.camera.base.callback.loader.PictureCaptureCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/3/30 10:36
 * @desc:
 */
class EmptyCameraCaptureControllerImpl: ICameraCaptureController {

    companion object{
        private const val TAG = "${CameraConstants.TAG}EmptyCapture"
    }

    override fun setOrientationHint(degrees: Int) {
        CsLogger.tag(TAG).i("setOrientationHint. degrees=$degrees")
    }

    override fun setSurfaceSize(width: Int, height: Int) {
        CsLogger.tag(TAG).i("setSurfaceSize. width=$width, height=$height")
    }

    override fun startPictureCapture(callback: PictureCaptureByteCallback?) {
        CsLogger.tag(TAG).i("startPictureCapture.")
    }

    override fun startPictureCaptureToLocal(callback: PictureCaptureCallback?) {
        CsLogger.tag(TAG).i("startPictureCaptureToLocal.")
    }

    override fun startPictureCaptureToLocal(filePath: String, callback: PictureCaptureCallback?) {
        CsLogger.tag(TAG).i("startPictureCaptureToLocal. filePath=$filePath")
    }

    override fun startPictureCaptureToAlbum(callback: PictureCaptureCallback?) {
        CsLogger.tag(TAG).i("startPictureCaptureToAlbum.")
    }
}