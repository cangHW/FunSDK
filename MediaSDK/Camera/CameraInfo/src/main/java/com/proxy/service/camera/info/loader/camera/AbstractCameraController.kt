package com.proxy.service.camera.info.loader.camera

import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.CallSuper
import com.proxy.service.camera.base.callback.loader.CameraLoaderCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.camera.ICameraAction
import com.proxy.service.camera.base.loader.camera.ICameraActionGet
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.info.loader.manager.CameraDeviceManager
import com.proxy.service.camera.info.loader.manager.CaptureSessionManager
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2026/3/20 15:21
 * @desc:
 */
abstract class AbstractCameraController : ICameraAction, ICameraActionGet {

    protected val tag = "${CameraConstants.TAG}Loader"

    // 单线程
    private val handlerThread = HandlerThread("Camera-${System.currentTimeMillis()}").apply {
        start()
    }
    protected val handler = Handler(handlerThread.looper)

    protected val cameraDeviceManager = CameraDeviceManager.create(handler)
    protected val captureSessionManager = CaptureSessionManager.create(handler)

    //是否开启预览
    protected val isStartPreview = AtomicBoolean(false)

    //是否已释放
    protected val isReleased = AtomicBoolean(false)

    protected var cameraFaceMode: CameraFaceMode? = null

    fun setCameraLoaderCallback(callback: CameraLoaderCallback?){
        cameraDeviceManager.setCameraLoaderCallback(callback)
    }

    final override fun openCamera(mode: CameraFaceMode) {
        if (isReleased.get()) {
            CsLogger.tag(tag).w("The camera has been released.")
            return
        }

        CsLogger.tag(tag).i("openCamera. cameraId=${mode.getCameraId()}")

        val cameraId = mode.getCameraId()
        if (cameraId.isNullOrEmpty()) {
            CsLogger.tag(tag)
                .e("当前摄像头模式不支持. cameraId=$cameraId, desc=${mode.getCameraDesc()}")
            return
        }
        this.cameraFaceMode = mode
        cameraDeviceManager.openCamera(cameraId, null)
    }

    final override fun getOpenedCameraMode(): CameraFaceMode? {
        return cameraFaceMode
    }

    final override fun startPreview() {
        CsLogger.tag(tag).i("startPreview.")
        isStartPreview.set(true)
        resumePreview()
    }

    final override fun stopPreview() {
        CsLogger.tag(tag).i("stopPreview.")
        isStartPreview.set(false)
        captureSessionManager.closeCaptureSession()
    }

    @CallSuper
    override fun pausePreview() {
        CsLogger.tag(tag).i("pausePreview.")
        captureSessionManager.stopRepeating()
    }

    @CallSuper
    override fun resumePreview() {
        CsLogger.tag(tag).i("resumePreview.")
    }

    final override fun releaseCamera() {
        if (isReleased.compareAndSet(false, true)) {
            CsLogger.tag(tag).i("releaseCamera.")
            captureSessionManager.closeCaptureSession()
            cameraDeviceManager.closeCamera()
            onClear()

            handler.post {
                try {
                    handlerThread.quitSafely()
                    handlerThread.join()
                } catch (throwable: Throwable) {
                    CsLogger.tag(tag).e(throwable)
                }
            }
        }
    }

    /**
     * 清理
     * */
    @CallSuper
    open fun onClear() {
        CsLogger.tag(tag).i("onClear.")
    }

}