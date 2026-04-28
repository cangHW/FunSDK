package com.proxy.service.camera.info.loader.camera

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.view.Surface
import androidx.annotation.CallSuper
import com.proxy.service.camera.base.callback.loader.PreviewCallback
import com.proxy.service.camera.base.loader.camera.ICameraPreview
import com.proxy.service.camera.base.mode.loader.CameraErrorMode
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.info.loader.controller.IFunController
import com.proxy.service.camera.info.loader.controller.IFunController.IParamsController
import com.proxy.service.camera.info.loader.controller.IFunController.SurfaceChangedCallback
import com.proxy.service.camera.info.loader.controller.func.preview.PreviewControllerImpl
import com.proxy.service.camera.info.loader.converter.CustomCaptureCallback
import com.proxy.service.camera.info.loader.manager.CameraDeviceManager
import com.proxy.service.camera.info.loader.manager.CaptureSessionManager
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/3/23 16:48
 * @desc:
 */
abstract class AbstractCameraPreviewController : AbstractCameraController(), ICameraPreview,
    IParamsController {

    private var previewController: IFunController? = null
    private var previewSurfaces: ArrayList<Surface> = ArrayList()

    protected var lastTemplateType: Int = CameraDevice.TEMPLATE_PREVIEW
    protected var lastRequestSurfaces: List<Surface> = emptyList()
    protected var lastCaptureCallback: CameraCaptureSession.CaptureCallback? = null

    override fun getCameraFaceModeFromFunController(): CameraFaceMode? {
        return cameraFaceMode
    }

    private fun getSurfaces(): List<Surface> {
        val list = ArrayList<Surface>()
        list.addAll(previewSurfaces)
        previewController?.getSurface()?.let {
            list.add(it)
        }
        return list
    }

    override fun setPreviewCallback(callback: PreviewCallback) {
        CsLogger.tag(tag).i("setPreviewCallback.")
        previewController?.destroy()
        previewController = PreviewControllerImpl.create(callback)
        previewController?.setParamsController(this)
        previewController?.setSurfaceChangedCallback(object : SurfaceChangedCallback {
            override fun onSurfaceChanged() {
                captureSessionManager.closeCaptureSession()
            }

            override fun refreshPreview(
                templateType: Int,
                tempSurfaces: List<Surface>,
                success: () -> Unit,
                failed: () -> Unit
            ) {
                captureSessionManager.closeCaptureSession()
                requestPreview(templateType, tempSurfaces, success, failed)
            }
        })
        captureSessionManager.closeCaptureSession()
    }

    override fun setPreviewSurface(surface: Surface) {
        CsLogger.tag(tag).i("setPreviewSurface. surface=$surface")
        previewSurfaces.clear()
        previewSurfaces.add(surface)
        captureSessionManager.closeCaptureSession()
    }

    override fun addPreviewSurface(surface: Surface) {
        CsLogger.tag(tag).i("addPreviewSurface. surface=$surface")
        previewSurfaces.add(surface)
        captureSessionManager.closeCaptureSession()
    }

    override fun pausePreview() {
        super.pausePreview()
        previewController?.abort()
    }

    override fun resumePreview() {
        super.resumePreview()
        previewController?.abort()
        requestPreview(CameraDevice.TEMPLATE_PREVIEW, listOf(), null, null)
    }

    override fun onClear() {
        super.onClear()
        previewController?.destroy()
        previewController = null
        previewSurfaces.clear()
        lastRequestSurfaces = emptyList()
        lastCaptureCallback = null
    }

    /**
     * 获取相机会话相关的 surface
     * */
    @CallSuper
    protected open fun findCaptureSessionOutputSurfaces(list: ArrayList<Surface>) {
        CsLogger.tag(tag).i("findCaptureSessionOutputSurfaces.")
        list.addAll(getSurfaces())
    }

    /**
     * 处理预览相关参数
     * */
    @CallSuper
    protected open fun parsePreviewRequest(builder: CaptureRequest.Builder) {
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
    }

    /**
     * 预览请求
     * */
    protected fun requestPreview(
        templateType: Int,
        tempSurfaces: List<Surface>,
        success: (() -> Unit)?,
        failed: (() -> Unit)?
    ) {
        if (!isStartPreview.get()) {
            failed?.invoke()
            return
        }

        if (getSurfaces().isEmpty()) {
            CsLogger.tag(tag).e("缺少预览 surface")
            failed?.invoke()
            return
        }

        createCaptureSession(
            callback = { device, session ->
                val list = getSurfaces()
                if (list.isEmpty()) {
                    CsLogger.tag(tag).w("缺少预览 surface")
                    failed?.invoke()
                    return@createCaptureSession
                }

                try {
                    val builder = device.createCaptureRequest(templateType)
                    list.forEach {
                        builder.addTarget(it)
                    }
                    tempSurfaces.forEach {
                        builder.addTarget(it)
                    }
                    parsePreviewRequest(builder)

                    val captureCallback = CustomCaptureCallback(success, failed)

                    lastTemplateType = templateType
                    lastRequestSurfaces = list + tempSurfaces
                    lastCaptureCallback = captureCallback

                    session.setRepeatingRequest(builder.build(), captureCallback, handler)
                } catch (throwable: Throwable) {
                    CsLogger.tag(tag).e(throwable, "发起预览失败. device=$device, session=$session")
                    failed?.invoke()
                }
            },
            failed = failed
        )
    }

    /**
     * 打开会话
     * */
    private fun createCaptureSession(
        callback: (device: CameraDevice, session: CameraCaptureSession) -> Unit,
        failed: (() -> Unit)? = null
    ) {
        CsLogger.tag(tag).i("createCaptureSession.")
        cameraDeviceManager.openCamera(
            cameraFaceMode?.getCameraId() ?: "",
            object : CameraDeviceManager.Callback {
                override fun onOpened(device: CameraDevice) {
                    captureSessionManager.openCaptureSession(
                        device,
                        object : CaptureSessionManager.OutputSurfaceCallback {
                            override fun getOutputSurface(): List<Surface> {
                                val surfaces = ArrayList<Surface>()
                                if (getSurfaces().isNotEmpty()) {
                                    findCaptureSessionOutputSurfaces(surfaces)
                                }
                                return surfaces
                            }
                        },
                        object : CaptureSessionManager.Callback {
                            override fun onConfigured(session: CameraCaptureSession) {
                                callback(device, session)
                            }

                            override fun onConfigureFailed() {
                                super.onConfigureFailed()
                                failed?.invoke()
                            }
                        }
                    )
                }

                override fun onError(error: CameraErrorMode) {
                    super.onError(error)
                    failed?.invoke()
                }
            }
        )
    }
}