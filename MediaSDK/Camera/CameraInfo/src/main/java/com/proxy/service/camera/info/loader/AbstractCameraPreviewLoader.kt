package com.proxy.service.camera.info.loader

import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.MeteringRectangle
import android.view.Surface
import androidx.annotation.CallSuper
import com.proxy.service.camera.base.config.loader.LoaderConfig
import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.base.mode.af.FocusAreaInfo
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/2/7 14:17
 * @desc:
 */
abstract class AbstractCameraPreviewLoader(
    config: LoaderConfig
) : AbstractCameraLoader(config) {

    protected var cameraMode: CameraMode? = null
    private var cameraAfMode: CameraAfMode = config.getCameraAfMode()
    private var previewSurface: Surface? = null

    override fun findCaptureSessionSurfaces(list: ArrayList<Surface>) {
        previewSurface?.let {
            list.add(it)
        }
    }


    @CallSuper
    override fun setPicturePreview(surface: Surface, width: Int, height: Int) {
        CsLogger.tag(tag).i("setPreviewSurface. width=$width, height=$height")
        previewSurface = surface
        cameraMode = CameraMode.PICTURE

        handler.post {
            try {
                cameraCaptureSession?.stopRepeating()
                cameraCaptureSession?.abortCaptures()
            } catch (throwable: Throwable) {
                CsLogger.tag(tag).e(throwable)
            }

            createCaptureSession()
        }
    }

    @CallSuper
    override fun setVideoPreview(surface: Surface) {
        CsLogger.tag(tag).i("setVideoPreview.")
        cameraMode = CameraMode.VIDEO

        handler.post {
            try {
                cameraCaptureSession?.stopRepeating()
                cameraCaptureSession?.abortCaptures()
            } catch (throwable: Throwable) {
                CsLogger.tag(tag).e(throwable)
            }

            createCaptureSession()
        }
    }

    override fun _setCameraAfMode(mode: CameraAfMode) {
        super._setCameraAfMode(mode)

        cameraAfMode = mode
        resumePreview()
    }

    @CallSuper
    override fun _pausePreview() {
        super._pausePreview()

        try {
            cameraCaptureSession?.stopRepeating()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    @CallSuper
    override fun _resumePreview() {
        super._resumePreview()

        val device = cameraDevice
        if (device == null) {
            openCamera(cameraFaceMode)
            return
        }
        val surface = previewSurface ?: return

        try {
            val builder = device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            builder.addTarget(surface)
            builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

            if (cameraAfMode == CameraAfMode.AfAutoMode) {
                val afMode: Int? = when (cameraMode) {
                    CameraMode.PICTURE -> {
                        CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                    }

                    CameraMode.VIDEO -> {
                        CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_VIDEO
                    }

                    else -> {
                        null
                    }
                }
                afMode?.let {
                    builder.set(CaptureRequest.CONTROL_AF_MODE, afMode)
                }
            } else if (cameraAfMode is CameraAfMode.AfFixedMode) {
                val list = ArrayList<MeteringRectangle>()
                (cameraAfMode as? CameraAfMode.AfFixedMode?)?.list?.forEach {
                    val area = MeteringRectangle(
                        it.x.coerceAtLeast(0),
                        it.y.coerceAtLeast(0),
                        it.width,
                        it.height,
                        it.weight
                    )
                    list.add(area)
                }

                if (list.isNotEmpty()) {
                    builder.set(CaptureRequest.CONTROL_AF_REGIONS, list.toTypedArray())
                    builder.set(CaptureRequest.CONTROL_AF_MODE, CameraMetadata.CONTROL_AF_MODE_AUTO)
                    builder.set(
                        CaptureRequest.CONTROL_AF_TRIGGER,
                        CameraMetadata.CONTROL_AF_TRIGGER_START
                    )
                }
            }

            cameraCaptureSession?.setRepeatingRequest(builder.build(), null, handler)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    override fun _releaseCamera() {
        super._releaseCamera()

        try {
            previewSurface = null
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }
}