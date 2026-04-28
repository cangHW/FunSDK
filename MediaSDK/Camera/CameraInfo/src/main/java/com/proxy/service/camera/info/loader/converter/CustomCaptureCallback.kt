package com.proxy.service.camera.info.loader.converter

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureFailure
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2026/4/28 14:30
 * @desc:
 */
class CustomCaptureCallback(
    private val success: (() -> Unit)?,
    private val failed: (() -> Unit)?
) : CameraCaptureSession.CaptureCallback() {

    companion object {
        private const val TAG = "${CameraConstants.TAG}CaptureCallback"
    }

    private val isCallbackDone = AtomicBoolean(false)

    override fun onCaptureStarted(
        session: CameraCaptureSession,
        request: CaptureRequest,
        timestamp: Long,
        frameNumber: Long
    ) {
        super.onCaptureStarted(session, request, timestamp, frameNumber)

        if (isCallbackDone.compareAndSet(false, true)) {
            success?.invoke()
        }
    }

    private var lastAfState: Int? = null

    override fun onCaptureCompleted(
        session: CameraCaptureSession,
        request: CaptureRequest,
        result: TotalCaptureResult
    ) {
        super.onCaptureCompleted(session, request, result)

        if (isCallbackDone.compareAndSet(false, true)) {
            success?.invoke()
        }

        val afState = result.get(CaptureResult.CONTROL_AF_STATE)
        if (afState != null && afState != lastAfState) {
            lastAfState = afState

            when (afState) {
                CameraMetadata.CONTROL_AF_STATE_INACTIVE -> {
                    CsLogger.tag(TAG).d("INACTIVE. The autofocus is not activated.")
                }

                CameraMetadata.CONTROL_AF_STATE_PASSIVE_SCAN -> {
                    CsLogger.tag(TAG).d("PASSIVE_SCAN. Autofocus is in progress")
                }

                CameraMetadata.CONTROL_AF_STATE_PASSIVE_FOCUSED -> {
                    CsLogger.tag(TAG).d("PASSIVE_FOCUSED. Autofocus successful.")
                }

                CameraMetadata.CONTROL_AF_STATE_PASSIVE_UNFOCUSED -> {
                    CsLogger.tag(TAG).e("PASSIVE_UNFOCUSED. Autofocus failure.")
                }

                CameraMetadata.CONTROL_AF_STATE_ACTIVE_SCAN -> {
                    CsLogger.tag(TAG).d("ACTIVE_SCAN. Active focusing in progress.")
                }

                CameraMetadata.CONTROL_AF_STATE_FOCUSED_LOCKED -> {
                    CsLogger.tag(TAG).d("FOCUSED_LOCKED. Active focusing successful.")
                }

                CameraMetadata.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED -> {
                    CsLogger.tag(TAG).e("NOT_FOCUSED_LOCKED. Active focusing failure.")
                }
            }
        }
    }

    override fun onCaptureFailed(
        session: CameraCaptureSession,
        request: CaptureRequest,
        failure: CaptureFailure
    ) {
        super.onCaptureFailed(session, request, failure)

        if (isCallbackDone.compareAndSet(false, true)) {
            failed?.invoke()
        }
    }

}