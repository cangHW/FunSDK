package com.proxy.service.camera.info.loader

import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.view.Surface
import com.proxy.service.camera.base.callback.TakePictureCallback
import com.proxy.service.camera.base.config.loader.LoaderConfig
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.info.loader.manager.CapturePhotoController
import com.proxy.service.camera.info.utils.CameraUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils

/**
 * @author: cangHX
 * @data: 2026/2/7 14:12
 * @desc:
 */
abstract class AbstractCameraPictureLoader(
    config: LoaderConfig
) : AbstractCameraPreviewLoader(config) {

    private val capturePhotoController = CapturePhotoController.create()

    override fun findCaptureSessionSurfaces(list: ArrayList<Surface>) {
        super.findCaptureSessionSurfaces(list)

        if (cameraMode == CameraMode.PICTURE) {
            capturePhotoController.getImageReader()?.let {
                list.add(it.surface)
            }
        }
    }

    override fun setPicturePreview(surface: Surface, width: Int, height: Int) {
        capturePhotoController.refresh(width, height, handler)
        super.setPicturePreview(surface, width, height)
    }

    override fun capturePhoto(isSavePhotoAlbum: Boolean, callback: TakePictureCallback?) {
        CsLogger.tag(tag).i("capturePhoto. isSavePhotoAlbum=$isSavePhotoAlbum")

        if (cameraMode != CameraMode.PICTURE) {
            CsLogger.tag(tag).w("It is not in photo mode at present.")
            return
        }

        val device = cameraDevice ?: return
        val imageReader = capturePhotoController.getImageReader() ?: return
        capturePhotoController.setCapturePhotoParams(isSavePhotoAlbum, callback)

        try {
            val rotation = CameraUtils.calculateRotation(
                cameraService.getSensorOrientation(cameraFaceMode),
                CsScreenUtils.getScreenRotation(),
                cameraFaceMode == CameraFaceMode.FaceFront
            )

            val builder = device.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            builder.addTarget(imageReader.surface)
            builder.set(CaptureRequest.JPEG_ORIENTATION, rotation)
            builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            cameraCaptureSession?.capture(builder.build(), null, handler)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable, "拍照失败.")
        }
    }

    override fun _releaseCamera() {
        super._releaseCamera()

        try {
            capturePhotoController.close()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }
}