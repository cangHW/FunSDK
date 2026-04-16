package com.proxy.service.camera.info.view.impl

import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.view.CameraViewAfMode
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

    override fun setPreviewSize(width: Int, height: Int) {
        CsLogger.tag(TAG).i("setPreviewSize. width=$width, height=$height")
    }

    override fun setCameraViewAfMode(mode: CameraViewAfMode) {
        CsLogger.tag(TAG).i("setCameraViewAfMode. mode=$mode")
    }

    override fun openCamera(mode: CameraFaceMode) {
        CsLogger.tag(TAG).i("openCamera.")
    }

    override fun startPreview() {
        CsLogger.tag(TAG).i("startPreview.")
    }

    override fun pausePreview() {
        CsLogger.tag(TAG).i("pausePreview.")
    }

    override fun stopPreview() {
        CsLogger.tag(TAG).i("stopPreview.")
    }

    override fun resumePreview() {
        CsLogger.tag(TAG).i("resumePreview.")
    }

    override fun releaseCamera() {
        CsLogger.tag(TAG).i("releaseCamera.")
    }

    override fun chooseCaptureMode(): ICameraCaptureController {
        CsLogger.tag(TAG).i("chooseCaptureMode.")
        return EmptyCameraCaptureControllerImpl()
    }

    override fun chooseRecordMode(): ICameraRecordController {
        CsLogger.tag(TAG).i("chooseRecordMode.")
        return EmptyCameraRecordControllerImpl()
    }
}