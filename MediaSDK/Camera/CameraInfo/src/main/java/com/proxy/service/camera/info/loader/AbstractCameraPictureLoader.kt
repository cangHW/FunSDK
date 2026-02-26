package com.proxy.service.camera.info.loader

import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.view.Surface
import com.proxy.service.camera.base.callback.TakePictureCallback
import com.proxy.service.camera.base.config.loader.LoaderConfig
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.info.loader.manager.TakePictureController
import com.proxy.service.camera.info.utils.CameraUtils
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/2/7 14:12
 * @desc:
 */
abstract class AbstractCameraPictureLoader(
    config: LoaderConfig
) : AbstractCameraPreviewLoader(config) {

    private val takePictureController = TakePictureController.create()

    override fun findCaptureSessionSurfaces(list: ArrayList<Surface>) {
        super.findCaptureSessionSurfaces(list)

        if (cameraMode == CameraMode.PICTURE) {
            takePictureController.getImageReader()?.let {
                list.add(it.surface)
            }
        }
    }

    override fun setPicturePreview(surface: Surface, width: Int, height: Int) {
        takePictureController.refresh(width, height, handler)
        super.setPicturePreview(surface, width, height)
    }

    override fun takePicture(isSavePhotoAlbum: Boolean, callback: TakePictureCallback?) {
        CsLogger.tag(tag).i("takePicture. isSavePhotoAlbum=$isSavePhotoAlbum")

        if (cameraMode != CameraMode.PICTURE) {
            CsLogger.tag(tag).w("It is not in photo mode at present.")
            return
        }

        val device = cameraDevice ?: return
        val imageReader = takePictureController.getImageReader() ?: return
        takePictureController.setTakePictureParams(isSavePhotoAlbum, callback)

        try {
            val rotation = CameraUtils.calculateRotation(
                cameraService.getSensorOrientation(cameraFaceMode),
                cameraFaceMode == CameraFaceMode.FaceFront
            )

            CsLogger.tag(tag).i("拍照旋转 rotation = $rotation")

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
            takePictureController.close()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }
}