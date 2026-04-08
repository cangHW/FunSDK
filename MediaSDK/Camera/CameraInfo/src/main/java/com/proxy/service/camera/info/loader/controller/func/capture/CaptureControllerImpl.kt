package com.proxy.service.camera.info.loader.controller.func.capture

import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.os.Handler
import com.proxy.service.camera.base.callback.loader.PictureCaptureByteCallback
import com.proxy.service.camera.base.callback.loader.PictureCaptureCallback
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.SensorOrientationMode
import com.proxy.service.camera.info.loader.controller.IFunController
import com.proxy.service.camera.info.loader.manager.CameraDeviceManager
import com.proxy.service.camera.info.loader.manager.CaptureSessionManager
import com.proxy.service.camera.info.utils.FileUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.callback.IoCallback
import com.proxy.service.core.framework.io.file.media.CsFileMediaUtils
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.config.MimeType
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.service.media.CsMediaCamera
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.File

/**
 * @author: cangHX
 * @data: 2026/3/23 18:34
 * @desc:
 */
open class CaptureControllerImpl private constructor(
    private val deviceManager: CameraDeviceManager,
    private val sessionManager: CaptureSessionManager,
    private val handler: Handler
) : AbstractCaptureController(handler) {

    private data class CaptureBean(
        val isSavePhotoAlbum: Boolean,
        val localFile: File?,
        val callback: PictureCaptureCallback?,
        val byteCallback: PictureCaptureByteCallback?
    )

    companion object {

        fun create(
            deviceManager: CameraDeviceManager,
            sessionManager: CaptureSessionManager,
            handler: Handler
        ): CaptureControllerImpl {
            return CaptureControllerImpl(
                deviceManager,
                sessionManager,
                handler
            )
        }
    }

    private var paramsController: IFunController.IParamsController? = null
    private var captureBean: CaptureBean? = null


    override fun setParamsController(controller: IFunController.IParamsController) {
        paramsController = controller
    }


    override fun startPictureCapture(callback: PictureCaptureByteCallback?) {
        CsLogger.tag(tag).i("startPictureCapture.")
        captureBean = CaptureBean(
            false,
            null,
            null,
            callback
        )
        requestPictureCapture()
    }

    override fun startPictureCaptureToLocal(callback: PictureCaptureCallback?) {
        val file = FileUtils.getPictureCaptureFile()
        CsLogger.tag(tag).i("startPictureCaptureToLocal. filePath=${file.absolutePath}")
        captureBean = CaptureBean(
            false,
            file,
            callback,
            null
        )
        requestPictureCapture()
    }

    override fun startPictureCaptureToLocal(filePath: String, callback: PictureCaptureCallback?) {
        CsLogger.tag(tag).i("startPictureCaptureToLocal. filePath=$filePath")
        captureBean = CaptureBean(
            false,
            File(filePath),
            callback,
            null
        )
        requestPictureCapture()
    }

    override fun startPictureCaptureToAlbum(callback: PictureCaptureCallback?) {
        CsLogger.tag(tag).i("startPictureCaptureToAlbum.")
        captureBean = CaptureBean(
            true,
            null,
            callback,
            null
        )
        requestPictureCapture()
    }


    private fun requestPictureCapture() {
        val cameraDevice = deviceManager.getCameraDevice()
        if (cameraDevice == null) {
            CsLogger.tag(tag).e("camera is not open.")
            callFailed()
            return
        }
        val captureSession = sessionManager.getCameraCaptureSession()
        if (captureSession == null) {
            CsLogger.tag(tag).e("capture session is not create.")
            callFailed()
            return
        }
        val cameraFaceMode = paramsController?.getCameraFaceMode()
        if (cameraFaceMode == null) {
            CsLogger.tag(tag).e("cameraFaceMode is null.")
            callFailed()
            return
        }

        try {
            val orientation = CsMediaCamera.getSensorOrientation(cameraFaceMode)
                ?: SensorOrientationMode.ORIENTATION_0

            val rotation = calculateRotation(
                orientation,
                cameraFaceMode == CameraFaceMode.FaceFront
            )

            CsLogger.tag(tag).i("PictureCapture rotation = $rotation")

            val builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            builder.addTarget(getSurface())
            builder.set(CaptureRequest.JPEG_ORIENTATION, rotation)
            builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            captureSession.capture(builder.build(), null, handler)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable, "PictureCapture failed")
            callFailed()
        }
    }

    private fun callSuccess(bytes: ByteArray) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                try {
                    captureBean?.byteCallback?.onPictureCaptureSuccess(bytes)
                } catch (throwable: Throwable) {
                    CsLogger.tag(tag).e(throwable)
                } finally {
                    captureBean = null
                }
                return ""
            }
        })?.start()
    }

    override fun callSuccess(filePath: String) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                try {
                    captureBean?.callback?.onPictureCaptureSuccess(filePath)
                } catch (throwable: Throwable) {
                    CsLogger.tag(tag).e(throwable)
                } finally {
                    captureBean = null
                }
                return ""
            }
        })?.start()
    }

    override fun callFailed() {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                try {
                    captureBean?.callback?.onPictureCaptureFailed()
                    captureBean?.byteCallback?.onPictureCaptureFailed()
                } catch (throwable: Throwable) {
                    CsLogger.tag(tag).e(throwable)
                } finally {
                    captureBean = null
                }
                return ""
            }
        })?.start()
    }


    override fun saveImage(bytes: ByteArray) {
        val bean = captureBean ?: return

        if (bean.byteCallback != null) {
            callSuccess(bytes)
            return
        }

        if (bean.isSavePhotoAlbum) {
            savePhotoAlbum(bytes, FileUtils.createPictureCaptureFileName())
            return
        }

        val file = bean.localFile
        if (file == null) {
            CsLogger.tag(tag).e("The local path used to save the picture is lost.")
            callFailed()
            return
        }

        saveLocal(bytes, file)
    }

    private fun saveLocal(bytes: ByteArray, localFile: File) {
        CsFileWriteUtils.setSourceByte(bytes).writeAsync(
            localFile,
            callback = object : IoCallback {
                override fun onFailed() {
                    callFailed()
                }

                override fun onSuccess() {
                    callSuccess(localFile.absolutePath)
                }
            }
        )
    }

    private fun savePhotoAlbum(bytes: ByteArray, name: String) {
        CsFileMediaUtils.getImageManager()
            .setSourceByte(bytes)
            .setMimeType(MimeType.IMAGE_JPEG)
            .setDisplayName(name)
            .insert(object : InsertCallback {
                override fun onFailed() {
                    callFailed()
                }

                override fun onSuccess(path: String) {
                    callSuccess(path)
                }
            })
    }

    /**
     * 获取拍照时的旋转角度
     * */
    private fun calculateRotation(som: SensorOrientationMode, isFrontCamera: Boolean): Int {
        val rotationDegrees = CsScreenUtils.getScreenRotation().degree * 90
        val sign = if (isFrontCamera) 1 else -1
        return (som.degree + rotationDegrees * sign + 360) % 360
    }

}