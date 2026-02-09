package com.proxy.service.camera.info.loader

import android.annotation.SuppressLint
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import androidx.annotation.CallSuper
import com.proxy.service.camera.base.config.loader.LoaderConfig
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.CameraFactory
import com.proxy.service.camera.base.loader.ICameraLoader
import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.af.FocusAreaInfo
import com.proxy.service.camera.info.CameraServiceImpl
import com.proxy.service.camera.info.loader.mode.EmptyCameraFaceMode
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2026/2/6 14:37
 * @desc:
 */
abstract class AbstractCameraLoader(
    private val config: LoaderConfig
) : ICameraLoader {

    protected val tag = "${CameraConstants.TAG}Loader"

    protected val cameraService = CameraServiceImpl()

    private val handlerThread = HandlerThread("thread-${System.currentTimeMillis()}").apply {
        start()
    }
    protected val handler = Handler(handlerThread.looper)

    protected var cameraFaceMode: CameraFaceMode = EmptyCameraFaceMode

    private val isReleased = AtomicBoolean(false)

    @Volatile
    protected var cameraDevice: CameraDevice? = null

    @Volatile
    protected var cameraCaptureSession: CameraCaptureSession? = null


    final override fun openCamera(mode: CameraFaceMode) {
        if (isReleased.get()) {
            CsLogger.tag(tag).w("The camera has been released.")
            return
        }

        CsLogger.tag(tag).i("openCamera. cameraId=${mode.getCameraId()}")
        _openCamera(mode)
    }

    @SuppressLint("MissingPermission")
    @CallSuper
    protected open fun _openCamera(mode: CameraFaceMode) {
        val cameraId = mode.getCameraId()
        if (cameraId.isNullOrEmpty()) {
            CsLogger.tag(tag)
                .e("当前摄像头模式不支持. cameraId=$cameraId, desc=${mode.getCameraDesc()}")
            return
        }
        if (cameraFaceMode != EmptyCameraFaceMode && cameraFaceMode != mode) {
            releaseOldCamera()
        }
        this.cameraFaceMode = mode
        CameraFactory.getCameraManager()?.openCamera(cameraId, cameraDeviceStateCallback, handler)
    }


    final override fun setCameraAfMode(mode: CameraAfMode) {
        if (isReleased.get()) {
            CsLogger.tag(tag).w("The camera has been released.")
            return
        }
        CsLogger.tag(tag).i("setCameraAfMode. mode=$mode")
        _setCameraAfMode(mode)
    }

    @CallSuper
    protected open fun _setCameraAfMode(mode: CameraAfMode) {
    }


    final override fun resumePreview() {
        if (isReleased.get()) {
            CsLogger.tag(tag).w("The camera has been released.")
            return
        }

        CsLogger.tag(tag).i("resumePreview")
        _resumePreview()
    }

    @CallSuper
    protected open fun _resumePreview() {
    }


    final override fun pausePreview() {
        if (isReleased.get()) {
            CsLogger.tag(tag).w("The camera has been released.")
            return
        }
        CsLogger.tag(tag).i("pausePreview")
        _pausePreview()
    }

    @CallSuper
    protected open fun _pausePreview() {
    }


    final override fun releaseCamera() {
        if (!isReleased.compareAndSet(false, true)) {
            CsLogger.tag(tag).w("There is no need to repeat release camera.")
            return
        }

        CsLogger.tag(tag).i("releaseCamera")

        _releaseCamera()

        try {
            handlerThread.quitSafely()
            handlerThread.join()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    @CallSuper
    protected open fun _releaseCamera() {
        releaseOldCamera()
    }


    /**
     * 获取相机会话相关的 surface
     * */
    @CallSuper
    protected abstract fun findCaptureSessionSurfaces(list: ArrayList<Surface>)


    protected fun releaseOldCamera() {
        CsLogger.tag(tag).i("releaseOldCamera")
        try {
            cameraDevice?.close()
            cameraCaptureSession?.close()

            CsLogger.tag(tag).i("releaseOldCamera end")
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        } finally {
            cameraDevice = null
            cameraCaptureSession = null
        }
    }

    protected fun createCaptureSession() {
        CsLogger.tag(tag).i("createCaptureSession.")

        try {
            val list = ArrayList<Surface>()
            findCaptureSessionSurfaces(list)
            if (list.isEmpty()) {
                return
            }
            cameraDevice?.createCaptureSession(list, cameraCaptureSessionStateCallback, handler)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable, "创建相机会话失败.")
        }
    }

    private val cameraDeviceStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            CsLogger.tag(tag).i("openCamera onOpened.")
            cameraDevice = camera
            createCaptureSession()
        }

        override fun onDisconnected(camera: CameraDevice) {
            CsLogger.tag(tag).i("openCamera onDisconnected.")
            try {
                camera.close()
            } catch (throwable: Throwable) {
                CsLogger.tag(tag).e(throwable)
            } finally {
                cameraDevice = null
            }

            releaseOldCamera()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            CsLogger.tag(tag).e("openCamera onError. error=$error")

            releaseOldCamera()
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