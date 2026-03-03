package com.proxy.service.camera.info.loader

import android.annotation.SuppressLint
import android.hardware.camera2.CameraDevice
import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.CallSuper
import com.proxy.service.camera.base.config.loader.LoaderConfig
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.CameraFactory
import com.proxy.service.camera.base.loader.ICameraLoader
import com.proxy.service.camera.base.mode.CameraFaceMode
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

    // 单线程
    private val handlerThread = HandlerThread("thread-${System.currentTimeMillis()}").apply {
        start()
    }
    protected val handler = Handler(handlerThread.looper)

    //是否已释放
    protected val isReleased = AtomicBoolean(false)


    protected val cameraService = CameraServiceImpl()
    protected var cameraFaceMode: CameraFaceMode = EmptyCameraFaceMode

    @Volatile
    protected var cameraDevice: CameraDevice? = null


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
     * 释放旧相机
     * */
    @CallSuper
    protected open fun releaseOldCamera() {
        CsLogger.tag(tag).i("releaseOldCamera")
        try {
            cameraDevice?.close()

            CsLogger.tag(tag).i("release cameraDevice end")
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable, "release cameraDevice error")
        } finally {
            cameraDevice = null
        }
    }


    private val cameraDeviceStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            CsLogger.tag(tag).i("openCamera onOpened.")
            cameraDevice = camera

            reCreateCaptureSession()
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

}