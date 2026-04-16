package com.proxy.service.camera.info.loader.manager

import android.annotation.SuppressLint
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.text.TextUtils
import com.proxy.service.camera.base.callback.loader.CameraLoaderCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.loader.CameraErrorMode
import com.proxy.service.camera.info.loader.factory.CameraFactory
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2026/3/19 20:02
 * @desc:
 */
class CameraDeviceManager private constructor(
    private val handler: Handler
) {

    interface Callback {
        fun onOpened(device: CameraDevice)

        fun onClosed(device: CameraDevice) {}

        fun onDisconnected(device: CameraDevice) {}

        fun onError(error: CameraErrorMode) {}
    }

    companion object {
        private const val TAG = "${CameraConstants.TAG}CameraDevice"

        fun create(handler: Handler): CameraDeviceManager {
            return CameraDeviceManager(handler)
        }
    }

    private var cameraLoaderCallback: CameraLoaderCallback? = null

    private val callbacks = ArrayList<Callback>()

    private var cameraId: String = ""
    private var cameraDevice: CameraDevice? = null

    fun setCameraLoaderCallback(callback: CameraLoaderCallback?) {
        this.cameraLoaderCallback = callback
    }

    fun getCameraDevice(): CameraDevice? {
        return cameraDevice
    }

    fun closeCamera() {
        CsLogger.tag(TAG).d("closeCamera. cameraId=$cameraId, camera=$cameraDevice")

        handler.post {
            CsLogger.tag(TAG).i("closeCamera Start. cameraId=$cameraId, camera=$cameraDevice")

            realCloseCamera(cameraDevice)
            clearCameraCache()
        }
    }

    fun openCamera(cameraId: String, callback: Callback?) {
        CsLogger.tag(TAG).d("openCamera. cameraId=$cameraId")
        if (TextUtils.isEmpty(cameraId)) {
            CsLogger.tag(TAG).e("camera id can not be null or empty.")
            callback?.onError(CameraErrorMode.ERROR_UNKNOWN)
            return
        }

        val manager = CameraFactory.getCameraManager()
        if (manager == null) {
            CsLogger.tag(TAG).e("camera manager service error.")
            callback?.onError(CameraErrorMode.ERROR_CAMERA_SERVICE)
            return
        }

        handler.post {
            CsLogger.tag(TAG).i("openCamera Start. cameraId=$cameraId")

            if (this.cameraId != "" && this.cameraId != cameraId) {
                CsLogger.tag(TAG).d("close old camera. camera=$cameraDevice")
                realCloseCamera(cameraDevice)
                clearCameraCache()
            }

            val device = cameraDevice
            if (device != null) {
                CsLogger.tag(TAG).i("It has been enabled and can be reused. camera=$cameraDevice")
                callback?.onOpened(device)
                return@post
            }

            try {
                realOpenCamera(manager, cameraId, callback)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
                callback?.onError(CameraErrorMode.ERROR_UNKNOWN)
            }
        }
    }


    private fun realCloseCamera(cameraDevice: CameraDevice?) {
        try {
            cameraDevice?.close()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).w(throwable)
        }
    }

    private fun clearCameraCache() {
        cameraId = ""
        cameraDevice = null
    }

    @SuppressLint("MissingPermission")
    private fun realOpenCamera(manager: CameraManager, cameraId: String, callback: Callback?) {
        callback?.let {
            callbacks.add(it)
        }
        if (this.cameraId == cameraId) {
            CsLogger.tag(TAG).w("The camera is in the process of turning on.")
            return
        }
        this.cameraId = cameraId

        manager.openCamera(
            cameraId,
            object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    CsLogger.tag(TAG).d("onOpened. cameraId=$cameraId, camera=$camera")

                    runMain {
                        cameraLoaderCallback?.onCameraOpened(camera.id, camera)
                    }

                    if (this@CameraDeviceManager.cameraId == camera.id) {
                        this@CameraDeviceManager.cameraDevice = camera
                        forEachCallbacks {
                            it.onOpened(camera)
                        }
                    } else {
                        realCloseCamera(camera)
                    }
                }

                override fun onClosed(camera: CameraDevice) {
                    super.onClosed(camera)
                    CsLogger.tag(TAG).d("onClosed. cameraId=$cameraId, camera=$camera")

                    runMain {
                        cameraLoaderCallback?.onCameraClosed(camera.id, camera)
                    }

                    if (this@CameraDeviceManager.cameraDevice == camera) {
                        clearCameraCache()
                        forEachCallbacks {
                            it.onClosed(camera)
                        }
                    }
                }

                override fun onDisconnected(camera: CameraDevice) {
                    CsLogger.tag(TAG).w("onDisconnected. cameraId=$cameraId, camera=$camera")

                    runMain {
                        cameraLoaderCallback?.onCameraDisconnected(camera.id, camera)
                    }

                    if (this@CameraDeviceManager.cameraDevice == camera) {
                        clearCameraCache()

                        forEachCallbacks {
                            it.onDisconnected(camera)
                        }
                    }
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    val mode = errorCodeToMode(error)
                    CsLogger.tag(TAG).e("onError. cameraId=$cameraId, mode=${mode.name}, camera=$camera")

                    runMain {
                        cameraLoaderCallback?.onCameraError(camera.id, camera, mode)
                    }

                    if (this@CameraDeviceManager.cameraDevice == camera) {
                        clearCameraCache()

                        forEachCallbacks {
                            it.onError(mode)
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

    private fun errorCodeToMode(error: Int): CameraErrorMode {
        return when (error) {
            CameraDevice.StateCallback.ERROR_CAMERA_IN_USE -> {
                CameraErrorMode.ERROR_CAMERA_IN_USE
            }

            CameraDevice.StateCallback.ERROR_MAX_CAMERAS_IN_USE -> {
                CameraErrorMode.ERROR_MAX_CAMERAS_IN_USE
            }

            CameraDevice.StateCallback.ERROR_CAMERA_DISABLED -> {
                CameraErrorMode.ERROR_CAMERA_DISABLED
            }

            CameraDevice.StateCallback.ERROR_CAMERA_DEVICE -> {
                CameraErrorMode.ERROR_CAMERA_DEVICE
            }

            CameraDevice.StateCallback.ERROR_CAMERA_SERVICE -> {
                CameraErrorMode.ERROR_CAMERA_SERVICE
            }

            else -> {
                CameraErrorMode.ERROR_UNKNOWN
            }
        }
    }

    private fun runMain(callback: Runnable) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                callback.run()
                return ""
            }
        })?.start()
    }
}