package com.proxy.service.camera.info.loader

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.view.Surface
import androidx.annotation.CallSuper
import com.proxy.service.camera.base.config.loader.LoaderConfig
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.info.loader.exception.PreviewSurfaceEmptyException
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/2/7 14:17
 * @desc:
 */
abstract class AbstractCameraPreviewLoader(
    config: LoaderConfig
) : AbstractCameraLoader(config) {

    protected var cameraMode: CameraMode? = config.getCameraMode()


    private var previewSurface: Surface? = null

    @Volatile
    protected var cameraCaptureSession: CameraCaptureSession? = null


    /**
     * 获取相机会话相关的 surface
     * */
    @CallSuper
    protected open fun findCaptureSessionSurfaces(list: ArrayList<Surface>) {
        previewSurface?.let {
            list.add(it)
        }
    }

    /**
     * 生成相机请求配置
     * */
    @CallSuper
    protected open fun parseCaptureRequestBuilder(builder: CaptureRequest.Builder) {
        val surface = previewSurface ?: throw PreviewSurfaceEmptyException()

        builder.addTarget(surface)
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
    }


    @CallSuper
    override fun setPreviewSurface(surface: Surface, mode: CameraMode) {
        if (isReleased.get()) {
            CsLogger.tag(tag).w("The camera has been released.")
            return
        }
        CsLogger.tag(tag).i("setPreviewSurface.")

        previewSurface = surface
        cameraMode = mode

        reCreateCaptureSession()
    }

    override fun reCreateCaptureSession() {
        CsLogger.tag(tag).i("reCreateCaptureSession.")

        handler.post {
            try {
                cameraCaptureSession?.stopRepeating()
                cameraCaptureSession?.abortCaptures()
            } catch (throwable: Throwable) {
                CsLogger.tag(tag).w(throwable)
            }

            try {
                val list = ArrayList<Surface>()
                findCaptureSessionSurfaces(list)
                if (list.isEmpty()) {
                    return@post
                }
                cameraDevice?.createCaptureSession(list, cameraCaptureSessionStateCallback, handler)
            } catch (throwable: Throwable) {
                CsLogger.tag(tag).e(throwable, "创建相机会话失败.")
            }
        }
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

        try {
            val builder = device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            parseCaptureRequestBuilder(builder)

            cameraCaptureSession?.setRepeatingRequest(builder.build(), null, handler)
        } catch (_: PreviewSurfaceEmptyException) {
            // 没有设置预览
            CsLogger.tag(tag).w("缺少预览 surface")
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable, "发起预览失败")
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

    override fun releaseOldCamera() {
        super.releaseOldCamera()

        try {
            cameraCaptureSession?.close()

            CsLogger.tag(tag).i("release cameraCaptureSession end")
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable, "release cameraCaptureSession error")
        } finally {
            cameraCaptureSession = null
        }
    }


    private val cameraCaptureSessionStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            CsLogger.tag(tag).i("createCaptureSession onConfigured.")
            cameraCaptureSession = session

            resumePreview()
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            CsLogger.tag(tag).e("createCaptureSession onConfigureFailed.")

            releaseOldCamera()
        }
    }
}