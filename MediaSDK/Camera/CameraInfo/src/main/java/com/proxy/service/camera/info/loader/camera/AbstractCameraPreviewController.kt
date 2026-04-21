package com.proxy.service.camera.info.loader.camera

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureFailure
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult
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
import com.proxy.service.camera.info.loader.manager.CameraDeviceManager
import com.proxy.service.camera.info.loader.manager.CaptureSessionManager
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2026/3/23 16:48
 * @desc:
 */
abstract class AbstractCameraPreviewController : AbstractCameraController(), ICameraPreview {

    private var previewController: IFunController? = null
    private var previewSurfaces: ArrayList<Surface> = ArrayList()

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
        previewController?.setParamsController(object : IParamsController {
            override fun getCameraFaceMode(): CameraFaceMode? {
                return cameraFaceMode
            }
        })
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
    }

    /**
     * 获取相机会话相关的 surface
     * */
    @CallSuper
    protected open fun findCaptureSessionSurfaces(list: ArrayList<Surface>) {
        CsLogger.tag(tag).i("findCaptureSessionSurfaces.")
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
                    val isCallbackDone = AtomicBoolean(false)

                    val builder = device.createCaptureRequest(templateType)
                    list.forEach {
                        builder.addTarget(it)
                    }
                    tempSurfaces.forEach {
                        builder.addTarget(it)
                    }
                    parsePreviewRequest(builder)
                    session.setRepeatingRequest(
                        builder.build(),
                        object : CameraCaptureSession.CaptureCallback() {
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

                            override fun onCaptureCompleted(
                                session: CameraCaptureSession,
                                request: CaptureRequest,
                                result: TotalCaptureResult
                            ) {
                                super.onCaptureCompleted(session, request, result)

                                if (isCallbackDone.compareAndSet(false, true)) {
                                    success?.invoke()
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
                        },
                        handler
                    )
                } catch (throwable: Throwable) {
                    CsLogger.tag(tag).e(throwable, "发起预览失败")
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
                                    findCaptureSessionSurfaces(surfaces)
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