package com.proxy.service.camera.info.loader.camera

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.view.Surface
import androidx.annotation.CallSuper
import com.proxy.service.camera.base.callback.loader.PreviewCallback
import com.proxy.service.camera.base.loader.camera.ICameraPreview
import com.proxy.service.camera.base.mode.PreviewImageFormatMode
import com.proxy.service.camera.info.loader.controller.func.preview.PreviewControllerImpl
import com.proxy.service.camera.info.loader.manager.CameraDeviceManager
import com.proxy.service.camera.info.loader.manager.CaptureSessionManager
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/3/23 16:48
 * @desc:
 */
abstract class AbstractCameraPreviewController : AbstractCameraController(), ICameraPreview {

    private var previewController: com.proxy.service.camera.info.loader.controller.IFunController? = null
    private var previewSurfaces: ArrayList<Surface> = ArrayList()

    private fun getSurfaces(): List<Surface> {
        val list = ArrayList<Surface>()
        list.addAll(previewSurfaces)
        previewController?.let {
            list.add(it.getSurface())
        }
        return list
    }

    override fun setPreviewCallback(
        format: PreviewImageFormatMode,
        pWidth: Int,
        pHeight: Int,
        callback: PreviewCallback
    ) {
        CsLogger.tag(tag)
            .i("setPreviewCallback. format=${format.name}, pWidth=$pWidth, pHeight=$pHeight")
        previewController?.destroy()
        previewController = PreviewControllerImpl.create(format, pWidth, pHeight, callback)
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

    override fun resumePreview() {
        super.resumePreview()
        if (!isStartPreview.get()) {
            return
        }

        if (getSurfaces().isEmpty()) {
            CsLogger.tag(tag).e("缺少预览 surface")
            return
        }

        createCaptureSession { device, session ->
            val list = getSurfaces()
            if (list.isEmpty()) {
                CsLogger.tag(tag).w("缺少预览 surface")
                return@createCaptureSession
            }

            try {
                val builder = device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                list.forEach {
                    builder.addTarget(it)
                }
                parsePreviewRequest(builder)
                session.setRepeatingRequest(builder.build(), null, handler)
            } catch (throwable: Throwable) {
                CsLogger.tag(tag).e(throwable, "发起预览失败")
            }
        }
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
     * 打开会话
     * */
    private fun createCaptureSession(callback: (device: CameraDevice, session: CameraCaptureSession) -> Unit) {
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
                        }
                    )
                }
            }
        )
    }

}