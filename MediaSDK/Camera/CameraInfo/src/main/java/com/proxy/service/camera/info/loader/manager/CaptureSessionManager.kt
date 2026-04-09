package com.proxy.service.camera.info.loader.manager

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.os.Handler
import android.view.Surface
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/3/23 10:47
 * @desc:
 */
class CaptureSessionManager private constructor(private val handler: Handler) {

    interface OutputSurfaceCallback {
        fun getOutputSurface(): List<Surface>
    }

    interface Callback {
        fun onConfigured(session: CameraCaptureSession)

        fun onConfigureFailed() {}

        fun onClosed(session: CameraCaptureSession) {}
    }

    companion object {
        private const val TAG = "${CameraConstants.TAG}CaptureSession"

        fun create(handler: Handler): CaptureSessionManager {
            return CaptureSessionManager(handler)
        }
    }

    private val callbacks = ArrayList<Callback>()

    private var device: CameraDevice? = null
    private var session: CameraCaptureSession? = null


    fun getCameraCaptureSession(): CameraCaptureSession? {
        return session
    }

    /**
     * 暂停重复请求, 用于暂停预览
     * */
    fun stopRepeating() {
        CsLogger.tag(TAG).i("stopCaptureSession.")

        handler.post {
            try {
                session?.stopRepeating()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).w(throwable)
            }
        }
    }

    fun closeCaptureSession() {
        CsLogger.tag(TAG).i("closeCaptureSession.")

        handler.post {
            realCloseCaptureSession(session)
            clearCaptureSession()
        }
    }

    fun openCaptureSession(
        device: CameraDevice,
        surfaceCallback: OutputSurfaceCallback,
        callback: Callback
    ) {
        CsLogger.tag(TAG).i("openCaptureSession.")

        handler.post {
            if (session?.device != device) {
                realCloseCaptureSession(session)
                clearCaptureSession()
            }

            val ss = session
            if (ss != null) {
                CsLogger.tag(TAG).i("It has been enabled and can be reused")
                callback.onConfigured(ss)
                return@post
            }

            try {
                realOpenCaptureSession(device, surfaceCallback, callback)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
                callback.onConfigureFailed()
            }
        }
    }


    private fun realCloseCaptureSession(session: CameraCaptureSession?) {
        try {
            session?.stopRepeating()
            session?.abortCaptures()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).w(throwable)
        } finally {
            try {
                session?.close()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).w(throwable)
            }
        }
    }

    private fun clearCaptureSession() {
        device = null
        session = null
    }

    @Throws(Throwable::class)
    private fun realOpenCaptureSession(
        device: CameraDevice,
        surfaceCallback: OutputSurfaceCallback,
        callback: Callback
    ) {
        callbacks.add(callback)
        val surfaces = surfaceCallback.getOutputSurface()
        if (surfaces.isEmpty()) {
            CsLogger.tag(TAG).e("surfaces is empty.")
            forEachCallbacks {
                it.onConfigureFailed()
            }
            return
        }

        if (this.device == device) {
            CsLogger.tag(TAG).w("The CaptureSession is in the process of turning on.")
            return
        }
        this.device = device

        device.createCaptureSession(
            surfaces,
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    CsLogger.tag(TAG).d("onConfigured. cameraId=${session.device.id}")
                    if (this@CaptureSessionManager.device == session.device) {
                        this@CaptureSessionManager.session = session

                        forEachCallbacks {
                            it.onConfigured(session)
                        }
                    } else {
                        realCloseCaptureSession(session)
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    CsLogger.tag(TAG).e("onConfigureFailed. cameraId=${session.device.id}")
                    if (this@CaptureSessionManager.device == session.device) {
                        clearCaptureSession()

                        forEachCallbacks {
                            it.onConfigureFailed()
                        }
                    }
                }

                override fun onClosed(session: CameraCaptureSession) {
                    super.onClosed(session)
                    CsLogger.tag(TAG).d("onClosed. cameraId=${session.device.id}")
                    if (this@CaptureSessionManager.device == session.device) {
                        clearCaptureSession()

                        forEachCallbacks {
                            it.onClosed(session)
                        }
                    }
                }
            },
            handler
        )
    }

    private fun forEachCallbacks(call: (callback: Callback) -> Unit) {
        if (callbacks.size <= 0) {
            return
        }
        callbacks.forEach {
            call(it)
        }
        callbacks.clear()
    }

}