package com.proxy.service.camera.info.loader.camera

import android.hardware.camera2.CaptureRequest
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.camera.ICameraController
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.CameraFunMode
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/3/19 20:00
 * @desc:
 */
class CameraControllerImpl : AbstractCameraFunController(), ICameraController {

    private var cameraAfMode: CameraAfMode = CameraConstants.DEFAULT_CAMERA_AF_MODE

    override fun setCameraAfMode(mode: CameraAfMode) {
        if (isReleased.get()) {
            CsLogger.tag(tag).w("The camera has been released.")
            return
        }
        CsLogger.tag(tag).i("setCameraAfMode. mode=$mode")

        cameraAfMode = mode
        resumePreview()
    }

    override fun parsePreviewRequest(builder: CaptureRequest.Builder) {
        super.parsePreviewRequest(builder)

        com.proxy.service.camera.info.loader.converter.CameraAfConverter.parse(
            cameraAfMode,
            funControllerToCameraMode(),
            builder
        )
    }

    private fun funControllerToCameraMode(): CameraFunMode? {
        return when (funController) {
            is ICameraCaptureController -> {
                CameraFunMode.CAPTURE
            }

            is ICameraRecordController -> {
                CameraFunMode.RECORD
            }

            else -> {
                null
            }
        }
    }
}