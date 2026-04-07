package com.proxy.service.camera.info.loader.manager

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.os.Handler
import android.view.Surface
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.atomic.AtomicBoolean

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

    private val isOpenState = AtomicBoolean(false)
    private val callbacks = ArrayList<Callback>()

    private var session: CameraCaptureSession? = null


    fun getCameraCaptureSession(): CameraCaptureSession? {
        return session
    }

    /**
     * 暂停重复请求, 用于暂停预览
     * */
    fun stopRepeating() {
        if (!isOpenState.get()) {
            CsLogger.tag(TAG)
                .d("CaptureSession has been suspended and there is no need to suspend it again")
            return
        }

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
        if (!isOpenState.compareAndSet(true, false)) {
            CsLogger.tag(TAG)
                .d("CaptureSession has been closed and there is no need to close it again")
            return
        }

        CsLogger.tag(TAG).i("closeCaptureSession.")

        handler.post {
            realCloseCaptureSession()
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
                realCloseCaptureSession()
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


    private fun realCloseCaptureSession() {
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
            isOpenState.set(false)
            session = null
        }
    }

    @Throws(Throwable::class)
    private fun realOpenCaptureSession(
        device: CameraDevice,
        surfaceCallback: OutputSurfaceCallback,
        callback: Callback
    ) {
        callbacks.add(callback)
        if (!isOpenState.compareAndSet(false, true)) {
            CsLogger.tag(TAG).w("The CaptureSession is in the process of turning on.")
            return
        }

        val surfaces = surfaceCallback.getOutputSurface()
        if (surfaces.isEmpty()) {
            CsLogger.tag(TAG).e("surfaces is empty.")
            forEachCallbacks {
                it.onConfigureFailed()
            }
            return
        }

        device.createCaptureSession(
            surfaces,
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    this@CaptureSessionManager.session = session
                    if (isOpenState.get()) {
                        CsLogger.tag(TAG).d("createCaptureSession onConfigured")

                        forEachCallbacks {
                            it.onConfigured(session)
                        }
                        isOpenState.set(true)
                    } else {
                        realCloseCaptureSession()
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    if (this@CaptureSessionManager.session == session) {
                        this@CaptureSessionManager.session = null
                        CsLogger.tag(TAG).e("createCaptureSession onConfigureFailed")

                        forEachCallbacks {
                            it.onConfigureFailed()
                        }

                        isOpenState.compareAndSet(true, false)
                    }
                }

                override fun onClosed(session: CameraCaptureSession) {
                    super.onClosed(session)
                    if (this@CaptureSessionManager.session == session) {
                        this@CaptureSessionManager.session = null
                        CsLogger.tag(TAG).d("createCaptureSession onClosed")

                        forEachCallbacks {
                            it.onClosed(session)
                        }

                        isOpenState.compareAndSet(true, false)
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