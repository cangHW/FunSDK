package com.proxy.service.camera.info.view.base

import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.camera.ICameraAction
import com.proxy.service.camera.base.loader.camera.ICameraController
import com.proxy.service.camera.base.loader.camera.ICameraFun
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.info.view.config.CameraViewConfig
import com.proxy.service.camera.info.view.impl.EmptyCameraCaptureControllerImpl
import com.proxy.service.camera.info.view.impl.EmptyCameraRecordControllerImpl
import com.proxy.service.core.service.media.CsMediaCamera

/**
 * @author: cangHX
 * @data: 2026/2/5 17:45
 * @desc:
 */
abstract class AbstractCameraView(
    private val config: CameraViewConfig,
) : ICameraAction, ICameraFun {

    protected val tag = "${CameraConstants.TAG}View"

    protected var cameraController: ICameraController? = null

    open fun init() {
        val loader = CsMediaCamera.createLoader()
        loader?.setCameraAfMode(config.cameraViewAfMode.toCameraAfMode())
        config.lifecycleOwner?.let {
            loader?.setLifecycleOwner(it)
        }

        val faceMode = config.cameraFaceMode
        cameraController = if (faceMode == null) {
            loader?.createCamera()
        } else {
            loader?.createAndOpenCamera(faceMode)
        }
    }

    override fun openCamera(mode: CameraFaceMode) {
        cameraController?.openCamera(mode)
    }

    override fun startPreview() {
        cameraController?.startPreview()
    }

    override fun resumePreview() {
        cameraController?.resumePreview()
    }

    override fun pausePreview() {
        cameraController?.pausePreview()
    }

    override fun stopPreview() {
        cameraController?.stopPreview()
    }

    override fun releaseCamera() {
        cameraController?.releaseCamera()
    }

    override fun chooseCaptureMode(): ICameraCaptureController {
        return cameraController?.chooseCaptureMode() ?: EmptyCameraCaptureControllerImpl()
    }

    override fun chooseRecordMode(): ICameraRecordController {
        return cameraController?.chooseRecordMode() ?: EmptyCameraRecordControllerImpl()
    }

}