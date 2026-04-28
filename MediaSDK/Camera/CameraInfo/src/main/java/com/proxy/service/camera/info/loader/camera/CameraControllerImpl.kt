package com.proxy.service.camera.info.loader.camera

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.camera.ICameraController
import com.proxy.service.camera.base.mode.loader.CameraMeteringMode
import com.proxy.service.camera.info.loader.converter.CameraMeteringConverter
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/3/19 20:00
 * @desc:
 */
class CameraControllerImpl : AbstractCameraFunController(), ICameraController {

    private var lastCaptureRequestBuilder: CaptureRequest.Builder? = null

    @Volatile
    private var cameraMeteringMode: CameraMeteringMode = CameraConstants.DEFAULT_CAMERA_METERING_MODE

    private var lastNeedsAfTrigger: Boolean = false

    override fun getCameraMeteringModeFromFunController(): CameraMeteringMode {
        return cameraMeteringMode
    }

    override fun setCameraMeteringMode(mode: CameraMeteringMode) {
        if (isReleased.get()) {
            CsLogger.tag(tag).w("The camera has been released.")
            return
        }
        CsLogger.tag(tag).i("setCameraMeteringMode. mode=$mode")

        cameraMeteringMode = mode
        handler.post {
            updateCameraParams()
        }
    }

    override fun parsePreviewRequest(builder: CaptureRequest.Builder) {
        super.parsePreviewRequest(builder)
        lastCaptureRequestBuilder = builder

        val faceMode = cameraFaceMode ?: return
        CameraMeteringConverter.applyMeteringMode(
            faceMode,
            cameraMeteringMode,
            cameraFunMode,
            builder
        )
    }

    override fun onClear() {
        super.onClear()
        lastNeedsAfTrigger = false
    }

    private fun updateCameraParams() {
        try {
            val meteringMode = cameraMeteringMode
            val faceMode = cameraFaceMode ?: return
            val builder = lastCaptureRequestBuilder ?: return
            val session = captureSessionManager.getCameraCaptureSession() ?: return
            val device = cameraDeviceManager.getCameraDevice() ?: return
            val needsAfTrigger = CameraMeteringConverter.needsAfTrigger(faceMode, meteringMode)

            if (needsAfTrigger || lastNeedsAfTrigger) {
                triggerAfCancel(device, session)
            }

            CameraMeteringConverter.applyMeteringMode(faceMode, meteringMode, cameraFunMode, builder)
            builder.set(
                CaptureRequest.CONTROL_AF_TRIGGER,
                CameraMetadata.CONTROL_AF_TRIGGER_IDLE
            )
            session.setRepeatingRequest(builder.build(), lastCaptureCallback, handler)

            if (needsAfTrigger) {
                val triggerBuilder = device.createCaptureRequest(lastTemplateType)
                lastRequestSurfaces.forEach { triggerBuilder.addTarget(it) }
                CameraMeteringConverter.applyMeteringMode(faceMode, meteringMode, cameraFunMode, triggerBuilder)
                triggerBuilder.set(
                    CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START
                )
                session.capture(triggerBuilder.build(), null, handler)
            }
            lastNeedsAfTrigger = needsAfTrigger
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    private fun triggerAfCancel(
        device: CameraDevice,
        session: CameraCaptureSession
    ) {
        val cancelBuilder = device.createCaptureRequest(lastTemplateType)
        lastRequestSurfaces.forEach { cancelBuilder.addTarget(it) }
        cancelBuilder.set(
            CaptureRequest.CONTROL_AF_TRIGGER,
            CameraMetadata.CONTROL_AF_TRIGGER_CANCEL
        )
        session.capture(cancelBuilder.build(), null, handler)
    }
}